package pt.isel.ls.storage

import kotlinx.datetime.LocalDate
import org.eclipse.jetty.util.security.Password
import pt.isel.ls.domain.Email
import pt.isel.ls.domain.Player
import pt.isel.ls.domain.Session
import pt.isel.ls.domain.SessionState
import pt.isel.ls.domain.info.AuthenticationParam
import pt.isel.ls.domain.info.GameInfo
import pt.isel.ls.domain.info.GameInfoParam
import pt.isel.ls.domain.info.PlayerInfo
import pt.isel.ls.domain.info.PlayerInfoParam
import pt.isel.ls.domain.info.SessionInfo
import pt.isel.ls.services.currentLocalDate
import pt.isel.ls.services.getSessionState
import kotlin.collections.HashMap

class SessionStorageStunt : SessionStorageInterface {
    private val player1 = PlayerInfo(1u, "test1")
    private val player2 = PlayerInfo(2u, "test2")
    private val completePlayer1 =
        Player(
            1u,
            "test1",
            "test1",
            Email("test1@gmail.com"),
            Password("test1"),
        )
    private val completePlayer2 =
        Player(
            2u,
            "test2",
            "test2",
            Email("test2@gmail.com"),
            Password("test2"),
        )

    private var sessionUuid: UInt = 4u
    private val date1 = currentLocalDate()
    private val players: Collection<PlayerInfo> = listOf(player1)
    private val players2: Collection<PlayerInfo> = listOf(player1, player2)
    private val completePlayers = listOf(completePlayer1, completePlayer2)
    private val gameInfo = GameInfo(1u, "Game")
    private val gameInfo2 = GameInfo(2u, "Game2")
    private val session1 = Session(1u, 2u, gameInfo, date1, player1, players2)
    private val session2 = Session(2u, 3u, gameInfo, date1, player1, players2)
    private val session3 = Session(3u, 2u, gameInfo2, date1, player1, players)
    private val hashSession: HashMap<UInt, Session> =
        hashMapOf(
            1u to session1,
            2u to session2,
            3u to session3,
        )

    override fun create(newItem: Session): UInt {
        val sid = sessionUuid++
        hashSession[sessionUuid] = newItem.copy(sid = sid)
        return sid
    }

    override fun read(
        sid: UInt,
        limit: UInt,
        offset: UInt,
    ): Session? =
        hashSession[sid]
            ?.copy(
                players = hashSession[sid]?.players?.drop(offset.toInt())?.take(limit.toInt()) ?: emptyList(),
            )

    override fun readBy(
        gameInfo: GameInfoParam?,
        date: LocalDate?,
        state: SessionState?,
        playerInfo: PlayerInfoParam?,
        offset: UInt,
        limit: UInt,
    ): Collection<SessionInfo> =
        hashSession
            .values
            .filter { session ->
                (gameInfo?.first == null || session.gameInfo.gid == gameInfo.first) &&
                    (gameInfo?.second == null || session.gameInfo.name == gameInfo.second) &&
                    (date == null || session.date == date) &&
                    (state == null || getSessionState(session) == state) &&
                    (
                        playerInfo == null ||
                            session.players.any { player ->
                                player.pid == playerInfo.first || player.username == playerInfo.second
                            }
                    )
            }
            .map { session ->
                SessionInfo(
                    session.sid ?: 0u,
                    session.owner,
                    session.gameInfo,
                    session.date,
                )
            }

    override fun updateAddPlayer(
        sid: UInt,
        pid: Collection<UInt>,
    ): Boolean {
        hashSession[sid]?.let { session ->
            val players = session.players.toMutableList()
            pid.forEach { pid ->
                if (players.any { player -> player.pid == pid }) {
                    return false
                }
                players.add(PlayerInfo(pid, "username"))
            }
            if (players.size > session.capacity.toInt()) {
                return false
            }
            hashSession[sid] = session.copy(players = players)
            return true
        }
        return false
    }

    override fun readPlayer(
        player: UInt,
        session: UInt,
    ): Player? =
        hashSession[session]?.players?.find { playerInfo -> playerInfo.pid == player }
            ?.let { playerInfo -> completePlayers.find { player -> player.pid == playerInfo.pid } }

    override fun updateCapacityOrDate(
        authentication: AuthenticationParam,
        capacity: UInt?,
        date: LocalDate?,
    ) {
        val (pid, sid) = authentication
        val sessionToUpdate = hashSession[sid]
        sessionToUpdate?.let { session ->
            if (session.owner.pid != pid) {
                throw IllegalArgumentException("Player is not the owner of the session.")
            }
            hashSession[sid] =
                session.copy(
                    capacity = capacity ?: sessionToUpdate.capacity,
                    date = date ?: sessionToUpdate.date,
                )
        }
    }

    override fun updateRemovePlayer(
        sid: UInt,
        pid: UInt,
        token: String,
    ) {
        hashSession[sid]?.let { session ->
            hashSession[sid] =
                session
                    .copy(
                        players = session.players.filter { player -> player.pid != pid && token != "invalid_token" },
                    )
        }
    }

    override fun delete(sid: UInt) {
        hashSession.remove(sid)
    }
}

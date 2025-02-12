package pt.isel.ls.storage

import kotlinx.datetime.LocalDate
import pt.isel.ls.domain.Session
import pt.isel.ls.domain.SessionState
import pt.isel.ls.domain.info.GameInfo
import pt.isel.ls.domain.info.GameInfoParam
import pt.isel.ls.domain.info.PlayerInfo
import pt.isel.ls.domain.info.PlayerInfoParam
import pt.isel.ls.domain.info.SessionInfo
import pt.isel.ls.services.getSessionState

class SessionStorageStunt : SessionStorageInterface {
    private val player1 = PlayerInfo(1u, "test1")
    private val player2 = PlayerInfo(2u, "test2")

    private var sessionUuid: UInt = 4u
    private val date1 = LocalDate(2024, 3, 10)
    private val players: Collection<PlayerInfo> = listOf(player1)
    private val players2: Collection<PlayerInfo> = listOf(player1, player2)
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

    override fun createSession(newItem: Session): UInt {
        val sid = sessionUuid++
        hashSession[sessionUuid] = newItem.copy(sid = sid)
        return sid
    }

    override fun readSession(
        sid: UInt,
        limit: UInt,
        offset: UInt,
    ): Session? =
        hashSession[sid]
            ?.copy(
                players = hashSession[sid]?.players?.drop(offset.toInt())?.take(limit.toInt()) ?: emptyList(),
            )

    override fun readSessions(
        gameInfo: GameInfoParam?,
        date: LocalDate?,
        state: SessionState?,
        playerInfo: PlayerInfoParam?,
        offset: UInt,
        limit: UInt,
    ): Collection<SessionInfo>? =
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
                                player.pid == playerInfo.first || player.userName == playerInfo.second
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
            .ifEmpty { null }

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

    override fun isPlayerInSession(
        player: UInt,
        session: UInt,
    ): Boolean {
        return hashSession[session]?.players?.any { playerInfo -> playerInfo.pid == player } ?: false
    }

    override fun updateCapacityOrDate(
        sid: UInt,
        capacity: UInt?,
        date: LocalDate?,
    ) {
        val sessionToUpdate = hashSession[sid]
        sessionToUpdate?.let { session ->
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
    ) {
        hashSession[sid]?.let { session ->
            hashSession[sid] = session.copy(players = session.players.filter { player -> player.pid != pid })
        }
    }

    override fun deleteSession(sid: UInt) {
        hashSession.remove(sid)
    }
}

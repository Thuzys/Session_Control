package pt.isel.ls.storage

import kotlinx.datetime.LocalDateTime
import pt.isel.ls.domain.Email
import pt.isel.ls.domain.Game
import pt.isel.ls.domain.Player
import pt.isel.ls.domain.Session
import pt.isel.ls.domain.SessionState
import pt.isel.ls.services.getSessionState

class SessionStorageStunt : SessionStorageInterface {
    private val defaultMail = Email("default@mail.com")
    private val player1 = Player(1u, "test1", defaultMail)
    private val player2 = Player(2u, "test2", defaultMail)

    private var sessionUuid: UInt = 4u
    private val date1 = LocalDateTime(2024, 3, 10, 12, 30)
    private val players: Collection<Player> = listOf(player1)
    private val players2: Collection<Player> = listOf(player1, player2)
    private val session1 = Session(1u, 2u, 1u, date1, players2)
    private val session2 = Session(2u, 3u, 1u, date1, players)
    private val session3 = Session(3u, 2u, 2u, date1, players)
    private val hashSession: HashMap<UInt, Session> =
        hashMapOf(
            1u to session1,
            2u to session2,
            3u to session3,
        )

    private val hashGame: HashMap<UInt, Game> =
        hashMapOf(
            1u to Game(1u, "test", "dev", listOf("genre")),
            2u to Game(2u, "test2", "dev2", listOf("genre2")),
            3u to Game(3u, "test3", "dev", listOf("genre")),
        )

    override fun createSession(newItem: Session): UInt {
        val sid = sessionUuid++
        hashSession[sessionUuid] = newItem.copy(sid = sid)
        return sid
    }

    override fun readSession(sid: UInt): Session? = hashSession[sid]

    override fun readSessions(
        gid: UInt?,
        date: LocalDateTime?,
        state: SessionState?,
        playerId: UInt?,
        offset: UInt,
        limit: UInt,
    ): Collection<Session>? =
        hashSession.values.filter { session ->
            (gid == null || session.gid == gid) &&
                (date == null || session.date == date) &&
                (state == null || getSessionState(session) == state) &&
                (playerId == null || session.players.any { player -> player.pid == playerId })
        }.ifEmpty { null }

    override fun updateAddPlayer(
        sid: UInt,
        newItem: Collection<Player>,
    ) {
        hashSession[sid]?.let { session ->
            hashSession[sid] = session.copy(players = newItem)
        }
    }

    override fun updateCapacityOrDate(
        sid: UInt,
        capacity: UInt?,
        date: LocalDateTime?,
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

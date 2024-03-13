package pt.isel.ls.storage

import kotlinx.datetime.LocalDateTime
import pt.isel.ls.domain.Email
import pt.isel.ls.domain.Game
import pt.isel.ls.domain.Player
import pt.isel.ls.domain.Session

class SessionStorageStunt : Storage<Session> {
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

    override fun create(newItem: Session): UInt {
        val sid = sessionUuid++
        hashSession[sessionUuid] = newItem.copy(sid = sid)
        return sid
    }

    override fun read(
        uInt: UInt?,
        offset: UInt,
        limit: UInt,
    ): Collection<Session>? {
        return if (uInt == null) {
            hashSession.values
        } else {
            val session = hashSession[uInt]
            return if (session != null) listOf(session) else null
        }
    }

    override fun update(
        uInt: UInt,
        newItem: Session,
    ) {
        hashSession[uInt] = newItem
    }
}

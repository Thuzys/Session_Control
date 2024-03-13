package pt.isel.ls.storage

import kotlinx.datetime.LocalDateTime
import pt.isel.ls.domain.Email
import pt.isel.ls.domain.Game
import pt.isel.ls.domain.Player
import pt.isel.ls.domain.Session

class StorageStunt : Storage {
    private val defaultMail = Email("default@mail.com")

    private var playerUuid: UInt = 3u
    private val player1 = Player(1u, "test1", defaultMail)
    private val player2 = Player(2u, "test20", defaultMail)
    private val hashPlayer: HashMap<UInt, Player> =
        hashMapOf(
            1u to player1,
            2u to player2,
        )

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

    private var gameUuid: UInt = 4u
    private val hashGame: HashMap<UInt, Domain> =
        hashMapOf(
            1u to Game(1u, "test", "dev", listOf("genre")),
            2u to Game(2u, "test2", "dev2", listOf("genre2")),
            3u to Game(3u, "test3", "dev", listOf("genre")),
        )

    override fun create(newItem: Domain): UInt =
        when (newItem) {
            is Player ->
                {
                    val uuid = playerUuid++
                    hashPlayer[uuid] = newItem.copy(uuid = uuid)
                    uuid
                }
            is Session ->
                {
                    val uuid = sessionUuid++
                    hashSession[sessionUuid] = newItem.copy(uuid = uuid)
                    uuid
                }
            is Game -> {
                hashGame[gameUuid] = newItem.copy(uuid = gameUuid)
                gameUuid
            }
            else -> 1u
        }

    override fun read(
        uInt: UInt?,
        type: Int,
        offset: UInt,
        limit: UInt,
    ): Collection<Domain>? =
        when (type) {
            Player.hash -> {
                val player = hashPlayer[uInt]
                if (player != null) listOf(player) else null
            }
            Game.hash -> {
                if (uInt == null) {
                    hashGame.values
                } else {
                    val game = hashGame[uInt]
                    if (game != null) listOf(game) else null
                }
            }
            Session.hash -> {
                if (uInt == null) {
                    hashSession.values
                } else {
                    val session = hashSession[uInt]
                    if (session != null) listOf(session) else null
                }
            }
            else -> null
        }

    override fun update(
        uInt: UInt,
        newItem: Domain,
    ) {
        when (newItem) {
            is Session ->
                {
                    hashSession[uInt] = newItem
                }
            else -> return
        }
    }

    override fun delete(uInt: UInt) {
        TODO("Not yet implemented")
    }
}

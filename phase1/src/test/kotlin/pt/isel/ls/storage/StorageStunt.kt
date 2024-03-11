package pt.isel.ls.storage

import pt.isel.ls.domain.Domain
import pt.isel.ls.domain.Email
import pt.isel.ls.domain.Game
import pt.isel.ls.domain.Player

class StorageStunt : Storage {
    private val defaultMail = Email("default@mail.com")

    private var playerUuid: UInt = 3u
    private val hashPlayer: HashMap<UInt, Player> =
        hashMapOf(
            1u to Player(1u, "test1", defaultMail),
            2u to Player(2u, "test20", defaultMail),
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
            is Player -> {
                val uuid = playerUuid++
                hashPlayer[uuid] = newItem.copy(uuid = uuid)
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
            else -> null
        }

    override fun update(
        uInt: UInt,
        newItem: Domain,
    ) {
        TODO("Not yet implemented")
    }

    override fun delete(uInt: UInt) {
        TODO("Not yet implemented")
    }
}

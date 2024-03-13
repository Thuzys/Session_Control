package pt.isel.ls.storage

import pt.isel.ls.domain.Email
import pt.isel.ls.domain.Player

class PlayerStorageStunt : Storage<Player> {
    private val defaultMail = Email("default@mail.com")
    private val player1 = Player(1u, "test1", defaultMail)
    private val player2 = Player(2u, "test20", defaultMail)
    private var uid = 3u
    private val players: HashMap<UInt, Player> =
        hashMapOf(
            1u to player1,
            2u to player2,
        )

    override fun create(newItem: Player): UInt =
        uid++.also {
            players[it] = newItem.copy(uuid = it)
        }

    override fun read(
        uInt: UInt?,
        offset: UInt,
        limit: UInt,
    ): Collection<Player>? =
        if (uInt == null) {
            players.values.drop(offset.toInt()).take(limit.toInt())
        } else {
            players[uInt]?.let { listOf(it) }
        }

    override fun update(
        uInt: UInt,
        newItem: Player,
    ) {
        TODO("Not yet implemented")
    }
}

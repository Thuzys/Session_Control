package pt.isel.ls.storage

import pt.isel.ls.domain.Email
import pt.isel.ls.domain.Player

class PlayerStorageStunt : PlayerStorageInterface {
    private val defaultMail = Email("default@mail.com")
    private val player1 = Player(1u, "test1", "test1", defaultMail)
    private val player2 = Player(2u, "test2", "test2", defaultMail)
    private var uid: UInt = 3u
    private val players =
        hashMapOf(
            1u to player1,
            2u to player2,
        )

    override fun create(newItem: Player): UInt {
        require(!players.map { it.value.email }.contains(newItem.email)) { "Email already exists." }
        require(!players.map { it.value.userName }.contains(newItem.userName)) { "UserName already exists." }
        players[uid++] = newItem
        return uid - 1u
    }

    override fun read(pid: UInt): Player = players[pid] ?: throw NoSuchElementException("Player not found.")

    override fun readBy(
        email: Email?,
        token: String?,
        userName: String?,
        limit: UInt,
        offset: UInt,
    ): Collection<Player>? =
        players
            .values
            .filter { it.email == email || token.toString().isNotEmpty() || it.userName == userName }
            .drop(offset.toInt())
            .take(limit.toInt())
            .ifEmpty { null }

    override fun update(
        uInt: UInt,
        newItem: Player,
    ) {
        TODO("Not yet implemented")
    }

    override fun delete(uInt: UInt) {
        TODO("Not yet implemented")
    }
}

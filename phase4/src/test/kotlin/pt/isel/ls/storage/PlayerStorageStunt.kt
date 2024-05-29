package pt.isel.ls.storage

import org.eclipse.jetty.util.security.Password
import pt.isel.ls.domain.Email
import pt.isel.ls.domain.Player
import pt.isel.ls.domain.errors.StorageError
import java.util.UUID

class PlayerStorageStunt(pid: UInt) : PlayerStorageInterface {
    private val defaultMail = Email("default@mail.com")
    private val password = Password("password")
    private val playerToken = UUID.randomUUID()
    private val player1 = Player(pid, "test1", "test1", defaultMail, password, playerToken)
    private val player2 = Player(2u, "test2", "test2", defaultMail, password)
    private var uid: UInt = 3u
    private val players =
        hashMapOf(
            1u to player1,
            2u to player2,
        )

    override fun create(newItem: Player): UInt {
        if (players.map { it.value.email }.contains(newItem.email)) {
            throw StorageError("Email already exists.")
        }
        if (players.map { it.value.username }.contains(newItem.username)) {
            throw StorageError("Username already exists.")
        }
        players[uid++] = newItem
        return uid - 1u
    }

    override fun read(pid: UInt): Player = players[pid] ?: throw NoSuchElementException("Player not found.")

    override fun readBy(
        email: Email?,
        token: String?,
        username: String?,
        limit: UInt,
        offset: UInt,
    ): Collection<Player>? =
        players
            .values
            .filter { it.email == email || token.toString().isNotEmpty() || it.username == username }
            .drop(offset.toInt())
            .take(limit.toInt())
            .ifEmpty { null }

    override fun update(newItem: Player) {
        val pid = newItem.pid
        checkNotNull(pid) { "Player not found." }
        players[pid] = newItem
    }

    override fun delete(uInt: UInt) {
        TODO("Not yet implemented")
    }

    override fun deleteToken(pid: UInt) {
        // do nothing
    }
}

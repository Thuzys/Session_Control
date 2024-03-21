package pt.isel.ls.storage

import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.domain.Email
import pt.isel.ls.domain.Player
import pt.isel.ls.utils.executeCommand
import pt.isel.ls.utils.makePlayers
import java.sql.Statement

/**
 * Represents the storage of player data.
 *
 * This class implements the [PlayerStorageInterface] and uses a PostgreSQL database to perform the operations.
 *
 * @param envName The name of the environment variable that contains the connection URL to the database.
 */
class PlayerStorage(envName: String) : PlayerStorageInterface {
    private val dataSource: PGSimpleDataSource = PGSimpleDataSource()

    init {
        val connectionURL = System.getenv(envName)
        dataSource.setURL(connectionURL)
    }

    override fun create(newItem: Player): UInt =
        dataSource.connection.use { connection ->
            connection.executeCommand {
                val insertQuery = "INSERT INTO PLAYER (name, email, token) VALUES (?, ?, ?)"
                val stm =
                    prepareStatement(
                        insertQuery,
                        Statement.RETURN_GENERATED_KEYS,
                    )
                stm.setString(1, newItem.name)
                stm.setString(2, newItem.email.email)
                stm.setString(3, newItem.token.toString())
                stm.executeUpdate()
                val key = stm.generatedKeys
                check(key.next()) { "No key returned" }
                val pid = key.getInt(1)
                pid.toUInt()
            }
        }

    override fun read(pid: UInt): Player =
        dataSource.connection.use { connection ->
            connection.executeCommand {
                val selectQuery = "SELECT pid, name, email, token FROM PLAYER WHERE pid = ?"
                val stmt = connection.prepareStatement(selectQuery)
                stmt.setInt(1, pid.toInt())
                val collection = makePlayers(stmt)
                collection.firstOrNull() ?: throw NoSuchElementException("Player not found.")
            }
        }

    override fun readBy(
        email: Email?,
        token: String?,
        limit: UInt,
        offset: UInt,
    ): Collection<Player>? =
        dataSource.connection.use { connection ->
            connection.executeCommand {
                val selectQuery =
                    "SELECT pid, name, email, token FROM PLAYER " +
                        "WHERE email = ? OR token = ? OFFSET ? LIMIT ?"
                val stmt = connection.prepareStatement(selectQuery)
                stmt.setString(1, email?.email)
                stmt.setString(2, token)
                stmt.setInt(3, offset.toInt())
                stmt.setInt(4, limit.toInt())
                makePlayers(stmt).ifEmpty { null }
            }
        }

    override fun update(
        uInt: UInt,
        newItem: Player,
    ) {
        TODO("Not needed for this phase.")
    }

    override fun delete(uInt: UInt) {
        TODO("Not needed for this phase.")
    }
}

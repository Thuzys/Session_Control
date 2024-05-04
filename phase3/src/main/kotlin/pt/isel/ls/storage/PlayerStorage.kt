package pt.isel.ls.storage

import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.domain.Email
import pt.isel.ls.domain.Player
import java.sql.Statement

/**
 * Represents the storage of player data.
 *
 * This class implements the [PlayerStorageInterface] and uses a PostgresSQL database to perform the operations.
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
                val insertQuery = "INSERT INTO PLAYER (name, email, token, username) VALUES (?, ?, ?, ?)"
                val stm =
                    prepareStatement(
                        insertQuery,
                        Statement.RETURN_GENERATED_KEYS,
                    )
                stm.setString(1, newItem.name)
                stm.setString(2, newItem.email.email)
                stm.setString(3, newItem.token.toString())
                stm.setString(4, newItem.userName)
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
                val selectQuery = "SELECT pid, name, username, email, token FROM PLAYER WHERE pid = ?"
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
                    "SELECT pid, name, email, userName, token FROM PLAYER " +
                        "WHERE email = ? OR token = ? OFFSET ? LIMIT ?"
                val stmt = connection.prepareStatement(selectQuery)
                var idx = 1
                stmt.setString(idx++, email?.email)
                stmt.setString(idx++, token)
                stmt.setInt(idx++, offset.toInt())
                stmt.setInt(idx, limit.toInt())
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

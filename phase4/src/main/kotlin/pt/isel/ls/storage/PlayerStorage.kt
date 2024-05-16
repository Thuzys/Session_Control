package pt.isel.ls.storage

import org.eclipse.jetty.util.security.Password
import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.domain.Email
import pt.isel.ls.domain.Player
import java.sql.Statement

/**
 * Represents the storage of player data.
 *
 * This class implements the [PlayerStorageInterface] and uses a PostgresSQL database to perform the operations.
 * @param envName The name of the environment variable that contains the connection URL to the database.
 * @property dataSource The data source used to connect to the database.
 * @constructor Creates an instance of [PlayerStorage] with the given environment variable name.
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
                val insertQuery = "INSERT INTO PLAYER (name, email, token, username, password) VALUES (?, ?, ?, ?, ?)"
                val stm =
                    prepareStatement(
                        insertQuery,
                        Statement.RETURN_GENERATED_KEYS,
                    )
                var idx = 1
                stm.setString(idx++, newItem.name)
                stm.setString(idx++, newItem.email.email)
                stm.setString(idx++, newItem.token.toString())
                stm.setString(idx++, newItem.username)
                stm.setString(idx, Password.obfuscate(newItem.password.toString()))
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
                val selectQuery = "SELECT pid, name, username, email, token, password FROM PLAYER WHERE pid = ?"
                val stmt = connection.prepareStatement(selectQuery)
                stmt.setInt(1, pid.toInt())
                val collection = makePlayers(stmt)
                collection.firstOrNull() ?: throw NoSuchElementException("Player not found.")
            }
        }

    override fun readBy(
        email: Email?,
        token: String?,
        userName: String?,
        limit: UInt,
        offset: UInt,
    ): Collection<Player>? =
        dataSource.connection.use { connection ->
            connection.executeCommand {
                val selectQuery =
                    "SELECT pid, name, email, userName, token, password FROM PLAYER " +
                        "WHERE email = ? OR token = ? or userName = ? " +
                        "OFFSET ? LIMIT ?"
                val stmt = connection.prepareStatement(selectQuery)
                var idx = 1
                stmt.setString(idx++, email?.email)
                stmt.setString(idx++, token)
                stmt.setString(idx++, userName)
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

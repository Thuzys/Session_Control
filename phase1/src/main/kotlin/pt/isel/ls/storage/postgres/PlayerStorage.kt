package pt.isel.ls.storage.postgres

import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.domain.Player
import pt.isel.ls.storage.Storage
import pt.isel.ls.utils.executeCommand
import pt.isel.ls.utils.makePlayers
import java.sql.Statement

class PlayerStorage(envName: String) : Storage<Player> {
    private val dataSource: PGSimpleDataSource = PGSimpleDataSource()

    init {
        val connectionURL = System.getenv(envName)
        dataSource.setURL(connectionURL)
    }

    override fun create(newItem: Player): UInt =
        dataSource.connection.use {
            it.executeCommand {
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

    override fun read(
        uInt: UInt?,
        offset: UInt,
        limit: UInt,
    ): Collection<Player>? =
        dataSource.connection.use { connection ->
            connection.executeCommand {
                uInt?.let { pid ->
                    val selectQuery = "SELECT pid, name, email, token FROM PLAYER WHERE pid = ?"
                    val stmt = connection.prepareStatement(selectQuery)
                    stmt.setInt(1, pid.toInt())
                    val collection = makePlayers(stmt)
                    collection.ifEmpty { null }
                } ?: connection.run {
                    val selectQuery = "SELECT pid, name, email, token FROM PLAYER OFFSET ? LIMIT ?"
                    val stmt = prepareStatement(selectQuery)
                    stmt.setInt(1, offset.toInt())
                    stmt.setInt(2, limit.toInt())
                    val collection = makePlayers(stmt)
                    collection.ifEmpty { null }
                }
            }
        }

    override fun delete(uInt: UInt) {
        TODO("Not needed for this phase.")
    }

    override fun update(
        uInt: UInt,
        newItem: Player,
    ) {
        TODO("Not needed for this phase.")
    }
}

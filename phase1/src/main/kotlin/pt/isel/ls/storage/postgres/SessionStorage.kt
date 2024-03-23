package pt.isel.ls.storage.postgres

import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.domain.Session
import pt.isel.ls.storage.Storage
import pt.isel.ls.storage.executeCommand
import pt.isel.ls.storage.makeSession
import java.sql.Statement

class SessionStorage(envName: String) : Storage<Session> {
    private val dataSource = PGSimpleDataSource()

    init {
        val connectionURL = System.getenv(envName)
        dataSource.setURL(connectionURL)
    }

    override fun create(newItem: Session): UInt =
        dataSource.connection.use { connection ->
            connection.executeCommand {
                val insertSessionCMD = "INSERT INTO SESSION (capacity, gid, date) VALUES (?,?,?);"
                val stmt1 = connection.prepareStatement(insertSessionCMD, Statement.RETURN_GENERATED_KEYS)
                stmt1.setInt(1, newItem.capacity.toInt())
                stmt1.setInt(2, newItem.gid.toInt())
                stmt1.setString(3, newItem.date.toString())
                stmt1.executeUpdate()
                val key = stmt1.generatedKeys
                require(key.next()) { "No key returned" }
                val sid = key.getInt(1)
                val insertAssociation = "INSERT INTO PLAYER_SESSION (pid, sid) VALUES (?,?);"
                val stmt2 = connection.prepareStatement(insertAssociation)
                newItem.players.forEach { player ->
                    player.pid?.let { it1 -> stmt2.setInt(1, it1.toInt()) }
                    stmt2.setInt(2, sid)
                    stmt2.executeUpdate()
                }
                sid.toUInt()
            }
        }

    override fun read(
        uInt: UInt?,
        offset: UInt,
        limit: UInt,
    ): Collection<Session>? =
        dataSource.connection.use { connection ->
            uInt?.toInt()?.let { sid ->
                val selectSessionCMD = "SELECT sid, capacity, gid, date FROM SESSION WHERE sid = ?;"
                val stmt1 = connection.prepareStatement(selectSessionCMD)
                stmt1.setInt(1, sid)
                val collection = connection.makeSession(stmt1)
                collection.ifEmpty { null }
            }
                ?: connection.run {
                    val selectSessionCMD = "SELECT sid, capacity, gid, date FROM SESSION OFFSET ? LIMIT ?;"
                    val stmt2 = connection.prepareStatement(selectSessionCMD)
                    stmt2.setInt(1, offset.toInt())
                    stmt2.setInt(2, limit.toInt())
                    val collection = connection.makeSession(stmt2)
                    collection.ifEmpty { null }
                }
        }

    override fun update(
        uInt: UInt,
        newItem: Session,
    ) = dataSource.connection.use { connection ->
        val insertPlayerCMD =
            "INSERT INTO PLAYER_SESSION (pid, sid) " +
                "SELECT ?, ?" +
                "WHERE NOT EXISTS (" +
                "SELECT 1 FROM PLAYER_SESSION WHERE pid = ? AND sid = ?);"
        val stmt1 = connection.prepareStatement(insertPlayerCMD)
        newItem.players.forEach { player ->
            player.pid?.let { it1 -> stmt1.setInt(1, it1.toInt()) }
            stmt1.setInt(2, uInt.toInt())
            player.pid?.let { stmt1.setInt(3, it.toInt()) }
            stmt1.setInt(4, uInt.toInt())
            stmt1.executeUpdate()
        }
    }

    override fun delete(uInt: UInt) {
        TODO("Not yet implemented")
    }
}

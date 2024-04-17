package pt.isel.ls.storage

import kotlinx.datetime.LocalDateTime
import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.domain.Player
import pt.isel.ls.domain.Session
import pt.isel.ls.domain.SessionState
import java.sql.PreparedStatement
import java.sql.SQLException
import java.sql.Statement

class SessionStorage(envName: String) : SessionStorageInterface {
    private val dataSource = PGSimpleDataSource()

    init {
        val connectionURL = System.getenv(envName)
        dataSource.setURL(connectionURL)
    }

    override fun createSession(newItem: Session): UInt =
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

    override fun readSession(sid: UInt): Session? =
        dataSource.connection.use { connection ->
            connection.executeCommand {
                sid.toInt().let { sid ->
                    val selectSessionCMD = "SELECT sid, capacity, gid, date FROM SESSION WHERE sid = ?;"
                    val stmt1 = connection.prepareStatement(selectSessionCMD)
                    stmt1.setInt(1, sid)
                    val collection = connection.makeSession(stmt1)
                    collection.firstOrNull()
                }
            }
        }

    override fun readSessions(
        gid: UInt?,
        date: LocalDateTime?,
        state: SessionState?,
        playerId: UInt?,
        offset: UInt,
        limit: UInt,
    ): Collection<Session>? =
        dataSource.connection.use { connection ->
            connection.executeCommand {
                val selectSessionCMD =
                    "SELECT s.sid, s.capacity, s.gid, s.date\n" +
                            "FROM session s\n" +
                            "    LEFT JOIN player_session ps ON ps.sid = s.sid\n" +
                            "WHERE (s.gid = ? or ? = 0)\n" +
                            "   AND (s.date = ? or ? = 'null')\n" +
                            "   AND (ps.pid = ? or ? = 0)\n" +
                            "GROUP BY s.sid, s.capacity, s.gid, s.date\n" +
                            "HAVING (? = 'null') OR\n" +
                            "       (? = 'OPEN' AND s.capacity > count(ps.pid)) OR\n" +
                            "       (? = 'CLOSE' AND s.capacity = count(ps.pid))\n" +
                            "OFFSET ? LIMIT ?;"
                val stmt2 = connection.prepareStatement(selectSessionCMD)
                var idx = 1
                repeat(2) {
                    stmt2.setInt(idx++, gid?.toInt() ?: 0)
                }
                repeat(2) {
                    stmt2.setString(idx++, date.toString())
                }
                repeat(2) {
                    stmt2.setInt(idx++, playerId?.toInt() ?: 0)
                }
                repeat(3) {
                    stmt2.setString(idx++, state.toString())
                }
                stmt2.setInt(idx++, offset.toInt())
                stmt2.setInt(idx, limit.toInt())
                val collection = connection.makeSession(stmt2)
                collection.ifEmpty { null }
            }
        }

    override fun updateAddPlayer(
        sid: UInt,
        newItem: Collection<Player>,
    ) = dataSource.connection.use { connection ->
        connection.executeCommand {
            val insertPlayerCMD =
                "INSERT INTO PLAYER_SESSION (pid, sid) " +
                        "SELECT ?, ?" +
                        "WHERE NOT EXISTS (" +
                        "SELECT 1 FROM PLAYER_SESSION WHERE pid = ? AND sid = ?);"
            val stmt1 = connection.prepareStatement(insertPlayerCMD)
            newItem.forEach { player ->
                player.pid?.let { it1 -> stmt1.setInt(1, it1.toInt()) }
                stmt1.setInt(2, sid.toInt())
                player.pid?.let { stmt1.setInt(3, it.toInt()) }
                stmt1.setInt(4, sid.toInt())
                stmt1.executeUpdate()
            }
        }
    }

    private fun makePlayerList(stmt2: PreparedStatement): MutableList<Int> {
        val response = stmt2.executeQuery()
        val players = emptyList<Int>().toMutableList()
        while (response.next()) {
            players.add(response.getInt(1))
        }
        return players
    }

    override fun updateCapacityOrDate(
        sid: UInt,
        capacity: UInt?,
        date: LocalDateTime?,
    ) {
        dataSource.connection.use { connection ->
            connection.executeCommand {
                val updateCMD =
                    "UPDATE SESSION\n" +
                            "SET\n" +
                            "    capacity = CASE WHEN ? IS NOT NULL THEN ? ELSE capacity END,\n" +
                            "    date = CASE WHEN ? IS NOT NULL THEN ? ELSE date END\n" +
                            "WHERE sid = ?;"
                val stmt1 = connection.prepareStatement(updateCMD)
                var idx = 1
                repeat(2) { stmt1.setInt(idx++, capacity?.toInt() ?: 0) }
                repeat(2) { stmt1.setString(idx++, date.toString()) }
                stmt1.setInt(idx, sid.toInt())
                stmt1.executeUpdate()
            }
        }
    }

    override fun deleteSession(sid: UInt) {
        dataSource.connection.use { connection ->
            connection.executeCommand {
                val deletePlayerSession = "DELETE FROM PLAYER_SESSION WHERE sid = ?;"
                val deleteSessionCMD = "DELETE FROM SESSION WHERE sid = ?;"
                val stmt1 = connection.prepareStatement(deleteSessionCMD)
                val stmt2 = connection.prepareStatement(deletePlayerSession)
                stmt2.setUInt(1, sid)
                stmt1.setUInt(1, sid)
                stmt2.executeUpdate()
                stmt1.executeUpdate()
            }
        }
    }

    override fun updateAddPlayer(
        sid: UInt,
        pid: Collection<UInt>,
    ): Boolean {
        return dataSource.connection.use { connection ->
            connection.executeCommand {
                val insertCMD = "INSERT INTO PLAYER_SESSION (pid, sid) VALUES (?, ?);"
                val stmt1 = connection.prepareStatement(insertCMD)
                stmt1.setUInt(2, sid)
                try {
                    pid.forEach { player ->
                        stmt1.setUInt(1, player)
                        stmt1.executeUpdate()
                    }
                    true
                } catch (e: SQLException) {
                    rollback()
                    false
                }
            }
        }
    }

//    override fun getPlayerSessions(pid: UInt): Collection<Int> =
//        dataSource.connection.use { connection ->
//            connection.executeCommand {
//                val selectSessionCMD = "SELECT sid FROM PLAYER_SESSION WHERE pid = ?;"
//                val stmt1 = connection.prepareStatement(selectSessionCMD)
//                stmt1.setInt(1, pid.toInt())
//                val response= stmt1.executeQuery()
//                val collection = emptyList<Int>().toMutableList()
//                while (response.next()) {
//                    collection.add(response.getInt(1))
//                }
//                collection
//            }
//        }

    override fun updateRemovePlayer(
        sid: UInt,
        pid: UInt,
    ) {
        dataSource.connection.use { connection ->
            connection.executeCommand {
                val deletePlayerCMD = "DELETE FROM PLAYER_SESSION WHERE pid = ? AND sid = ?;"
                val stmt1 = connection.prepareStatement(deletePlayerCMD)
                stmt1.setInt(1, pid.toInt())
                stmt1.setInt(2, sid.toInt())
                stmt1.executeUpdate()
            }
        }
    }
}

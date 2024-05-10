package pt.isel.ls.storage

import kotlinx.datetime.LocalDate
import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.domain.Session
import pt.isel.ls.domain.SessionState
import pt.isel.ls.domain.info.GameInfoParam
import pt.isel.ls.domain.info.PlayerInfoParam
import pt.isel.ls.domain.info.SessionInfo
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
                requireNotNull(newItem.owner.pid) { "Owner must have a pid" }
                val insertSessionCMD = "INSERT INTO SESSION (capacity, gid, date, owner) VALUES (?,?,?,?);"
                val stmt1 = connection.prepareStatement(insertSessionCMD, Statement.RETURN_GENERATED_KEYS)
                var idx = 1
                stmt1.setInt(idx++, newItem.capacity.toInt())
                stmt1.setInt(idx++, newItem.gameInfo.gid.toInt())
                stmt1.setDate(idx++, java.sql.Date.valueOf(newItem.date.toString()))
                stmt1.setInt(idx, newItem.owner.pid.toInt())
                stmt1.executeUpdate()
                val key = stmt1.generatedKeys
                require(key.next()) { "No key returned" }
                val sid = key.getInt(1)
                val insertAssociation = "INSERT INTO PLAYER_SESSION (pid, sid) VALUES (?,?);"
                val stmt2 = connection.prepareStatement(insertAssociation)
                newItem.players.forEach { player ->
                    player.pid.let { it1 -> stmt2.setInt(1, it1.toInt()) }
                    stmt2.setInt(2, sid)
                    stmt2.executeUpdate()
                }
                sid.toUInt()
            }
        }

    override fun readSession(
        sid: UInt,
        limit: UInt,
        offset: UInt,
    ): Session? =
        dataSource.connection.use { connection ->
            connection.executeCommand {
                sid.toInt().let { sid ->
                    val selectSessionCMD =
                        """
                        SELECT s.capacity, s.sid, s.date, s.owner, g.name, g.gid 
                        FROM session s
                            JOIN game g ON s.gid = g.gid
                        WHERE s.sid = ?;
                        """.trimIndent()
                    val stmt1 = connection.prepareStatement(selectSessionCMD)
                    stmt1.setInt(1, sid)
                    connection.makeSession(stmt1, limit, offset)
                }
            }
        }

    override fun readSessions(
        gameInfo: GameInfoParam?,
        date: LocalDate?,
        state: SessionState?,
        playerInfo: PlayerInfoParam?,
        offset: UInt,
        limit: UInt,
    ): Collection<SessionInfo>? =
        dataSource.connection.use { connection ->
            connection.executeCommand {
                val selectSessionCMD =
                    """
                    SELECT * FROM get_sessions_by(
                    ?, ?, ?, ?, 
                    ?, ?, ?, ?
                    );
                    """.trimIndent()
                val stmt2 = connection.prepareStatement(selectSessionCMD)
                var idx = 1
                val (gid, gameName) = gameInfo ?: Pair(null, null)
                val (pid, userName) = playerInfo ?: Pair(null, null)
                gid?.let { stmt2.setInt(idx++, it.toInt()) } ?: stmt2.setNull(idx++, java.sql.Types.INTEGER)
                date?.let { stmt2.setString(idx++, it.toString()) } ?: stmt2.setNull(idx++, java.sql.Types.VARCHAR)
                state?.let { stmt2.setString(idx++, it.toString()) } ?: stmt2.setNull(idx++, java.sql.Types.VARCHAR)
                pid?.let { stmt2.setInt(idx++, it.toInt()) } ?: stmt2.setNull(idx++, java.sql.Types.INTEGER)
                userName?.let { stmt2.setString(idx++, it) } ?: stmt2.setNull(idx++, java.sql.Types.VARCHAR)
                gameName?.let { stmt2.setString(idx++, it) } ?: stmt2.setNull(idx++, java.sql.Types.VARCHAR)
                limit.let { stmt2.setInt(idx++, it.toInt()) }
                offset.let { stmt2.setInt(idx, it.toInt()) }
                val collection = makeSessionInfo(stmt2)
                collection.ifEmpty { null }
            }
        }

    override fun updateCapacityOrDate(
        sid: UInt,
        capacity: UInt?,
        date: LocalDate?,
    ) {
        dataSource.connection.use { connection ->
            connection.executeCommand {
                val updateCMD =
                    "UPDATE SESSION\n" +
                        "SET\n" +
                        "    capacity = CASE WHEN ? IS NOT NULL THEN ? ELSE capacity END,\n" +
                        "    date = CASE WHEN ? IS NOT NULL THEN to_date(?, 'YYYY-MM-DD'::varchar) ELSE date END\n" +
                        "WHERE sid = ?;"
                val stmt1 = connection.prepareStatement(updateCMD)
                var idx = 1
                repeat(2) {
                    capacity?.let { stmt1.setInt(idx++, it.toInt()) } ?: stmt1.setNull(idx++, java.sql.Types.INTEGER)
                }
                repeat(2) {
                    date?.let { stmt1.setString(idx++, it.toString()) } ?: stmt1.setNull(idx++, java.sql.Types.VARCHAR)
                }
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

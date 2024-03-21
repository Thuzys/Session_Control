package pt.isel.ls.storage

import kotlinx.datetime.toLocalDateTime
import pt.isel.ls.domain.Email
import pt.isel.ls.domain.Player
import pt.isel.ls.domain.Session
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException
import java.util.UUID

/**
 * Makes a list of [Player] objects from a [PreparedStatement].
 * @param stmt The [PreparedStatement] to make the list from.
 * @return A list of [Player] objects.
 */
internal fun makePlayers(stmt: PreparedStatement): Collection<Player> {
    val rs = stmt.executeQuery()
    val players = mutableListOf<Player>()
    while (rs.next()) {
        players.add(
            Player(
                rs.getInt("pid").toUInt(),
                rs.getString("name"),
                Email(rs.getString("email")),
                UUID.fromString(
                    rs.getString("token"),
                ),
            ),
        )
    }
    return players
}

/**
 * Makes a list of [Session] objects from a [PreparedStatement].
 *
 * @param sessionStmt The [PreparedStatement] to make the list from.
 * @return A list of [Session] objects.
 */
internal fun Connection.makeSession(sessionStmt: PreparedStatement): Collection<Session> {
    val rs = sessionStmt.executeQuery()
    val sessions = mutableListOf<Session>()
    while (rs.next()) {
        val playerStmt =
            prepareStatement(
                "SELECT PLAYER.pid, name, email, token FROM PLAYER " +
                    "JOIN PLAYER_SESSION ON PLAYER.pid = PLAYER_SESSION.pid" +
                    " WHERE sid = ?;",
            )
        playerStmt.setInt(1, rs.getInt("sid"))
        sessions.add(
            Session(
                rs.getInt("sid").toUInt(),
                rs.getInt("capacity").toUInt(),
                rs.getInt("gid").toUInt(),
                rs.getString("date").toLocalDateTime(),
                makePlayers(playerStmt),
            ),
        )
    }
    return sessions
}

/**
 * Executes a command on a connection.
 * If an exception occurs, the connection will be rolled back and the auto-commit will be set to true.
 * Otherwise, the connection will be committed and the auto-commit will be set to true.
 * @param cmd The command to be executed.
 * @throws SQLException if an exception occurs.
 */
internal fun <T> Connection.executeCommand(cmd: Connection.() -> T): T {
    try {
        autoCommit = false
        val response = cmd()
        autoCommit = true
        return response
    } catch (e: SQLException) {
        rollback()
        autoCommit = true
        throw e
    }
}

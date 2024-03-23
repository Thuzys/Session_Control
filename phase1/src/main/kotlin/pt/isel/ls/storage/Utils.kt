package pt.isel.ls.storage

import kotlinx.datetime.toLocalDateTime
import pt.isel.ls.domain.Email
import pt.isel.ls.domain.Game
import pt.isel.ls.domain.Player
import pt.isel.ls.domain.Session
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.util.UUID

/**
 * Makes a list of [Game] objects from a [PreparedStatement].
 *
 * @param getGameStmt The [PreparedStatement] to make the list from.
 * @param getGenresStmt The [PreparedStatement] to make the list from.
 * @return A list of [Game] objects.
 */
fun getGamesFromDB(
    getGameStmt: PreparedStatement,
    getGenresStmt: PreparedStatement,
): MutableList<Game> =
    mutableListOf<Game>().apply {
        val rs = getGameStmt.executeQuery()

        while (rs.next()) {
            val gid = rs.getUInt("gid")
            add(
                Game(
                    gid = gid,
                    name = rs.getString("name"),
                    dev = rs.getString("developer"),
                    genres = processGenres(getGenresStmt, gid),
                ),
            )
        }
    }

/**
 * Gets a [Game] object from a [PreparedStatement].
 *
 * @param getGameStmt The [PreparedStatement] to get the [Game] object from.
 * @param getGenresStmt The [PreparedStatement] to get the [Game] object from.
 * @param gid The game id.
 * @return A [Game] object.
 */
fun getGameFromDB(
    getGameStmt: PreparedStatement,
    getGenresStmt: PreparedStatement,
    gid: UInt,
): Game? {
    val rs = getGameStmt.executeQuery()
    return when {
        rs.next() -> {
            Game(
                gid = rs.getUInt("gid"),
                name = rs.getString("name"),
                dev = rs.getString("developer"),
                genres = processGenres(getGenresStmt, gid),
            )
        }
        else -> null
    }
}

/**
 * Processes the genres of a game.
 *
 * @param getGenresStmt The [PreparedStatement] to get the genres from.
 * @param gid The game id.
 * @return A collection of genres.
 */
fun processGenres(
    getGenresStmt: PreparedStatement,
    gid: UInt,
): Collection<String> {
    val genres = mutableSetOf<String>()

    run {
        getGenresStmt.setUInt(1, gid)
        val genresRS = getGenresStmt.executeQuery()
        while (genresRS.next()) {
            genres.add(genresRS.getString("name"))
        }
    }

    return genres
}

/**
 * Adds a game to the database.
 *
 * @param newItem The game to be added.
 * @param addGameStmt The [PreparedStatement] to add the game.
 * @param relateGameToGenreStmt The [PreparedStatement] to relate the game to a genre.
 * @param addGenreStmt The [PreparedStatement] to add a genre.
 */

fun addGameToDB(
    newItem: Game,
    addGameStmt: PreparedStatement,
    relateGameToGenreStmt: PreparedStatement,
    addGenreStmt: PreparedStatement,
) {
    setGameName(addGameStmt, newItem.name)
    setGameDev(addGameStmt, newItem.dev)
    setGameGenres(newItem.genres, addGameStmt, relateGameToGenreStmt, addGenreStmt)
    addGameStmt.executeUpdate()
}

/**
 * Sets the game name in the prepared statement.
 *
 * @param addGameStmt The [PreparedStatement] to set the game name.
 * @param name The name to be set.
 */
private fun setGameName(
    addGameStmt: PreparedStatement,
    name: String,
) = addGameStmt.setString(1, name)

/**
 * Sets the game developer in the prepared statement.
 *
 * @param addGameStmt The [PreparedStatement] to set the game developer.
 * @param dev The developer to be set.
 */
private fun setGameDev(
    addGameStmt: PreparedStatement,
    dev: String,
) = addGameStmt.setString(2, dev)

/**
 * Sets the game genres in the database.
 *
 * @param genres The genres to be set.
 * @param addGameStmt The [PreparedStatement] to add the game.
 * @param relateGameToGenreStmt The [PreparedStatement] to relate the game to a genre.
 * @param addGenreStmt The [PreparedStatement] to add a genre.
 */
private fun setGameGenres(
    genres: Collection<String>,
    addGameStmt: PreparedStatement,
    relateGameToGenreStmt: PreparedStatement,
    addGenreStmt: PreparedStatement,
) {
    genres.forEach { genre ->
        addGenreToDB(addGenreStmt, genre)
        gameGenresRelation(relateGameToGenreStmt, getGameId(addGameStmt), genre)
    }
}

/**
 * Adds a genre to the database.
 *
 * @param addGenreStmt The [PreparedStatement] to add the genre.
 * @param genre The genre to be added.
 */
private fun addGenreToDB(
    addGenreStmt: PreparedStatement,
    genre: String,
) {
    addGenreStmt.setString(1, genre)
    addGenreStmt.executeUpdate()
}

/**
 * Relates a game to a genre.
 *
 * @param relateGameToGenreStmt The [PreparedStatement] to relate the game to a genre.
 * @param gid The game id.
 * @param genre The genre to be related.
 */
private fun gameGenresRelation(
    relateGameToGenreStmt: PreparedStatement,
    gid: UInt,
    genre: String,
) {
    relateGameToGenreStmt.setUInt(1, gid)
    relateGameToGenreStmt.setString(2, genre)
    relateGameToGenreStmt.executeUpdate()
}

/**
 * Gets the game id from the prepared statement.
 *
 * @param addGameStmt The [PreparedStatement] to get the game id from.
 * @return The game id.
 */
fun getGameId(addGameStmt: PreparedStatement): UInt {
    val rs = addGameStmt.generatedKeys
    check(rs.next()) { "Failed to create game." }
    return rs.getUInt("gid")
}

/**
 * Sets the unsigned integer value in the prepared statement.
 *
 * @param parameterIndex The index of the parameter to be set.
 * @param value The value to be set.
 */
fun PreparedStatement.setUInt(
    parameterIndex: Int,
    value: UInt,
) {
    setInt(parameterIndex, value.toInt())
}

/**
 * Gets the unsigned integer value from the result set.
 *
 * @param columnLabel The index of the column to get the value from.
 * @return The unsigned integer value.
 */
fun ResultSet.getUInt(columnLabel: String): UInt = getInt(columnLabel).toUInt()

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

package pt.isel.ls.storage

import kotlinx.datetime.toLocalDate
import org.eclipse.jetty.util.security.Password
import pt.isel.ls.domain.Email
import pt.isel.ls.domain.Game
import pt.isel.ls.domain.Player
import pt.isel.ls.domain.Session
import pt.isel.ls.domain.errors.StorageError
import pt.isel.ls.domain.info.GameInfo
import pt.isel.ls.domain.info.PlayerInfo
import pt.isel.ls.domain.info.SessionInfo
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.util.UUID

internal const val LIMIT = 10
internal const val OFFSET = 0

/**
 * Checks if a value used as param is valid.
 *
 * @param condition The condition to be checked.
 * @param lazyMessage The message to be displayed in case of an error.
 * @throws StorageError containing the message of the error.
 */
internal inline fun checkValidStorage(
    condition: Boolean,
    lazyMessage: () -> String,
) {
    if (!condition) {
        throw StorageError(lazyMessage())
    }
}

/**
 * Makes a list of [Game] objects from a [PreparedStatement].
 *
 * @param getGameStmt The [PreparedStatement] to make the list from.
 * @param getGenresStmt The [PreparedStatement] to make the list from.
 * @param limit The limit of games.
 * @param offset The offset of games.
 * @return A list of [Game] objects.
 */
internal fun getGamesFromDB(
    getGameStmt: PreparedStatement,
    getGenresStmt: PreparedStatement,
    areGenresInGameStmt: PreparedStatement,
    genres: Collection<String>?,
    limit: Int,
    offset: Int,
): Collection<Game> =
    mutableListOf<Game>().also {
        val rs = getGameStmt.executeQuery()
        var l = 0
        var o = 0
        while (rs.next() && l <= limit) {
            val gid = rs.getUInt("gid")

            if (genres == null || areGenresInGame(areGenresInGameStmt, gid, genres)) {
                if (o++ >= offset) {
                    it.add(
                        Game(
                            gid = gid,
                            name = rs.getString("name"),
                            dev = rs.getString("developer"),
                            genres = processGenres(getGenresStmt, gid),
                        ),
                    )
                    l++
                }
            }
        }
    }

/**
 * Checks if the genres are in the game.
 *
 * @param areGenresInGameStmt The [PreparedStatement] to check if the genres are in the game.
 * @param gid The game id.
 * @param genres The genres to be checked.
 * @return True if the genres are in the game, false otherwise.
 */
private fun areGenresInGame(
    areGenresInGameStmt: PreparedStatement,
    gid: UInt,
    genres: Collection<String>,
): Boolean {
    areGenresInGameStmt.setUInt(1, gid)

    return genres.any { genre ->
        areGenresInGameStmt.setString(2, genre)
        val rs = areGenresInGameStmt.executeQuery()
        rs.next()
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
internal fun getGameFromDB(
    getGameStmt: PreparedStatement,
    getGenresStmt: PreparedStatement,
    gid: UInt,
): Game? {
    val rs = getGameStmt.executeQuery()
    return if (rs.next()) {
        Game(
            gid = rs.getUInt("gid"),
            name = rs.getString("name"),
            dev = rs.getString("developer"),
            genres = processGenres(getGenresStmt, gid),
        )
    } else {
        null
    }
}

/**
 * Processes the genres of a game.
 *
 * @param getGenresStmt The [PreparedStatement] to get the genres from.
 * @param gid The game id.
 * @return A collection of genres.
 */
private fun processGenres(
    getGenresStmt: PreparedStatement,
    gid: UInt,
): Collection<String> =
    mutableSetOf<String>().apply {
        getGenresStmt.setUInt(1, gid)
        val genresRS = getGenresStmt.executeQuery()
        while (genresRS.next()) {
            add(genresRS.getString("name"))
        }
    }

/**
 * Adds a game to the database.
 *
 * @param newItem The game to be added.
 * @param addGameStmt The [PreparedStatement] to add the game.
 * @param relateGameToGenreStmt The [PreparedStatement] to relate the game to a genre.
 * @param addGenreStmt The [PreparedStatement] to add a genre.
 */

internal fun addGameToDB(
    newItem: Game,
    addGameStmt: PreparedStatement,
    relateGameToGenreStmt: PreparedStatement,
    addGenreStmt: PreparedStatement,
): UInt {
    addGameStmt.setString(1, newItem.name)
    addGameStmt.setString(2, newItem.dev)
    addGameStmt.executeUpdate()

    val gid = uInt(addGameStmt)
    setGameGenres(gid, newItem.genres, relateGameToGenreStmt, addGenreStmt)
    return gid
}

/**
 * Sets the game genres in the database.
 *
 * @param gid The game id.
 * @param genres The genres to be set.
 * @param relateGameToGenreStmt The [PreparedStatement] to relate the game to a genre.
 * @param addGenreStmt The [PreparedStatement] to add a genre.
 */
private fun setGameGenres(
    gid: UInt,
    genres: Collection<String>,
    relateGameToGenreStmt: PreparedStatement,
    addGenreStmt: PreparedStatement,
) {
    genres.forEach { genre ->
        addGenreToDB(addGenreStmt, genre)
        gameGenresRelation(relateGameToGenreStmt, gid, genre)
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
 * Gets the id from the prepared statement.
 *
 * @param stmt The [PreparedStatement] to get the id from.
 * @return The id.
 * @throws StorageError if no key is returned.
 */
internal fun uInt(stmt: PreparedStatement): UInt {
    val key = stmt.generatedKeys
    if (!key.next()) {
        throw StorageError("No key returned.")
    }
    val id = key.getInt(1)
    return id.toUInt()
}

/**
 * Sets the unsigned integer value in the prepared statement.
 *
 * @param parameterIndex The index of the parameter to be set.
 * @param value The value to be set.
 */
internal fun PreparedStatement.setUInt(
    parameterIndex: Int,
    value: UInt,
) = setInt(parameterIndex, value.toInt())

/**
 * Gets the unsigned integer value from the result set.
 *
 * @param columnLabel The index of the column to get the value from.
 * @return The unsigned integer value.
 */
internal fun ResultSet.getUInt(columnLabel: String): UInt = getInt(columnLabel).toUInt()

/**
 * Makes a list of [Player] objects from a [PreparedStatement].
 * @param stmt The [PreparedStatement] to make the list from.
 * @return A list of [Player] objects.
 */
internal fun makePlayers(stmt: PreparedStatement): Collection<Player> {
    val rs = stmt.executeQuery()
    val players = mutableListOf<Player>()
    while (rs.next()) {
        players.add(buildPlayer(rs))
    }
    return players
}

/**
 * Makes a [Player] object from a [PreparedStatement].
 *
 * @param stmt The [PreparedStatement] to make the [Player] object from.
 * @return A [Player] object.
 * @throws StorageError if an exception occurs.
 */
internal fun makePlayer(stmt: PreparedStatement): Player? {
    val rs = stmt.executeQuery()
    return if (rs.next()) {
        buildPlayer(rs)
    } else {
        null
    }
}

/**
 * Builds a [Player] object from a [ResultSet].
 *
 * @param rs The [ResultSet] to build the [Player] object from.
 * @return A [Player] object.
 */
private fun buildPlayer(rs: ResultSet): Player =
    Player(
        rs.getInt("pid").toUInt(),
        rs.getString("name"),
        rs.getString("username"),
        Email(rs.getString("email")),
        Password(rs.getString("password")),
        if (rs.getString("token") == null) {
            UUID.randomUUID()
        } else {
            UUID.fromString(rs.getString("token"))
        },
    )

/**
 * Make a [Session] object from a [PreparedStatement].
 * @param stmt The [PreparedStatement] to make the [Session] object from.
 * @param l The limit of playersInfo.
 * @param o The offset of playersInfo.
 * @return A [Session] object.
 * @throws StorageError if an exception occurs.
 */
internal fun Connection.makeSession(
    stmt: PreparedStatement,
    l: UInt,
    o: UInt,
): Session? {
    val rs = stmt.executeQuery()
    return if (rs.next()) {
        val ownerPreparedStatement =
            prepareStatement(
                """
                SELECT pid, userName FROM PLAYER
                WHERE pid = ?;
                """.trimIndent(),
            )
        ownerPreparedStatement.setInt(1, rs.getInt("owner"))
        val owner = makePlayersInfo(ownerPreparedStatement).firstOrNull() ?: throw StorageError("Owner not found")
        val playerStmt =
            prepareStatement(
                """
                SELECT PLAYER.pid, userName FROM PLAYER
                JOIN PLAYER_SESSION ON PLAYER.pid = PLAYER_SESSION.pid
                WHERE sid = ?
                LIMIT ? OFFSET ?;
                """.trimIndent(),
            )
        playerStmt.setInt(1, rs.getInt("sid"))
        playerStmt.setInt(2, l.toInt())
        playerStmt.setInt(3, o.toInt())
        Session(
            rs.getInt("sid").toUInt(),
            rs.getInt("capacity").toUInt(),
            GameInfo(rs.getInt("gid").toUInt(), rs.getString("name")),
            rs.getString("date").toLocalDate(),
            owner,
            makePlayersInfo(playerStmt),
        )
    } else {
        null
    }
}

internal fun makePlayersInfo(stmt: PreparedStatement): Collection<PlayerInfo> {
    val rs = stmt.executeQuery()
    val players = mutableListOf<PlayerInfo>()
    while (rs.next()) {
        players.add(
            PlayerInfo(
                rs.getInt("pid").toUInt(),
                rs.getString("userName"),
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
internal fun Connection.makeSessionInfo(sessionStmt: PreparedStatement): Collection<SessionInfo> {
    val rs = sessionStmt.executeQuery()
    val sessions = mutableListOf<SessionInfo>()
    while (rs.next()) {
        val ownerPreparedStatement =
            prepareStatement(
                "SELECT PLAYER.pid, name, userName, email, token FROM PLAYER " +
                    " WHERE pid = ?;",
            )
        ownerPreparedStatement.setInt(1, rs.getInt("owner"))
        val owner = makePlayersInfo(ownerPreparedStatement).first()
        sessions.add(
            SessionInfo(
                rs.getInt("sid").toUInt(),
                owner,
                GameInfo(rs.getInt("gid").toUInt(), rs.getString("game_name")),
                rs.getString("date").toLocalDate(),
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
 * @throws StorageError if an exception occurs.
 * @throws Exception if an unexpected exception occurs.
 * @return The response of the command.
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
        throw StorageError(handleSQLExceptionMessage(e))
    } catch (e: Exception) {
        rollback()
        autoCommit = true
        throw e
    }
}

/**
 * Make a user-friendly message from a SQLException.
 */
private fun handleSQLExceptionMessage(e: SQLException): String {
    return e.message?.substringAfter("ERROR:")?.dropLastWhile { it != '\n' } ?: "An error occurred."
}

package pt.isel.ls.storage

import pt.isel.ls.domain.Game
import pt.isel.ls.utils.toUInt
import java.sql.PreparedStatement


/**
 * Sets the values of the game to be inserted in the database.
 *
 * @param newItem The game to be inserted.
 * @param relateGameToGenreStmt The prepared statement to relate the game to the genre.
 * @param addGenreStmt The prepared statement to add a genre.
 */
fun PreparedStatement.setGameValues(
    newItem: Game,
    relateGameToGenreStmt: PreparedStatement,
    addGenreStmt: PreparedStatement
) {
    setGameName(newItem.name)
    setGameDev(newItem.dev)
    setGameGenres(newItem.genres, relateGameToGenreStmt, addGenreStmt)
    executeUpdate()
}

/**
 * Sets the name of the game to be inserted in the database.
 *
 * @param name The name of the game.
 */
private fun PreparedStatement.setGameName(name: String) =
    setString(1, name)

/**
 * Sets the developer of the game to be inserted in the database.
 *
 * @param dev The developer of the game.
 */
private fun PreparedStatement.setGameDev(dev: String) =
    setString(2, dev)

/**
 * Sets the genres of the game to be inserted in the database.
 *
 * @param genres The genres of the game.
 * @param relateGameToGenreStmt The prepared statement to relate the game to the genre.
 * @param addGenreStmt The prepared statement to add a genre.
 */
private fun PreparedStatement.setGameGenres(
    genres: Collection<String>,
    relateGameToGenreStmt: PreparedStatement,
    addGenreStmt: PreparedStatement
) {
    genres.forEach { genre ->
        addGenreStmt.setGenreValues(genre)
        relateGameToGenreStmt.setGameGenresRelation(getGameId(), genre)
    }
}

/**
 * Sets the values of the genre to be inserted in the database in the table "genre".
 *
 * @param genre The genre to be inserted.
 */
private fun PreparedStatement.setGenreValues(genre: String) {
    setString(1, genre)
    executeUpdate()
}

/**
 * Sets the relation between the game and the genre in the database in the table "genre_game".
 *
 * @param id The id of the game.
 * @param genre The genre of the game.
 */
private fun PreparedStatement.setGameGenresRelation(id: UInt, genre: String) {
    setUInt(1, id)
    setString(2, genre)
    executeUpdate()
}

/**
 * Sets the unsigned integer value in the prepared statement.
 *
 * @param parameterIndex The index of the parameter to be set.
 * @param value The value to be set.
 */
private fun PreparedStatement.setUInt(
    parameterIndex: Int,
    value: UInt
) {
    setInt(parameterIndex, value.toInt())
}

/**
 * Gets the id of the game that was inserted in the database.
 *
 * @return The id of the game.
 */
fun PreparedStatement.getGameId(): UInt {
    val rs = generatedKeys
    check(rs.next()) { "Failed to create game." }
    return rs.toUInt(1)
}
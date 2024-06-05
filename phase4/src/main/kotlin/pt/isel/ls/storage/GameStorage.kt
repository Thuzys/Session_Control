package pt.isel.ls.storage

import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.domain.Game
import pt.isel.ls.domain.info.Genres
import java.sql.Statement

/**
 * Class that represents the storage of the Game entity.
 */
class GameStorage(envVarName: String) : GameStorageInterface {
    private val dataSource = PGSimpleDataSource()

    init {
        dataSource.setUrl(System.getenv(envVarName))
    }

    override fun create(newItem: Game): UInt =
        dataSource.connection.use { connection ->
            connection.executeCommand {
                val addGameStmt =
                    connection.prepareStatement(
                        "INSERT INTO GAME(name, developer) VALUES (?, ?)",
                        Statement.RETURN_GENERATED_KEYS,
                    )

                val addGenreStmt =
                    connection.prepareStatement(
                        "INSERT INTO GENRE(name) VALUES (?) ON CONFLICT DO NOTHING",
                    )

                val relateGameToGenreStmt =
                    connection.prepareStatement(
                        "INSERT INTO GAME_GENRE(gid, genre) VALUES (?, ?)",
                    )

                addGameToDB(newItem, addGameStmt, relateGameToGenreStmt, addGenreStmt)
            }
        }

    override fun read(uInt: UInt): Game =
        dataSource.connection.use {
            it.executeCommand {
                val getGameStmt =
                    it.prepareStatement(
                        "SELECT gid, name, developer FROM GAME WHERE gid = ?",
                    )

                val getGenresStmt =
                    it.prepareStatement(
                        "SELECT name FROM GENRE JOIN GAME_GENRE ON GENRE.name = GAME_GENRE.genre WHERE GAME_GENRE.gid = ?",
                    )

                getGameStmt.setUInt(1, uInt)
                getGameFromDB(getGameStmt, getGenresStmt, uInt)
                    ?: throw NoSuchElementException("Game with id $uInt not found")
            }
        }

    override fun readBy(
        offset: UInt,
        limit: UInt,
        dev: String?,
        genres: Collection<String>?,
        name: String?,
    ): Collection<Game> =
        dataSource.connection.use {
            it.executeCommand {
                val baseQuery = StringBuilder("SELECT gid, name, developer FROM GAME WHERE 1=1")
                dev?.let { baseQuery.append(" AND compare_name(developer, ?)") }
                name?.let { baseQuery.append(" AND compare_name(name, ?)") }
                val getGameStr = baseQuery.toString()
                val getGamesStmt = it.prepareStatement(getGameStr)

                val getGenresStmt =
                    it.prepareStatement(
                        "SELECT name FROM GENRE JOIN GAME_GENRE ON GENRE.name = GAME_GENRE.genre WHERE GAME_GENRE.gid = ?",
                    )

                val areGenresInGameStmt =
                    it.prepareStatement(
                        "SELECT name FROM GENRE JOIN GAME_GENRE ON GENRE.name = GAME_GENRE.genre " +
                            "WHERE GAME_GENRE.gid = ? AND UPPER(GAME_GENRE.genre) = UPPER(?)",
                    )

                var paramIdx = 1
                dev?.let { d -> getGamesStmt.setString(paramIdx++, d) }
                name?.let { n -> getGamesStmt.setString(paramIdx++, n) }

                getGamesFromDB(getGamesStmt, getGenresStmt, areGenresInGameStmt, genres, limit.toInt(), offset.toInt())
            }
        }

    override fun readGenres(): Genres =
        dataSource.connection.use {
            it.executeCommand {
                val getGenresStmt = it.prepareStatement("SELECT name FROM GENRE")
                val genres = mutableListOf<String>()
                getGenresStmt.executeQuery().use { rs ->
                    while (rs.next()) {
                        genres.add(rs.getString("name"))
                    }
                }
                genres
            }
        }

    override fun update(
        uInt: UInt,
        newItem: Game,
    ) {
        TODO("Not needed")
    }

    override fun delete(uInt: UInt) {
        TODO("Not needed")
    }
}

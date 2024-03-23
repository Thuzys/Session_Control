package pt.isel.ls.storage

import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.domain.Game
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
        dataSource.connection.use {
            it.executeCommand {
                val addGameStmt =
                    it.prepareStatement(
                        "INSERT INTO game(name, developer) VALUES (?, ?)",
                        Statement.RETURN_GENERATED_KEYS,
                    )

                val addGenreStmt =
                    it.prepareStatement(
                        "INSERT INTO genre(name) VALUES (?) IF NOT EXISTS",
                    )

                val relateGameToGenreStmt =
                    it.prepareStatement(
                        "INSERT INTO game_genre(game_id, genre) VALUES (?, ?)",
                    )

                addGameToDB(newItem, addGameStmt, relateGameToGenreStmt, addGenreStmt)
                getGameId(addGameStmt)
            }
        }

    override fun read(uInt: UInt): Game =
        dataSource.connection.use {
            it.executeCommand {
                val getGameStmt =
                    it.prepareStatement(
                        "SELECT (gid, name, developer) from GAME WHERE gid = ?",
                    )

                val getGenresStmt =
                    it.prepareStatement(
                        "SELECT (name) FROM GENRES JOIN GAME_GENRES ON " +
                            "GENRE.name = GAME_GENRES.genre WHERE GAME_GENRES.gid = ?",
                    )

                getGameStmt.setUInt(1, uInt)
                getGameFromDB(getGameStmt, getGenresStmt, uInt)
                    ?: throw NoSuchElementException("Game with id $uInt not found")
            }
        }

    override fun readBy(
        offset: UInt,
        limit: UInt,
        dev: String,
        genres: Collection<String>,
    ): Collection<Game> =
        dataSource.connection.use {
            it.executeCommand {
                val getGamesStmt =
                    it.prepareStatement(
                        "SELECT (gid, name, developer) from GAME WHERE developer = ? LIMIT ? OFFSET ?",
                    )

                val getGenresStmt =
                    it.prepareStatement(
                        "SELECT (name) FROM GENRE JOIN GAME_GENRE ON " +
                            "GENRE.name = GAME_GENRE.genre WHERE GAME_GENRE.gid = ?",
                    )

                getGamesStmt.setString(1, dev)
                getGamesStmt.setUInt(2, limit)
                getGamesStmt.setUInt(3, offset)
                getGamesFromDB(getGamesStmt, getGenresStmt)
                    .ifEmpty { throw NoSuchElementException("Game with dev $dev not found") }
            }
        }

    override fun delete(uInt: UInt) {
        TODO("Not needed")
    }

    override fun update(
        uInt: UInt,
        newItem: Game,
    ) {
        TODO("Not needed")
    }
}

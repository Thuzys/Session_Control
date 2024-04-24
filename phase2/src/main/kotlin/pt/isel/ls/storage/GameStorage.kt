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
                        "INSERT INTO GAME(name, developer) VALUES (?, ?)",
                        Statement.RETURN_GENERATED_KEYS,
                    )

                val addGenreStmt =
                    it.prepareStatement(
                        "INSERT INTO GENRE(name) VALUES (?) ON CONFLICT DO NOTHING",
                    )

                val relateGameToGenreStmt =
                    it.prepareStatement(
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
    ): Collection<Game> =
        dataSource.connection.use {
            it.executeCommand {
                val getGameStr = buildGameGetterString(dev)
                val getGamesStmt = it.prepareStatement("$getGameStr LIMIT ? OFFSET ?")

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
                getGamesStmt.setUInt(paramIdx++, limit)
                getGamesStmt.setUInt(paramIdx, offset)

                getGamesFromDB(getGamesStmt, getGenresStmt, areGenresInGameStmt, genres)
                    .ifEmpty {
                        val errorMsg =
                            when {
                                dev != null && genres == null -> "dev $dev"
                                genres != null && dev == null -> "genres $genres"
                                else -> "dev $dev and genres $genres"
                            }
                        throw NoSuchElementException("Game with $errorMsg not found")
                    }
            }
        }

    override fun readByPlayer(
        pid: UInt,
        offset: UInt,
        limit: UInt,
    ): Collection<Game> =
        dataSource.connection.use {
            it.executeCommand {
                val getGamesStmt =
                    it.prepareStatement(
                        "SELECT GAME.gid, name, developer, pid" +
                            " FROM GAME JOIN SESSION ON GAME.gid = SESSION.gid" +
                            " JOIN player_session ON SESSION.sid = PLAYER_SESSION.sid" +
                            " WHERE pid = ?" +
                            " LIMIT ? OFFSET ?",
                    )

                val getGenresStmt =
                    it.prepareStatement(
                        "SELECT name FROM GENRE JOIN GAME_GENRE ON GENRE.name = GAME_GENRE.genre WHERE GAME_GENRE.gid = ?",
                    )

                getGamesStmt.setUInt(1, pid)
                getGamesStmt.setUInt(2, limit)
                getGamesStmt.setUInt(3, offset)

                getGamesFromDB(getGamesStmt, getGenresStmt)
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

package pt.isel.ls.storage.postgres

import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.domain.Game
import pt.isel.ls.storage.Storage
import pt.isel.ls.storage.getGameId
import pt.isel.ls.storage.setGameValues
import pt.isel.ls.utils.executeCommand
import java.sql.Statement


class GameStorage(envVarName: String) : Storage<Game> {

    private val dataSource = PGSimpleDataSource()

    init {
        dataSource.setUrl(System.getenv(envVarName))
    }

    override fun create(newItem: Game): UInt =
        dataSource.connection.use {
            it.executeCommand {
                val addGameStmt = it.prepareStatement(
                    "INSERT INTO game(name, dev) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
                )

                val addGenreStmt = it.prepareStatement(
                    "INSERT INTO genre(name) VALUES (?) IF NOT EXISTS"
                )

                val relateGameToGenreStmt = it.prepareStatement(
                    "INSERT INTO game_genre(game_id, genre) VALUES (?, ?)"
                )

                addGameStmt.run {
                    setGameValues(newItem, relateGameToGenreStmt, addGenreStmt)
                    getGameId()
                }
            }
        }


    override fun read(
        uInt: UInt?,
        offset: UInt,
        limit: UInt,
    ): Collection<Game>? {
        TODO()
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

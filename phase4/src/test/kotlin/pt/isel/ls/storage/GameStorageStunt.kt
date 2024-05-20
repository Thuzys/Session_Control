package pt.isel.ls.storage

import pt.isel.ls.domain.Game
import pt.isel.ls.domain.info.Genres

class GameStorageStunt : GameStorageInterface {
    private val game1 = Game(1u, "test", "dev", setOf("genre"))
    private val game2 = Game(2u, "test2", "dev2", setOf("genre2"))
    private val game3 = Game(3u, "test3", "dev", setOf("genre"))
    private val games =
        HashMap<UInt, Game>().apply {
            put(1u, game1)
            put(2u, game2)
            put(3u, game3)
        }

    private var nextId: UInt = 4u

    override fun create(newItem: Game): UInt {
        val id = nextId++
        games[id] = newItem.copy(gid = id)
        return id
    }

    override fun read(uInt: UInt): Game =
        games.values.toList().find { it.gid == uInt } ?: throw NoSuchElementException("Game with id $uInt not found")

    override fun readBy(
        offset: UInt,
        limit: UInt,
        dev: String?,
        genres: Collection<String>?,
        name: String?,
    ): Collection<Game> {
        val gamesToSearch = games.values.toList()
        val rangeToSearch = gamesToSearch.drop(offset.toInt()).take(limit.toInt())

        val filtered =
            rangeToSearch.filter { game ->
                (dev == null || game.dev == dev) && (genres == null || game.genres.containsAll(genres)) &&
                    (name == null || game.name == name)
            }

        return filtered.ifEmpty { throw NoSuchElementException("No games found") }
    }

    override fun update(
        uInt: UInt,
        newItem: Game,
    ) {
        TODO("Not needed for tests")
    }

    override fun delete(uInt: UInt) {
        TODO("Not needed for tests")
    }

    override fun readGenres() =
        games
            .values
            .flatMap { it.genres }
            .distinct()
}

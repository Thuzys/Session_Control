package pt.isel.ls.storage

import pt.isel.ls.domain.Game

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
        pid: UInt?
    ): Collection<Game> {
        val gamesToSearch = games.values.toList()
        val rangeToSearch = gamesToSearch.drop(offset.toInt()).take(limit.toInt())
        if (pid == 1u) return listOf(game1)
        val filtered = rangeToSearch.filter { it.dev == dev && it.genres.containsAll(genres ?: emptyList()) }

        return filtered.ifEmpty { throw NoSuchElementException("Game with dev $dev and genres $genres not found") }
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
}

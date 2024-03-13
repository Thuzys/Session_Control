package pt.isel.ls.storage

import pt.isel.ls.domain.Game

class GameStorageStunt : Storage<Game> {
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
        games[id] = newItem.copy(uuid = id)
        return id
    }

    override fun read(
        uInt: UInt?,
        offset: UInt,
        limit: UInt,
    ): Collection<Game>? {
        val gamesToSearch = games.values.toList()
        val rangeToSearch = gamesToSearch.drop(offset.toInt()).take(limit.toInt())

        return if (uInt != null) {
            games[uInt]?.let { listOf(it) }
        } else {
            rangeToSearch
        }
    }

    override fun delete(uInt: UInt) {
        TODO("Not needed for tests")
    }

    override fun update(
        uInt: UInt,
        newItem: Game,
    ) {
        TODO("Not needed for tests")
    }
}

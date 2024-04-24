package pt.isel.ls.storage

import kotlinx.datetime.LocalDateTime
import pt.isel.ls.domain.Email
import pt.isel.ls.domain.Game
import pt.isel.ls.domain.Player
import pt.isel.ls.domain.Session

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
    private val sessionsStunt =
        HashMap<UInt, Session>().apply {
            put(
                1u,
                Session(
                    1u,
                    1u,
                    1u,
                    LocalDateTime(2021, 1, 1, 1, 1, 1, 1),
                    listOf(
                        Player(1u, "test", Email("test@gmail.com")),
                        Player(2u, "test2", Email("test2@gmail.com")),
                    ),
                ),
            )
            put(
                2u,
                Session(
                    2u,
                    2u,
                    2u,
                    LocalDateTime(2021, 1, 1, 1, 1, 1, 1),
                    listOf(
                        Player(1u, "test", Email("test@gmail.com")),
                        Player(3u, "test3", Email("test3@gmail.com")),
                    ),
                ),
            )
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
    ): Collection<Game> {
        val gamesToSearch = games.values.toList()
        val rangeToSearch = gamesToSearch.drop(offset.toInt()).take(limit.toInt())

        val filtered = rangeToSearch.filter { it.dev == dev && it.genres.containsAll(genres ?: emptyList()) }

        return filtered.ifEmpty { throw NoSuchElementException("Game with dev $dev and genres $genres not found") }
    }

    override fun readByPlayer(
        pid: UInt,
        offset: UInt,
        limit: UInt,
    ): Collection<Game> {
        val gamesToSearch = games.values.toList()
        val rangeToSearch = gamesToSearch.drop(offset.toInt()).take(limit.toInt())

        val filtered =
            rangeToSearch
                .filter { game ->
                    game.gid in
                        sessionsStunt.values
                            .filter { session ->
                                session.players.any { player ->
                                    player.pid == pid
                                }
                            }.map { it.gid }
                }

        return filtered.ifEmpty { throw NoSuchElementException("Game with player $pid not found") }
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

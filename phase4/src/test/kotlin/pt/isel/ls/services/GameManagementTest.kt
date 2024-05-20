package pt.isel.ls.services

import pt.isel.ls.domain.errors.ServicesError
import pt.isel.ls.storage.GameStorageStunt
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class GameManagementTest {
    private fun executeGameManagementTest(action: (gameServices: GameServices) -> Unit) =
        GameManagement(GameStorageStunt())
            .run(action)

    @Test
    fun `create a new Game - Success`() {
        executeGameManagementTest { handler ->
            // ARRANGE
            val expectedGid1 = 4u
            val expectedGid2 = 5u

            // ACT
            val game1 = handler.createGame("test", "dev", setOf("genre"))
            val game2 = handler.createGame("test", "dev", setOf("genre"))

            // ASSERT
            assertEquals(expectedGid1, game1)
            assertEquals(expectedGid2, game2)
        }
    }

    @Test
    fun `error creating a game - Failure due to invalid parameter`() {
        executeGameManagementTest { handler ->
            // ARRANGE
            val gameName = "test"
            val gameDev = "dev"
            val invalidGenres = setOf<String>()

            // ACT & ASSERT
            assertFailsWith<ServicesError> {
                handler.createGame(gameName, gameDev, invalidGenres)
            }
        }
    }

    @Test
    fun `get details of a game - Success`() {
        executeGameManagementTest { handler ->
            // ARRANGE
            val expectedName = "test"
            val expectedDev = "dev"
            val expectedGenre = "genre"

            // ACT
            val game = handler.getGameDetails(1u)

            // ASSERT
            assertEquals(expectedName, game.name)
            assertEquals(expectedDev, game.dev)
            assertTrue { expectedGenre in game.genres }
        }
    }

    @Test
    fun `error getting details of a game - Failure due to non-existing game id`() {
        executeGameManagementTest { handler ->
            // ARRANGE
            val invalidGid = 60u

            // ACT & ASSERT
            assertFailsWith<ServicesError> { handler.getGameDetails(invalidGid) }
        }
    }

    @Test
    fun `get Game by developer and genres - Success`() {
        executeGameManagementTest { handler ->
            // ARRANGE
            val expectedSize = 2

            // ACT
            val games = handler.getGames("dev", setOf("genre"), null)

            // ASSERT
            assertEquals(expectedSize, games.size)
        }
    }

    @Test
    fun `get Game by developer and genres with no results - Failure due to non existing combination of developer and genres`() {
        executeGameManagementTest { handler ->
            // ARRANGE
            val gameDev = "dev"
            val gameGenres = setOf("genre2")

            // ACT & ASSERT
            assertFailsWith<ServicesError> { handler.getGames(gameDev, gameGenres, null) }
        }
    }

    @Test
    fun `get Game by developer - Success`() {
        executeGameManagementTest { handler ->
            // ARRANGE
            val expectedSize = 2

            // ACT
            val games = handler.getGames("dev", null, null)

            // ASSERT
            assertEquals(expectedSize, games.size)
        }
    }

    @Test
    fun `get Game by developer with no results - Failure due to non existing developer`() {
        executeGameManagementTest { handler ->
            // ARRANGE
            val gameDev = "dev3"

            // ACT & ASSERT
            assertFailsWith<ServicesError> { handler.getGames(gameDev, null, null) }
        }
    }

    @Test
    fun `get Game by genres - Success`() {
        executeGameManagementTest { handler ->
            // ARRANGE
            val expectedSize = 2

            // ACT
            val games = handler.getGames(null, setOf("genre"), null)

            // ASSERT
            assertEquals(expectedSize, games.size)
        }
    }

    @Test
    fun `get Game by genres with no results - Failure due to non existing genre`() {
        executeGameManagementTest { handler ->
            // ARRANGE
            val gameGenres = setOf("genre45")

            // ACT & ASSERT
            assertFailsWith<ServicesError> { handler.getGames(null, gameGenres, null) }
        }
    }

    @Test
    fun `get Game by name - Success`() {
        executeGameManagementTest { handler ->
            // ARRANGE
            val expectedSize = 1

            // ACT
            val games = handler.getGames(null, null, "test")

            // ASSERT
            assertEquals(expectedSize, games.size)
        }
    }

    @Test
    fun `get Game by name with no results - Failure due to non existing name`() {
        executeGameManagementTest { handler ->
            // ARRANGE
            val gameName = "test70"

            // ACT & ASSERT
            assertFailsWith<ServicesError> { handler.getGames(null, null, gameName) }
        }
    }

    @Test
    fun `get Game by developer, genres and name - Success`() {
        executeGameManagementTest { handler ->
            // ARRANGE
            val expectedSize = 1

            // ACT
            val games = handler.getGames("dev", setOf("genre"), "test")

            // ASSERT
            assertEquals(expectedSize, games.size)
        }
    }

    @Test
    fun `get Game by developer, genres and name with no results - Failure due to non existing combination of developer, genres and name`() {
        executeGameManagementTest { handler ->
            // ARRANGE
            val gameDev = "dev"
            val gameGenres = setOf("genre2")
            val gameName = "test3"

            // ACT & ASSERT
            assertFailsWith<ServicesError> { handler.getGames(gameDev, gameGenres, gameName) }
        }
    }

    @Test
    fun `read all genres successfully`() {
        executeGameManagementTest { handler ->
            // ARRANGE
            val expectedGenres = setOf("genre", "genre2")

            // ACT
            val genres = handler.getAllGenres()

            // ASSERT
            assertContentEquals(expectedGenres, genres)
        }
    }
}

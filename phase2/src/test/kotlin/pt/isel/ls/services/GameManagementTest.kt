package pt.isel.ls.services

import pt.isel.ls.domain.errors.ServicesError
import pt.isel.ls.storage.GameStorageStunt
import kotlin.test.Test
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
            val games = handler.getGameByDevAndGenres("dev", setOf("genre"))

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
            assertFailsWith<ServicesError> { handler.getGameByDevAndGenres(gameDev, gameGenres) }
        }
    }

    @Test
    fun `get games by pid`() {
        executeGameManagementTest { handler ->
            // ARRANGE
            val expectedSize = 1

            // ACT
            val games = handler.getGameByDevAndGenres(pid = 1u)

            // ASSERT
            assertEquals(expectedSize, games.size)
        }
    }

    @Test
    fun `get games with nonexistent pid`() {
        executeGameManagementTest { handler ->
            // ARRANGE
            val pid = 2u

            // ACT & ASSERT
            assertFailsWith<ServicesError> { handler.getGameByDevAndGenres(pid = pid) }
        }
    }
}

package pt.isel.ls.services

import pt.isel.ls.domain.errors.ServicesError
import pt.isel.ls.storage.PlayerStorageStunt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PlayerManagementTest {
    private fun actionOfPlayerManagementTest(code: (player: PlayerServices) -> Unit) =
        // arrangement
        PlayerManagement(PlayerStorageStunt())
            .let(code)

    @Test
    fun `error creating a new player successfully due email already exists`() =
        actionOfPlayerManagementTest { playerManagement: PlayerServices ->
            assertFailsWith<ServicesError> { playerManagement.createPlayer("test2", "test@mail.com") }
        }

    @Test
    fun `error creating a new player successfully due userName already exists`() =
        actionOfPlayerManagementTest { playerManagement: PlayerServices ->
            assertFailsWith<ServicesError> { playerManagement.createPlayer("test1", "newEmail@mail.com") }
        }

    @Test
    fun `creating a new player successfully`() =
        actionOfPlayerManagementTest { playerManagement: PlayerServices ->
            assertEquals(3u, playerManagement.createPlayer("test3", "newEmail@mail.com").first)
        }

    @Test
    fun `getting details of a player successfully`() =
        actionOfPlayerManagementTest { playerManagement: PlayerServices ->
            assertEquals("test1", playerManagement.getPlayerDetails(1u).name)
        }

    @Test
    fun `message generated by getting details of a non-existent player error`() =
        actionOfPlayerManagementTest { playerManagement: PlayerServices ->
            assertEquals(
                expected = "Unable to get the details of a Player due: Player not found.",
                actual = runCatching { playerManagement.getPlayerDetails(3u) }.exceptionOrNull()?.message,
            )
        }

    @Test
    fun `type of error generated by getting the details of a non-existing player error`() =
        actionOfPlayerManagementTest { playerManagement: PlayerServices ->
            assertFailsWith<ServicesError> { playerManagement.getPlayerDetails(3u) }
        }

    @Test
    fun `error creating a new player due an invalid email`() =
        actionOfPlayerManagementTest { playerManagement: PlayerServices ->
            assertFailsWith<ServicesError> { playerManagement.createPlayer("test1", "non-valid-email") }
        }

    @Test
    fun `message generated by creating an invalid player due an invalid email`() =
        actionOfPlayerManagementTest { playerManagement: PlayerServices ->
            assertEquals(
                expected = "Unable to create a new Player due: Invalid email pattern.",
                actual =
                    runCatching {
                        playerManagement.createPlayer("test1", "non-valid-email").first
                    }.exceptionOrNull()?.message,
            )
        }

    @Test
    fun `type of error creating a new player due an invalid name`() =
        actionOfPlayerManagementTest { playerManagement: PlayerServices ->
            assertFailsWith<ServicesError> { playerManagement.createPlayer("   ", "valid@email.com") }
        }

    @Test
    fun `message generated by creating an invalid player due an invalid name`() =
        actionOfPlayerManagementTest { playerManagement: PlayerServices ->
            assertEquals(
                expected = "Unable to create a new Player due: Name must not be blank.",
                actual =
                    runCatching {
                        playerManagement.createPlayer("   ", "valid@email.com")
                    }.exceptionOrNull()?.message,
            )
        }

    @Test
    fun `isValidToken call with a valid token`() =
        actionOfPlayerManagementTest { playerManagement: PlayerServices ->
            assertTrue { playerManagement.isValidToken("validToken") }
        }

    @Test
    fun `isValidToken call with an invalid token`() =
        actionOfPlayerManagementTest { playerManagement: PlayerServices ->
            val invalidToken = ""
            assertFalse { playerManagement.isValidToken(invalidToken) }
        }
}

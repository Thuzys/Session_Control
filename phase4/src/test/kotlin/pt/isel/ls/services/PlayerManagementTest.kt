package pt.isel.ls.services

import pt.isel.ls.domain.errors.ServicesError
import pt.isel.ls.storage.PlayerStorageStunt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PlayerManagementTest {
    private val email = "test@mail.com"
    private val alreadyExistName = "test1"
    private val name = "test2"
    private val password = "password"
    private val pid = 1u

    private fun actionOfPlayerManagementTest(code: (player: PlayerServices) -> Unit) =
        // arrangement
        PlayerManagement(PlayerStorageStunt(pid))
            .let(code)

    @Test
    fun `error creating a new player successfully due email already exists`() =
        actionOfPlayerManagementTest { playerManagement: PlayerServices ->
            val nameParam = name to null
            val emailPassParam = email to password
            assertFailsWith<ServicesError> { playerManagement.createPlayer(nameParam, emailPassParam) }
        }

    @Test
    fun `error creating a new player successfully due userName already exists`() =
        actionOfPlayerManagementTest { playerManagement: PlayerServices ->
            val nameParam = alreadyExistName to null
            val emailPassParam = email + "m" to password
            assertFailsWith<ServicesError> { playerManagement.createPlayer(nameParam, emailPassParam) }
        }

    @Test
    fun `creating a new player successfully`() =
        actionOfPlayerManagementTest { playerManagement: PlayerServices ->
            val nameParam = name to "newUserName"
            val emailPassParam = email + "m" to password
            assertEquals(3u, playerManagement.createPlayer(nameParam, emailPassParam).pid)
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
            val nameParam = name to null
            val emailPassParam = "non-valid-email" to password
            assertFailsWith<IllegalArgumentException> { playerManagement.createPlayer(nameParam, emailPassParam) }
        }

    @Test
    fun `message generated by creating an invalid player due an invalid email`() =
        actionOfPlayerManagementTest { playerManagement: PlayerServices ->
            assertEquals(
                expected = "Invalid email pattern.",
                actual =
                    runCatching {
                        val nameParam = name to null
                        val emailPassParam = "non-valid-email" to password
                        playerManagement.createPlayer(nameParam, emailPassParam).pid
                    }.exceptionOrNull()?.message,
            )
        }

    @Test
    fun `type of error creating a new player due an invalid name`() =
        actionOfPlayerManagementTest { playerManagement: PlayerServices ->
            val nameParam = "   " to name
            val emailPassParam = email to password
            assertFailsWith<ServicesError> { playerManagement.createPlayer(nameParam, emailPassParam) }
        }

    @Test
    fun `message generated by creating an invalid player due an invalid name`() =
        actionOfPlayerManagementTest { playerManagement: PlayerServices ->
            assertEquals(
                expected = "Name must not be blank.",
                actual =
                    runCatching {
                        val nameParam = "   " to name
                        val emailPassParam = email to password
                        playerManagement.createPlayer(nameParam, emailPassParam)
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

    @Test
    fun `getting details of a player by userName successfully`() =
        actionOfPlayerManagementTest { playerManagement: PlayerServices ->
            assertEquals("test1", playerManagement.getPlayerDetailsBy("test1").name)
        }

    @Test
    fun `login successfully`() =
        actionOfPlayerManagementTest { playerManagement: PlayerServices ->
            val playerAuthentication = playerManagement.login("test1", "password")
            assertEquals(1u, playerAuthentication.pid)
        }

    @Test
    fun `logout successfully`() =
        actionOfPlayerManagementTest { playerManagement: PlayerServices ->
            playerManagement.logout(pid)
        }
}

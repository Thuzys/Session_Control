package pt.isel.ls.services

import pt.isel.ls.domain.errors.ServicesError
import pt.isel.ls.storage.PlayerDataMem
import pt.isel.ls.storage.PlayerStorageStunt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PlayerManagementTest {
    private fun makePlayerTest(code: (player: PlayerServices) -> Unit) {
        PlayerManagement(PlayerDataMem(PlayerStorageStunt())).run { code(this) }
    }

    @Test
    fun `create a new player`() =
        makePlayerTest {
            assertEquals(3u, it.createPlayer("test2", "test@mail.com").first)
        }

    @Test
    fun `get details of a player`() =
        makePlayerTest {
            assertEquals("test1", it.getPlayerDetails(1u).name)
        }

    @Test
    fun `get details of a non-existent player`() =
        makePlayerTest {
            assertEquals(
                expected = "Unable to get the details of a Player due: Player not found.",
                actual = runCatching { it.getPlayerDetails(3u) }.exceptionOrNull()?.message,
            )
        }

    @Test
    fun `type of error getting the details of a non-existing player`() =
        makePlayerTest {
            assertFailsWith<ServicesError> { it.getPlayerDetails(3u) }
        }

    @Test
    fun `error creating a new player`() =
        makePlayerTest {
            assertFailsWith<ServicesError> { it.createPlayer("test1", "non-valid-email") }
        }

    @Test
    fun `creating an invalid player`() =
        makePlayerTest {
            assertEquals(
                expected = "Unable to create a new Player due: Invalid email pattern.",
                actual =
                    runCatching {
                        it.createPlayer("test1", "non-valid-email").first
                    }.exceptionOrNull()?.message,
            )
        }
}

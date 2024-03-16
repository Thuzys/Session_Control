package pt.isel.ls.storage

import pt.isel.ls.domain.Email
import pt.isel.ls.domain.associatedTo
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PlayerDataMemTest {
    private fun makePlayerDataMemTest(code: (playerDataMem: PlayerDataInterface) -> Unit) {
        PlayerDataMem(PlayerStorageStunt()).run { code(this) }
    }

    @Test
    fun `create a player`() =
        makePlayerDataMemTest {
            assertEquals(3u, it.storePlayer("test2" associatedTo Email("test@mail.com")))
        }

    @Test
    fun `get details of a player`() =
        makePlayerDataMemTest {
            assertEquals("test1", it.readPlayer(1u).name)
        }

    @Test
    fun `get details of a non-existent player`() =
        makePlayerDataMemTest {
            assertEquals(
                expected = "Player not found.",
                actual = runCatching { it.readPlayer(3u) }.exceptionOrNull()?.message,
            )
        }

    @Test
    fun `type of error getting the details of a non-existing player`() =
        makePlayerDataMemTest {
            assertFailsWith<NoSuchElementException> { it.readPlayer(3u) }
        }

    @Test
    fun `error creating a new player`() =
        makePlayerDataMemTest {
            assertFailsWith<IllegalArgumentException> {
                it.storePlayer("   " associatedTo Email("valid@mail.com"))
            }
        }
}

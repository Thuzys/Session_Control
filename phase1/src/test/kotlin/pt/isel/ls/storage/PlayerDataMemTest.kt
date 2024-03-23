package pt.isel.ls.storage

import pt.isel.ls.domain.Email
import pt.isel.ls.domain.associatedTo
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PlayerDataMemTest {
    private fun actionOfPlayerDataMemArrangement(code: (playerDataMem: PlayerDataInterface) -> Unit) =
        // arrangement
        PlayerDataMem(PlayerStorageStuntDummy())
            .let(code)

    @Test
    fun `test of a successful player creation`() =
        actionOfPlayerDataMemArrangement { dataMem: PlayerDataInterface ->
            assertEquals(3u, dataMem.storePlayer("test2" associatedTo Email("test@mail.com")))
        }

    @Test
    fun `getting details of a existing player`() =
        actionOfPlayerDataMemArrangement { dataMem: PlayerDataInterface ->
            assertEquals("test1", dataMem.readPlayer(1u).name)
        }

    @Test
    fun `message of exception due a readPlayer call of a non-existent player`() =
        actionOfPlayerDataMemArrangement { dataMem: PlayerDataInterface ->
            assertEquals(
                expected = "Player not found.",
                actual = runCatching { dataMem.readPlayer(3u) }.exceptionOrNull()?.message,
            )
        }

    @Test
    fun `type of error getting the details of a non-existing player`() =
        actionOfPlayerDataMemArrangement { dataMem: PlayerDataInterface ->
            assertFailsWith<NoSuchElementException> { dataMem.readPlayer(3u) }
        }

    @Test
    fun `exception during a creation of a player with an invalid name`() =
        actionOfPlayerDataMemArrangement { dataMem: PlayerDataInterface ->
            assertFailsWith<IllegalArgumentException> {
                dataMem.storePlayer("   " associatedTo Email("valid@mail.com"))
            }
        }
}

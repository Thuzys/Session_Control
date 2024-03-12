package pt.isel.ls.domain

import kotlin.test.Test
import kotlin.test.assertFailsWith

class GameTest {
    private val validId: UInt = 1u
    private val validName = "FIFA 14"
    private val validDev = "EA SPORTS"
    private val validGenres: Collection<String> = setOf("Sports")

    @Test
    fun `empty name of Game class implementation`() {
        val emptyName = ""
        assertFailsWith<IllegalArgumentException> { Game(validId, emptyName, validDev, validGenres) }
    }

    @Test
    fun `empty dev of Game class implementation`() {
        val emptyDev = ""
        assertFailsWith<IllegalArgumentException> { Game(validId, validName, emptyDev, validGenres) }
    }

    @Test
    fun `empty set of genres of Game class implementation`() {
        val emptyGenres: Collection<String> = setOf()
        assertFailsWith<IllegalArgumentException> { Game(validId, validName, validDev, emptyGenres) }
    }
}

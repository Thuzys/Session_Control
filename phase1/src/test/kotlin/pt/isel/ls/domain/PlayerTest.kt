package pt.isel.ls.domain

import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class PlayerTest {
    private val goodTestName = "player1"
    private val goodEmailTest = "goodEmail@test.com"
    private val badTestName = ""
    private val tokenTest = UUID.randomUUID()
    private val uId: UInt = 1u

    @Test
    fun `invalid name test`() {
        assertFailsWith<IllegalArgumentException> { Player(uId, badTestName, goodEmailTest) }
    }

    @Test
    fun `good instantiation of an existent Player`() {
        assertTrue { Player(uId, goodTestName, goodEmailTest, tokenTest).token == tokenTest }
    }

    @Test
    fun `good instantiation of a new Player`() {
        assertTrue { Player(uId, goodTestName, goodEmailTest).token != tokenTest }
    }
}

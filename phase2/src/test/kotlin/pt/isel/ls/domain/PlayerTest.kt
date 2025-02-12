package pt.isel.ls.domain

import kotlin.test.Test
import kotlin.test.assertFailsWith

class PlayerTest {
    private val goodTestName = "player1"
    private val goodEmailTest = Email("goodEmail@test.com")
    private val badTestName = ""
    private val uId: UInt = 1u

    @Test
    fun `exception generated by an invalid name test`() {
        assertFailsWith<IllegalArgumentException> { Player(uId, badTestName, goodEmailTest) }
    }

    @Test
    fun `create a new player with associatedTo function test`() {
        goodTestName associatedTo goodEmailTest // should not throw any exception
    }
}

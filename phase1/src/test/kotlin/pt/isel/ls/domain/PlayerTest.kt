package pt.isel.ls.domain

import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class PlayerTest {
    private val goodTestName = "player1"
    private val emailWithoutAt = "badEmail.com"
    private val emailWithoutDotCom = "badEmail@test"
    private val goodEmailTest = "goodEmail@test.com"
    private val badTestName = ""
    private val tokenTest = UUID.randomUUID()
    private val uId: UInt = 1u

    @Test
    fun `invalid email @ missing`() {
        assertFailsWith<IllegalArgumentException> { Player(uId, goodTestName, emailWithoutAt) }
    }

    @Test
    fun `invalid email dot_com missing`() {
        assertFailsWith<IllegalArgumentException> { Player(uId, goodTestName, emailWithoutDotCom) }
    }

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

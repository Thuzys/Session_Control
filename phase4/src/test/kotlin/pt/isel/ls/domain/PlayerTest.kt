package pt.isel.ls.domain

import org.eclipse.jetty.util.security.Password
import pt.isel.ls.domain.info.CreatePlayerEmailPasswordParam
import pt.isel.ls.domain.info.CreatePlayerNameParam
import kotlin.test.Test
import kotlin.test.assertFailsWith

class PlayerTest {
    private val goodTestName = "player1"
    private val goodEmailTest = Email("goodEmail@test.com")
    private val goodEmailNameTest = "goodEmail@test.com"
    private val badTestName = ""
    private val uId: UInt = 1u
    private val passwordTest = Password("password")
    private val passwordNameTest = "password"

    @Test
    fun `exception generated by an invalid name test`() {
        assertFailsWith<IllegalArgumentException> {
            Player(uId, badTestName, email = goodEmailTest, username = goodTestName, password = passwordTest)
        }
    }

    @Test
    fun `exception generated by an invalid userName test`() {
        assertFailsWith<IllegalArgumentException> {
            Player(uId, goodTestName, username = badTestName, email = goodEmailTest, password = passwordTest)
        }
    }

    @Test
    fun `create a new player with associatedTo function test`() {
        val createPlayerName = CreatePlayerNameParam(goodTestName, null)
        val createPlayerEmailPassword = CreatePlayerEmailPasswordParam(goodEmailNameTest, passwordNameTest)
        createPlayerName associateWith createPlayerEmailPassword // should not throw an exception
    }
}

package pt.isel.ls.domain

import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

class SessionStateTest {
    @Test
    fun `test OPEN state`() {
        val state = SessionState.OPEN
        assertEquals("OPEN", state.name)
    }

    @Test
    fun `test CLOSE state`() {
        val state = SessionState.CLOSE
        assertEquals("CLOSE", state.name)
    }
}

package pt.isel.ls.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class SessionStateTest {
    @Test
    fun `OPEN state name equals to OPEN string`() {
        val state = SessionState.OPEN
        assertEquals("OPEN", state.name)
    }

    @Test
    fun `CLOSE state name equals to CLOSE string`() {
        val state = SessionState.CLOSE
        assertEquals("CLOSE", state.name)
    }
}

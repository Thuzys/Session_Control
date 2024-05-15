package pt.isel.ls.utils

import org.http4k.core.Method
import org.http4k.core.Request
import pt.isel.ls.services.PlayerManagementStunt
import pt.isel.ls.webApi.unauthorizedAccess
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

private const val DUMMY_ROUTE = "/dummyRoute"

class UnauthorizedAccessTest {
    @Test
    fun `return null for an authorized request`() {
        // Arrange
        val request =
            Request(Method.GET, DUMMY_ROUTE)
                .header("Authorization", "Bearer ${PlayerManagementStunt.playerToken}")

        // assert
        assertNull(unauthorizedAccess(request, PlayerManagementStunt))
    }

    @Test
    fun `return unauthorized message for a missing token request`() {
        // Arrange
        val request = Request(Method.GET, DUMMY_ROUTE)

        // Act
        val result = unauthorizedAccess(request, PlayerManagementStunt)

        // Assert
        assertEquals("token not provided", result)
    }

    @Test
    fun `return unauthorized message for an unauthorized request with invalid token`() {
        // Arrange
        val request =
            Request(Method.GET, DUMMY_ROUTE)
                .header("Authorization", "Bearer invalidToken")

        // Act
        val result = unauthorizedAccess(request, PlayerManagementStunt)

        // Assert
        assertEquals("invalid token", result)
    }
}

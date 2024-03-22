package pt.isel.ls.storage

import kotlinx.serialization.json.Json
import pt.isel.ls.storage.serializer.UUIDSerializer
import java.util.UUID
import kotlin.test.DefaultAsserter.assertEquals
import kotlin.test.Test

class UUIDSerializerTest {
    @Test
    fun `test of a successful serialization`() {
        val uuid = UUID.randomUUID()
        val expected = uuid.toString()
        val result = Json.encodeToString(UUIDSerializer, uuid)
        assertEquals(expected = expected, actual = result.filter { it != '"' }, message = "The UUID should be serialized to a string")
    }

    @Test
    fun `test of a successful deserialization`() {
        val uuid = UUID.randomUUID()
        val result = Json.decodeFromString(UUIDSerializer, "\"$uuid\"")
        assertEquals(expected = uuid, actual = result, message = "The UUID should be deserialized from a string")
    }
}

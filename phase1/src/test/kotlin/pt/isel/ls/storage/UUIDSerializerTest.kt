package pt.isel.ls.storage

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.json.Json
import java.util.UUID
import kotlin.test.DefaultAsserter.assertEquals
import kotlin.test.Test

class UUIDSerializerTest {
    @Test
    fun `test serialize`() {
        val uuid = UUID.randomUUID()
        val expected = uuid.toString()
        val result = Json.encodeToString(UUIDSerializer, uuid)
        assertEquals(expected = expected, actual = result.filter { it != '"' }, message = "The UUID should be serialized to a string")
    }

    @Test
    fun `test deserialize`() {
        val uuid = UUID.randomUUID()
        val result = Json.decodeFromString(UUIDSerializer, "\"$uuid\"")
        assertEquals(expected = uuid, actual = result, message = "The UUID should be deserialized from a string")
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Test
    fun `description of the UUIDSerializer`() {
        val expected = "UUID"
        val result = UUIDSerializer.descriptor.serialName
        assertEquals(expected = expected, actual = result, message = "The descriptor should have the name UUID")
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Test
    fun `kind of the UUIDSerializer`() {
        val expected = PrimitiveKind.STRING
        val result = UUIDSerializer.descriptor.kind
        assertEquals(expected = expected, actual = result, message = "The descriptor should have the kind STRING")
    }
}

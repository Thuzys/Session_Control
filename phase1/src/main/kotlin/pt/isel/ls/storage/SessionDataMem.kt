package pt.isel.ls.storage

import pt.isel.ls.domain.Domain
import pt.isel.ls.domain.errors.ReadError

/**
 * Represents the session data memory.
 *
 * This class provides methods for creating, reading, updating, and deleting [Domain] objects in the session data memory.
 * It uses the [Storage] interface to interact with the underlying storage system.
 *
 * @property mem The storage of the application.
 */
class SessionDataMem(private val mem: Storage) : DataMem {
    override fun create(newItem: Domain): UInt = mem.create(newItem)

    override fun read(
        uInt: UInt?,
        type: Int,
        offset: UInt,
        limit: UInt,
    ): Domain =
        mem.read(uInt, type, offset, limit)?.first()
            ?: throw ReadError("Unable to find the item.")

    override fun update(
        uInt: UInt,
        newItem: Domain,
    ) = mem.update(uInt, newItem)

    override fun delete(uInt: UInt) = mem.delete(uInt) // TODO: Implement delete and fix error

    @Suppress("UNCHECKED_CAST")
    override fun <T : Domain> readBy(
        type: Int,
        offset: UInt,
        limit: UInt,
        filter: (Iterable<T>) -> Collection<T>,
    ): Collection<T> =
        filter(
            (mem.read(uInt = null, type = type, offset = offset, limit = limit) as? Collection<T>) ?: emptyList(),
        )
}

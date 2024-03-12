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
class SessionDataMem(private val mem: Storage) {
    /**
     * Create a new item and storage the same.
     *
     * @param newItem the new item to be stored.
     * @return [UInt] a unique key to be associated to the new [Domain].
     */
    fun create(newItem: Domain): UInt = mem.create(newItem)

    /**
     * Reads the details of an item from the session data memory.
     *
     * @param uInt The unique identifier of each item.
     * If it's provided, the function will return the details of the item with this identifier.
     * @param type The hash of each instance class of [Domain].
     * It's used to determine the type of items to be retrieved from the storage.
     * @param offset The offset to be applied to the collection.
     * It's used for pagination purposes, to skip a certain number of items in the collection.
     * @param limit The limit to be applied to the collection.
     * It's used for pagination purposes, to limit the number of items returned in the collection.
     * @return A [Domain] object containing all the information wanted
     * or throws an IllegalArgumentException if nothing is found.
     */
    fun read(
        uInt: UInt?,
        type: Int,
        offset: Int = 0,
        limit: Int = 1,
    ): Domain =
        mem.read(uInt, type, offset, limit)?.first()
            ?: throw ReadError("Unable to find the item.")

    /**
     * Update an item given his uInt.
     *
     * @param uInt the identifier of an [Domain].
     * @param newItem the newItem to be updated.
     */
    fun update(
        uInt: UInt,
        newItem: Domain,
    ) = mem.update(uInt, newItem)

    /**
     * Delete an item given his uInt.
     *
     * @param uInt the identifier of an [Domain].
     */
    fun delete(uInt: UInt) = mem.delete(uInt) // TODO: Implement delete and fix error

    /**
     * Reads all the items of a specific type from the session data memory.
     *
     * @param type The hash of each instance class of [Domain].
     * It's used to determine the type of items to be retrieved from the storage.
     * @param offset The offset to be applied to the collection.
     * It's used for pagination purposes, to skip a certain number of items in the collection.
     * @param limit The limit to be applied to the collection.
     * It's used for pagination purposes, to limit the number of items returned in the collection.
     * @param filter The filter to be applied to the collection.
     * It's used to filter the items in the collection based on a certain condition.
     * @return A collection of [Domain] objects containing all the information wanted
     * or an empty list if nothing is found.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Domain> readBy(
        type: Int,
        offset: Int = 0,
        limit: Int = 10,
        filter: (Collection<T>) -> Collection<T>,
    ): Collection<T> =
        filter(
            (mem.read(uInt = null, type = type, offset = offset, limit = limit) as? Collection<T>) ?: emptyList(),
        )
}

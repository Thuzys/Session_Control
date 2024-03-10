package pt.isel.ls.storage

import pt.isel.ls.domain.Domain

/**
 * Represents the session data memory.
 * @property mem the storage of the application.
 */
class SessionDataMem(private val mem: Storage) {
    /**
     * Create a new item and storage the same.
     * @param newItem the new item to be stored.
     * @return [UInt] a unique key to be associated to the new [Domain].
     */
    fun create(newItem: Domain): UInt = mem.create(newItem)

    /**
     * Read the details of an item.
     * @param uInt the identifier of each item.
     * @param type the hash of each instance class of [Domain].
     * @return a [Domain] containing all the information wanted or null if nothing is found.
     */
    fun read(
        uInt: UInt,
        type: Int,
    ): Domain = mem.read(uInt, type)?.first() ?: throw IllegalArgumentException("Unable to find the item.")

    /**
     * Update an item given his uInt.
     * @param uInt the identifier of an [Domain].
     * @param newItem the newItem to be updated.
     */
    fun update(
        uInt: UInt,
        newItem: Domain,
    ) = mem.update(uInt, newItem)

    /**
     * Delete an item given his uInt.
     * @param uInt the identifier of an [Domain].
     */
    fun delete(uInt: UInt) = mem.delete(uInt) // TODO: Implement delete and fix error

    /**
     * Read all the items of a specific type.
     * @param type the hash of each instance class of [Domain].
     * @param filter the filter to be applied to the collection.
     * @return a [Collection] of [Domain] containing all the information wanted or null if nothing is found.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Domain> readBy(
        type: Int,
        filter: (Collection<T>) -> Collection<T>,
    ): Collection<T> = filter((mem.read(null, type) as? Collection<T>) ?: emptyList())
}

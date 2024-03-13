package pt.isel.ls.storage

import pt.isel.ls.domain.Domain

interface DataMem {
    /**
     * Create a new item and storage the same.
     *
     * @param newItem the new item to be stored.
     * @return [UInt] a unique key to be associated to the new [Domain].
     */
    fun create(newItem: Domain): UInt

    /**
     * Reads the details of an item from the session data memory.
     *
     * @param uInt The unique identifier of each item.
     * If it's provided, the function will return the details of the item with this identifier.
     * @param type The hash of each instance class of [Domain].
     * It's used to determine the type of items to be retrieved from the storage.
     * @param offset The offset to be applied to the collection.
     * It's used for pagination to skip a certain number of items in the collection.
     * @param limit The limit to be applied to the collection.
     * It's used for pagination to limit the number of items returned in the collection.
     * @return A [Domain] object containing all the information wanted
     * or throws an IllegalArgumentException if nothing is found.
     */
    fun read(
        uInt: UInt?,
        type: Int,
        offset: UInt = 0u,
        limit: UInt = 1u,
    ): Domain

    /**
     * Update an item given his uInt.
     *
     * @param uInt the identifier of an [Domain].
     * @param newItem the newItem to be updated.
     */
    fun update(
        uInt: UInt,
        newItem: Domain,
    )

    /**
     * Delete an item given his uInt.
     *
     * @param uInt the identifier of an [Domain].
     */
    fun delete(uInt: UInt) // TODO: Fix error

    /**
     * Reads all the items of a specific type from the session data memory.
     *
     * @param type The hash of each instance class of [Domain].
     * It's used to determine the type of items to be retrieved from the storage.
     * @param offset The offset to be applied to the collection.
     * It's used for pagination to skip a certain number of items in the collection.
     * @param limit The limit to be applied to the collection.
     * It's used for pagination to limit the number of items returned in the collection.
     * @param filter The filter to be applied to the collection.
     * It's used to filter the items in the collection based on a certain condition.
     * @param T The type of the [Domain] to be retrieved.
     * @return A collection of [Domain] objects containing all the information wanted
     * or an empty list if nothing is found.
     */
    fun <T : Domain> readBy(
        type: Int,
        offset: UInt = 0u,
        limit: UInt = 10u,
        filter: (Iterable<T>) -> Collection<T>,
    ): Collection<T>
}

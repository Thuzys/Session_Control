package pt.isel.ls.storage

import pt.isel.ls.domain.Domain

/**
 * Interface representing the storage of domain objects.
 *
 * This interface provides methods for creating, reading, updating, and deleting [Domain] objects in the storage.
 */
interface Storage {
    /**
     * Create a newItem and insert into the correct container.
     *
     * @param newItem the item to be added.
     * @return [UInt] the unique identifier of the new item.
     */
    fun create(newItem: Domain): UInt

    /**
     * Reads the details of an item or a collection of items from the storage.
     *
     * @param uInt The unique identifier of each item.
     * If it's provided, the function will return the details of the item with this identifier.
     * If it's null, the function will return a collection of items.
     * @param type The hash of each instance class of [Domain].
     * It's used to determine the type of items to be retrieved from the storage.
     * @param offset The offset to be applied to the collection.
     * It's used for pagination purposes, to skip a certain number of items in the collection.
     * @param limit The limit to be applied to the collection.
     * It's used for pagination purposes, to limit the number of items returned in the collection.
     * @return A collection of [Domain] objects containing all the information wanted
     * or null if nothing is found.
     */
    fun read(
        uInt: UInt?,
        type: Int,
        offset: UInt,
        limit: UInt,
    ): Collection<Domain>?

    /**
     * Update an Item given his uInt.
     *
     * @param uInt the identifier of an [Domain].
     * @param newItem the newItem to be updated.
     */
    fun update(
        uInt: UInt,
        newItem: Domain,
    )

    /**
     * Delete an Item given his uInt.
     *
     * @param uInt the identifier of an [Domain].
     */
    fun delete(uInt: UInt) // TODO fix error
}

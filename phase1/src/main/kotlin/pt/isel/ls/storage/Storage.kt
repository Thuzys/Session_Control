package pt.isel.ls.storage

/**
 * Represents the storage of the application.
 *
 * This interface provides methods for creating, reading, updating, and deleting objects of type [T] in the storage.
 *
 * @param T The type of the objects that this storage handles.
 */
interface Storage<T> {
    /**
     * Creates a new item and stores it.
     *
     * @param newItem The new item to be stored.
     * @return The unique identifier of the new item.
     */
    fun create(newItem: T): UInt

    /**
     * Reads the details of items from the storage.
     *
     * @param uInt The unique identifier of each item.
     * If it's provided, the function will return the details of the item with this identifier.
     * @param offset The offset to be applied to the collection.
     * It's used for pagination to skip a certain number of items in the collection.
     * @param limit The limit to be applied to the collection.
     * It's used for pagination to limit the number of items returned in the collection.
     * @return A collection of [T] objects containing all the information wanted or null if nothing is found.
     */
    fun read(
        uInt: UInt? = null,
        offset: UInt = 0u,
        limit: UInt = 10u,
    ): Collection<T>?

    /**
     * Updates an item given its unique identifier.
     *
     * @param uInt The identifier of a [T].
     * @param newItem The new item to be updated.
     */
    fun update(
        uInt: UInt,
        newItem: T,
    )

    /**
     * Deletes an item given its unique identifier.
     *
     * @param uInt The identifier of a [T].
     */
    fun delete(uInt: UInt)
}

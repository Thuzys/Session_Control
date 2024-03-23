package pt.isel.ls.storage


interface Storage<T> {

    fun create(newItem: T): UInt


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

package pt.isel.ls.storage

import pt.isel.ls.domain.Domain

/**
 * Representation of the domain's storage.
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
     * Read the details of an item.
     *
     * @param uInt the identifier of each item.
     * @param type the hash of each instance class of [Domain].
     * @return a [Collection] of [Domain] containing all the information wanted or null if nothing is found.
     */
    fun read(
        uInt: UInt?,
        type: Int,
        // TODO: Implement offset and limit
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
    fun delete(uInt: UInt)
}

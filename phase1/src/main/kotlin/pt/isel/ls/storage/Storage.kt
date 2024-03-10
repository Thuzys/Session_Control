package pt.isel.ls.storage

import pt.isel.ls.domain.Domain

/**
 * Representation of the domain's storage.
 */
interface Storage {
    /**
     * create a newItem and insert into the correct container.
     * @param newItem the item to be added.
     * @return [UInt] the unique identifier of the new item.
     */
    fun create(newItem: Domain): UInt

    /**
     * Returns a element given his uInt.
     * @param uInt the identifier of an [Domain].
     * @param type the hash of each instance class of [Domain].
     * @return [Domain] the wanted data.
     */
    fun read(
        uInt: UInt,
        type: Int,
    ): Domain?

    /**
     * Update an Item given his uInt.
     * @param uInt the identifier of an [Domain].
     * @param newItem the newItem to be updated.
     */
    fun update(
        uInt: UInt,
        newItem: Domain,
    )

    /**
     * Delete an Item given his uInt.
     * @param uInt the identifier of an [Domain].
     */
    fun delete(uInt: UInt)
}

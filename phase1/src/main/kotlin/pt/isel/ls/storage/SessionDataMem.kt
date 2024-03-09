package pt.isel.ls.storage

import pt.isel.ls.domain.Domain

/**
 * Representation of the domain's storage.
 */
interface SessionDataMem {
    /**
     * create a newItem and insert into the correct container.
     * @param newItem the item to be added.
     * @return the [UInt] that represents the newItem.
     */
    fun create(newItem: Domain): UInt

    /**
     * Returns a element given his uInt.
     * @param uInt the identifier of an [Domain].
     * @return [Domain] the wanted data.
     */
    fun read(uInt: UInt): Domain

    /**
     * Update an Item given his uInt.
     * @param uInt the identifier
     */
    fun update(
        uInt: UInt,
        newItem: Domain,
    )

    fun delete(uInt: UInt)
}

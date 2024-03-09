package pt.isel.ls.storage

import pt.isel.ls.domain.Domain

interface SessionDataMem {
    fun create(newItem: Domain): UInt

    fun read(uInt: UInt)

    fun update(
        uInt: UInt,
        newItem: Domain,
    )

    fun delete(uInt: UInt)
}

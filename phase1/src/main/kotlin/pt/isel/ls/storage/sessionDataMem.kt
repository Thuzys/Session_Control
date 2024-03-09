package pt.isel.ls.storage

interface SessionDataMem<T> {
    fun <T>create(newItem: T)
    fun read(uInt: UInt)
    fun <T>update(uInt: UInt, newItem: T)
    fun delete(uInt: UInt)
}

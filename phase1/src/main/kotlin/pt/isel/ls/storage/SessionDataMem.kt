package pt.isel.ls.storage

import pt.isel.ls.domain.Session

/**
 * Represents the session data memory.
 *
 * This class provides methods for creating, reading, updating, and deleting [Session] objects in the session data memory.
 * It uses the [Storage] interface to interact with the underlying storage system.
 *
 * @property mem The storage of the application.
 */
class SessionDataMem(private val mem: Storage<Session>) : SessionDataInterface {
    override fun createSession(newItem: Session): UInt = mem.create(newItem)

    override fun readSession(
        sid: UInt?,
        offset: UInt?,
        limit: UInt?,
    ): Collection<Session> {
        return if (offset == null || limit == null) {
            mem.read(sid) ?: throw NoSuchElementException("Unable to find the item.")
        } else {
            return mem.read(sid, offset, limit) ?: throw NoSuchElementException("Unable to find the item.")
        }
    }

    override fun updateSession(
        sid: UInt,
        newItem: Session,
    ) = mem.update(sid, newItem)
}

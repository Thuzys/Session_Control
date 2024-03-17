package pt.isel.ls.storage

import pt.isel.ls.domain.Session

/**
 * The interface that defines the operations to be performed on the Session's data.
 *
 * @property createSession the operation to store a new session.
 * @property readSession the operation to read the details of a session.
 * @property updateSession the operation to update a session.
 */
interface SessionDataInterface {
    /**
     * Create a new item and storage the same.
     *
     * @param newItem the new item to be stored.
     * @return [UInt] a unique key to be associated to the new [Session].
     */
    fun createSession(newItem: Session): UInt

    /**
     * Reads the details of a [Session] from the session data memory.
     *
     * @param sid The unique identifier of the [Session] to read.
     * If it's provided, the function will return the details of the [Session] with this identifier.
     * @param offset The offset to be applied to the collection.
     * It's used for pagination to skip a certain number of items in the collection.
     * @param limit The limit to be applied to the collection.
     * It's used for pagination to limit the number of items returned in the collection.
     * @return A [Session] object containing all the information wanted
     * or throws an IllegalArgumentException if nothing is found.
     */
    fun readSession(
        sid: UInt? = null,
        offset: UInt? = 0u,
        limit: UInt? = 1u,
    ): Collection<Session>

    /**
     * Update an item given his uInt.
     *
     * @param sid the identifier of an [Session].
     * @param newItem the newItem to be updated.
     */
    fun updateSession(
        sid: UInt,
        newItem: Session,
    )
}

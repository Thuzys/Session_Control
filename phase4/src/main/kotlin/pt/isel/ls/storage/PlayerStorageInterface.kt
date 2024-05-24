package pt.isel.ls.storage

import pt.isel.ls.domain.Email
import pt.isel.ls.domain.Player

/**
 * PlayerStorage is an interface that defines the operations that can be performed on a Player.
 * It provides methods to create, read, update, and delete a Player.
 */
interface PlayerStorageInterface {
    /**
     * Creates a new Player and returns the unique identifier of the created Player.
     *
     * @param newItem The Player object to be created.
     * @return The unique identifier of the created Player.
     */
    fun create(newItem: Player): UInt

    /**
     * Reads the details of a Player given its unique identifier.
     *
     * @param pid The unique identifier of the Player.
     * @return The Player object with the given unique identifier.
     */
    fun read(pid: UInt): Player

    /**
     * Reads the details of Players from the storage.
     *
     * @param email The email of the Player.
     * If it's provided, the function will return the details of the Player with this email.
     * @param token The token of the Player.
     * If it's provided, the function will return the details of the Player with this token.
     * @param limit The limit to be applied to the collection.
     * @param offset The offset to be applied to the collection.
     * It's used for pagination to skip a certain number of items in the collection.
     * @return A collection of Player objects containing all the information wanted or null if nothing is found.
     */
    fun readBy(
        email: Email? = null,
        token: String? = null,
        username: String? = null,
        limit: UInt = LIMIT.toUInt(),
        offset: UInt = OFFSET.toUInt(),
    ): Collection<Player>?

    /**
     * Updates a Player given its unique identifier.
     *
     * @param newItem The new Player object to be updated.
     */
    fun update(newItem: Player)

    /**
     * Deletes a Player given its unique identifier.
     *
     * @param uInt The unique identifier of the Player.
     */
    fun delete(uInt: UInt)

    /**
     * Deletes a token from the storage.
     *
     * @param pid The token to be deleted.
     */
    fun deleteToken(pid: UInt)
}

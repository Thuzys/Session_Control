package pt.isel.ls.storage

import pt.isel.ls.domain.Domain
import pt.isel.ls.domain.Email
import pt.isel.ls.domain.Player

class StorageStunt : Storage {
    private val defaultMail = Email("default@mail.com")

    private var playerUuid: UInt = 3u
    private val hashPlayer: HashMap<UInt, Player> =
        hashMapOf(
            1u to Player(1u, "test1", defaultMail),
            2u to Player(2u, "test20", defaultMail),
        )

    override fun create(newItem: Domain): UInt =
        when (newItem) {
            is Player ->
                {
                    val uuid = playerUuid++
                    hashPlayer[uuid] = newItem.copy(uuid = uuid)
                    uuid
                }

            else ->
                {
                    1u
                }
        }

    override fun read(
        uInt: UInt,
        type: Int,
    ): Domain? =
        when (type) {
            Player.hash -> {
                hashPlayer[uInt]
            }
            else -> null
        }

    override fun update(
        uInt: UInt,
        newItem: Domain,
    ) {
        TODO("Not yet implemented")
    }

    override fun delete(uInt: UInt) {
        TODO("Not yet implemented")
    }
}

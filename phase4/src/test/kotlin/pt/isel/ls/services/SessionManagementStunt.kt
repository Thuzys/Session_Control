package pt.isel.ls.services

import kotlinx.datetime.LocalDate
import org.eclipse.jetty.util.security.Password
import pt.isel.ls.domain.Email
import pt.isel.ls.domain.Player
import pt.isel.ls.domain.Session
import pt.isel.ls.domain.SessionState
import pt.isel.ls.domain.errors.ServicesError
import pt.isel.ls.domain.info.AuthenticationParam
import pt.isel.ls.domain.info.GameInfo
import pt.isel.ls.domain.info.GameInfoParam
import pt.isel.ls.domain.info.PlayerInfo
import pt.isel.ls.domain.info.PlayerInfoParam
import pt.isel.ls.domain.info.SessionInfo
import java.util.UUID

private val pid = 1u
private val sid1 = 1u
private val sid2 = 2u
private val gid1 = 1u
private val date1 = LocalDate(2024, 3, 10)
private val owner1 = PlayerInfo(1u, "test1")
private val owner2 = PlayerInfo(2u, "test2")
private val p1Token = UUID.fromString("568f8e19-4e4c-43a2-a1c9-d416aa39a8b4")
private const val P1_NAME = "test1"
private const val P1_USERNAME = "test1"
private val p1Email = Email("test1@gmail.com")
private val p1Password = Password("test1")

object SessionManagementStunt : SessionServices {
    private val player1 = PlayerInfo(1u, "test1")
    private val player2 = PlayerInfo(2u, "test2")
    private val players2: Collection<PlayerInfo> = listOf(player1, player2)
    private val gameInfoVal = GameInfo(1u, "Game")

    override fun addPlayer(
        player: UInt,
        session: UInt,
    ) = if (player != pid || session != sid1) {
        throw ServicesError("Unable to add player")
    } else {
        // do nothing
    }

    override fun sessionDetails(
        sid: UInt,
        limit: UInt?,
        offset: UInt?,
    ): Session =
        if (sid == sid1) {
            Session(sid, 1u, gameInfoVal, date1, player1, players2)
        } else {
            throw ServicesError("Session not found")
        }

    override fun createSession(
        gameInfo: GameInfoParam,
        date: LocalDate,
        capacity: UInt,
        owner: PlayerInfoParam,
    ): UInt {
        val (ownerId, userName) = owner
        val (gid, _) = gameInfo
        return if (ownerId != null && userName != null && gid != null) {
            sid1
        } else {
            throw ServicesError("Unable to create session")
        }
    }

    override fun getSessions(
        gameInfo: GameInfoParam?,
        date: LocalDate?,
        state: SessionState?,
        playerInfo: PlayerInfoParam?,
        offset: UInt?,
        limit: UInt?,
    ): Collection<SessionInfo> {
        return when {
            gameInfo?.first == gid1 && state == SessionState.CLOSE || gameInfo?.second == "Game" ->
                listOf(
                    SessionInfo(sid1, owner1, gameInfoVal, date1),
                    SessionInfo(sid2, owner2, gameInfoVal, date1),
                )
            gameInfo?.first == gid1 && state == SessionState.OPEN || playerInfo?.second == "test1" ->
                listOf(
                    SessionInfo(sid2, owner2, gameInfoVal, date1),
                )
            date == date1 && state == SessionState.OPEN ->
                listOf(
                    SessionInfo(sid1, owner1, gameInfoVal, date1),
                    SessionInfo(sid2, owner2, gameInfoVal, date1),
                )
            gameInfo?.first == 400u -> emptyList()
            playerInfo?.first == 2u ->
                listOf(
                    SessionInfo(sid2, owner2, gameInfoVal, date1),
                )
            else -> emptyList()
        }
    }

    override fun updateCapacityOrDate(
        authentication: AuthenticationParam,
        capacity: UInt?,
        date: LocalDate?,
    ) {
        if (authentication.first != pid || authentication.second != sid1) {
            throw ServicesError("Unable to update session")
        } else {
            // do nothing
        }
    }

    override fun removePlayer(
        player: UInt,
        session: UInt,
        token: String,
    ) {
        if (player != pid || session != sid1) throw ServicesError("Unable to remove player")
    }

    override fun deleteSession(sid: UInt) {
        if (sid != sid1) {
            throw ServicesError("Unable to delete the session")
        }
    }

    override fun getPlayerFromSession(
        player: UInt,
        session: UInt,
    ): Player? =
        if (player != pid || session != sid1) {
            null
        } else {
            Player(
                pid,
                P1_NAME,
                P1_USERNAME,
                p1Email,
                p1Password,
                p1Token,
            )
        }
}

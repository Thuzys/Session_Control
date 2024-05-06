package pt.isel.ls.services

import kotlinx.datetime.LocalDate
import pt.isel.ls.domain.Session
import pt.isel.ls.domain.SessionState
import pt.isel.ls.domain.errors.ServicesError
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

object SessionManagementStunt : SessionServices {
    val playerToken: UUID = UUID.randomUUID()
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

    override fun getSessionDetails(
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
                    SessionInfo(sid1, 1u, gameInfoVal, date1),
                    SessionInfo(sid2, 2u, gameInfoVal, date1),
                )
            gameInfo?.first == gid1 && state == SessionState.OPEN || playerInfo?.second == "test1" ->
                listOf(
                    SessionInfo(sid2, 2u, gameInfoVal, date1),
                )
            date == date1 && state == SessionState.OPEN ->
                listOf(
                    SessionInfo(sid1, 1u, gameInfoVal, date1),
                    SessionInfo(sid2, 2u, gameInfoVal, date1),
                )
            gameInfo?.first == 400u -> emptyList()
            playerInfo?.first == 2u ->
                listOf(
                    SessionInfo(sid2, 2u, gameInfoVal, date1),
                )
            else -> throw ServicesError("There are no Sessions that satisfy the given details")
        }
    }

    override fun updateCapacityOrDate(
        sid: UInt,
        capacity: UInt?,
        date: LocalDate?,
    ) {
    }

    override fun removePlayer(
        player: UInt,
        session: UInt,
    ) {
        if (player != pid || session != sid1) throw ServicesError("Unable to remove player")
    }

    override fun deleteSession(sid: UInt) {
        if (sid != sid1) {
            throw ServicesError("Unable to delete the session")
        }
    }
}

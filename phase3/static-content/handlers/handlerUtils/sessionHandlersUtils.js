import constants from "../../constants/constants.js";

/**
 * Check if player is in session
 * @param session
 * @returns {boolean}
 */
function isPlayerInSession(session) {
    if (!session.players) {
        return false;
    }
    return session.players.some(p => p.pid === constants.TEMPORARY_USER_ID);
}

/**
 * Check if a player is owner of a session
 * @param session
 * @returns {boolean}
 */
function isPlayerOwner(session) {
    return session.owner.pid === constants.TEMPORARY_USER_ID;
}

export {
    isPlayerInSession,
    isPlayerOwner
}
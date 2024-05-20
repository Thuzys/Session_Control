import constants from "../../constants/constants.js";

/**
 * Check if player is in session
 * @param session
 * @returns {boolean}
 */
function isValidPlayer(session) {
    return session.pid !== undefined;
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
    isValidPlayer,
    isPlayerOwner
}
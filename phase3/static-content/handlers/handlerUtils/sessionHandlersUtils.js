import constants from "../../constants/constants.js";

function isPlayerInSession(session) {
    return session.players.some(p => p.pid === constants.TEMPORARY_USER_ID);
}

function isPlayerOwner(session) {
    return session.owner.pid === constants.TEMPORARY_USER_ID;
}

export {
    isPlayerInSession,
    isPlayerOwner
}
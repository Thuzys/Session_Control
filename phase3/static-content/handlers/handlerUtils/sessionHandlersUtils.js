import constants from "../../constants/constants.js";

function isPlayerInSession(session) {
    return session.players.some(p => p.id === constants.TEMPORARY_USER_ID);
}

function isPlayerOwner(session) {
    return session.owner.id === constants.TEMPORARY_USER_ID;
}

export {
    isPlayerInSession,
    isPlayerOwner
}
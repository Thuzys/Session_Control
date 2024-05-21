/**
 * Check if a player is owner of a session
 * @param session
 * @returns {boolean}
 */
function isPlayerOwner(session) {
    const pid = parseInt(sessionStorage.getItem('pid'));
    return session.owner.pid === pid;
}

export {
    isPlayerOwner
}
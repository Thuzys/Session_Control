import handlerUtils from "./handlerUtils/handlerUtils.js";
import requestUtils from "../utils/requestUtils.js";
import constants from "../constants/constants.js";
import sessionHandlerViews from "../views/handlerViews/sessionHandlerViews.js";
import {fetcher} from "../utils/fetchUtils.js";
import {isPlayerOwner} from "./handlerUtils/sessionHandlersUtils.js";
import handlerViews from "../views/handlerViews/handlerViews.js";


/**
 * Search sessions by game id, player id, date and state
 *
 * @param mainContent main content of the page
 */
function searchSessions(mainContent) {
    const container = sessionHandlerViews.createSearchSessionsView();
    container.onsubmit = (e) => handleSearchSessionsSubmit(e);
    mainContent.replaceChildren(container);
}

/**
 * Handle search sessions submit event
 *
 * @param e event that triggered submit
 */
function handleSearchSessionsSubmit(e) {
    e.preventDefault();
    const params = new URLSearchParams();
    ['gameName', 'username', 'date'].forEach(id => {
        const value = document.getElementById(id).value;
        if (value) params.set(id, value.replace(':', '_'));
    });
    ['open', 'close'].forEach(state => {
        if (document.querySelector(`input[name="state"][value="${state}"]`).checked) params.set('state', state);
    });
    params.set('offset', "0");
    handlerUtils.changeHash(`#sessions?${params}`);
}

/**
 * Get sessions by query parameters and display them in the main content area of the page
 *
 * @param mainContent main content of the page
 */
function getSessions(mainContent) {
    const query = requestUtils.getQuery();
    const queryString = handlerUtils.makeQueryString(query);
    const token = sessionStorage.getItem('token');
    const url = `${constants.API_BASE_URL}${constants.SESSION_ROUTE}?${queryString}`;
    fetcher.get(url, token)
        .then(
            response =>
                handleGetSessionsResponse(response, mainContent)
        )
}

/**
 * Handle get sessions response from the server
 *
 * @param sessions response from the server
 * @param mainContent main content of the page
 */
function handleGetSessionsResponse(sessions, mainContent) {
    const container = sessionHandlerViews.createGetSessionsView(sessions);
    mainContent.replaceChildren(container);
}

/**
 * Handle search sessions submit event
 * @param mainContent main content of the page
 * @param gid game id
 * @param gameName game name to display
 */
function createSession(mainContent, gid, gameName) {
    const container = sessionHandlerViews.createCreateSessionView(gameName);
    container.onsubmit = (e) => handleCreateSessionSubmit(e, gid);
    mainContent.replaceChildren(container);
}

/**
 * Handle create session form submit event
 *
 * @param e event that triggered submit
 * @param gid game id
 */
function handleCreateSessionSubmit(e, gid) {
    e.preventDefault();
    const capacity = document.getElementById('capacity').value;
    const date = document.getElementById('dateCreate').value;
    const pid = sessionStorage.getItem('pid');
    const token = sessionStorage.getItem('token');
    const url = `${constants.API_BASE_URL}${constants.SESSION_ROUTE}`;
    const body = {
        gid: gid.toString(),
        capacity: capacity,
        date: date,
        owner: pid,
    };
    fetcher
        .post(url, body, token)
        .then(response => handleCreateSessionResponse(response))
}

/**
 * Handle create session response
 * @param response
 */
function handleCreateSessionResponse(response) {
    handlerUtils.changeHash("#sessions/" + response.id + "?offset=0");
}

/**
 * Get session details by session id and display them in the main content area of the page
 *
 * @param mainContent main content of the page
 */
function getSessionDetails(mainContent) {
    const url = `${constants.API_BASE_URL}${constants.SESSION_ID_ROUTE}${requestUtils.getParams()}`;
    const token = sessionStorage.getItem('token');
    fetcher.get(url, token)
        .then(
            response =>
                handleGetSessionDetailsResponse(response, mainContent)
        )
}

/**
 * Make session details
 *
 * @param session
 * @param mainContent
 */
function makeSessionDetails(session, mainContent) {
    Promise.resolve(sessionStorage.getItem('isInSession') === "true")
        .then(isInSession => {
            const playerListView = sessionHandlerViews.createPlayerListView(session);
            Promise.resolve(sessionStorage.getItem('isOwner') === "true")
                .then(isOwner => {
                    const container = sessionHandlerViews.createSessionDetailsView(
                        session,
                        playerListView,
                        isOwner,
                        isInSession,
                        addPlayerToSession,
                        removePlayerFromSession,
                        deleteSession
                    );
                    mainContent.replaceChildren(container);
                });
        });
}

/**
 * Handle get session details response from the server
 *
 * @param session response from the server
 * @param mainContent main content of the page
 */
function handleGetSessionDetailsResponse(session, mainContent) {
    window.addEventListener('hashchange', function() {
        if (!location.hash.includes('sessions/')) {
            sessionStorage.removeItem('isOwner');
            sessionStorage.removeItem('isInSession');
        }
    });

    let isOwner = sessionStorage.getItem('isOwner');
    if (isOwner == null) {
        isOwner = isPlayerOwner(session);
        sessionStorage.setItem('isOwner', isOwner.toString());
    }
    const pid = sessionStorage.getItem('pid');
    const token = sessionStorage.getItem('token');

    const url = `${constants.API_BASE_URL}${constants.SESSION_ID_ROUTE}${session.sid}/${pid}`;
    let isInSession = sessionStorage.getItem('isInSession');
    let getPlayerFromSessionError = false;
    if (isInSession === null) {
        fetcher.get(
            url,
            token,
            false,
            () => {
                sessionStorage.setItem('isInSession', "false")
                getPlayerFromSessionError = true;
            }
        ).then(_ => {
            if (!getPlayerFromSessionError) {
                sessionStorage.setItem('isInSession', "true");
            }
            makeSessionDetails(session, mainContent);
        });
    } else {
        makeSessionDetails(session, mainContent);
    }
}

/**
 * Add player to session
 * @param sid
 */
function addPlayerToSession(sid) {
    const pid = sessionStorage.getItem('pid');
    const token = sessionStorage.getItem('token');
    const url = `${constants.API_BASE_URL}${constants.SESSION_ID_ROUTE}${sid}/${pid}`;
    fetcher.put(url, token)
        .then( _ =>
            window.location.reload()
        )
}

/**
 * Remove player from session
 * @param sid
 */
function removePlayerFromSession(sid) {
    const pid = sessionStorage.getItem('pid');
    const token = sessionStorage.getItem('token');
    const url = `${constants.API_BASE_URL}${constants.SESSION_ID_ROUTE}${sid}/${pid}`;
    fetcher.del(url, token)
        .then( _ =>
            window.location.reload()
        )
}

/**
 * Update session capacity or date
 * @param mainContent main content of the page
 */
function updateSession(mainContent) {
    const url = `${constants.API_BASE_URL}${constants.SESSION_ID_ROUTE}${requestUtils.getParams()}`;
    const token = sessionStorage.getItem('token');
    fetcher
        .get(url, token)
        .then(session => {
            const container = sessionHandlerViews.createUpdateSessionView(session);
            container.onsubmit = (e) => handleUpdateSessionSubmit(e);
            mainContent.replaceChildren(container);
        });
}

/**
 * Handle update session submit event
 * @param e event that triggered submit
 */
function handleUpdateSessionSubmit(e) {
    e.preventDefault();
    const sid = requestUtils.getParams();
    const capacity = document.getElementById('capacity').value;
    const date = document.getElementById('dateChange').value;
    const pid = sessionStorage.getItem('pid');
    const token = sessionStorage.getItem('token');
    const url = `${constants.API_BASE_URL}${constants.SESSION_ID_ROUTE}${sid}`;
    const body = {
        capacity: capacity,
        date: date,
        pid: pid,
    };
    fetcher
        .put(url, token, body)
        .then(_ => handlerUtils.changeHash("#sessions/" + sid + "?offset=0"))
}

/**
 * Delete session by session id
 * @param sid
 */
function deleteSession(sid) {
    const url = constants.API_BASE_URL + constants.SESSION_ID_ROUTE + sid;
    const token = sessionStorage.getItem('token');
    fetcher.del(url, token)
        .then(() => {
            handlerViews.showAlert("Session deleted successfully");
            handlerUtils.changeHash("#sessionSearch");
        })
        .catch(() => window.alert("Session could not be deleted"))
}

export default {
    searchSessions,
    getSessions,
    getSessionDetails,
    createSession,
    updateSession,
};
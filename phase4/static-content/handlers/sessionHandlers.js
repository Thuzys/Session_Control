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
    const token = sessionStorage.getItem('token');
    const url = handlerUtils.createURL(constants.API_SESSION_ROUTE, requestUtils.getQuery());

    fetcher.get(url, token)
        .then(
            response =>
                handleGetSessionsResponse(response, mainContent)
        )
    mainContent.replaceChildren(handlerViews.createLoaderView());
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
    const url = handlerUtils.createURL(constants.API_SESSION_ROUTE);
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
    handlerUtils.changeHash(`${constants.SESSION_ROUTE}/${response.id}?offset=0`);
}

/**
 * Get session details by session id and display them in the main content area of the page
 *
 * @param mainContent main content of the page
 */
function getSessionDetails(mainContent) {
    const sid = requestUtils.getParams();
    const route = handlerUtils.createRoute(constants.API_SESSION_ROUTE, sid)
    const url = handlerUtils.createURL(route);
    const token = sessionStorage.getItem('token');

    fetcher.get(url, token)
        .then(
            response =>
                handleGetSessionDetailsResponse(response, mainContent)
        )
    mainContent.replaceChildren(handlerViews.createLoaderView());
}

/**
 * Make session details
 *
 * @param session session to display
 * @param mainContent main content of the page
 */
function makeSessionDetails(session, mainContent) {
    let playerListView;
    if (!isPlayerOwner(session)) {
        playerListView = sessionHandlerViews.createPlayerListView(session);
    } else {
        playerListView = sessionHandlerViews.createPlayerListView(session, removePlayerFromSession);
    }
    const container = sessionHandlerViews.createSessionDetailsView(
        session,
        playerListView,
        isPlayerOwner(session),
        sessionStorage.getItem('isInSession') === "true",
        addPlayerToSession,
        removePlayerFromSession,
        deleteSession
    );
    mainContent.replaceChildren(container);
}

/**
 * Handle get session details response from the server
 *
 * @param session response from the server
 * @param mainContent main content of the page
 */
function handleGetSessionDetailsResponse(session, mainContent) {
    const pid = sessionStorage.getItem('pid');
    const token = sessionStorage.getItem('token');
    const route = handlerUtils.createRoute(constants.API_SESSION_ROUTE, session.sid, pid);
    const url = handlerUtils.createURL(route);

    const isInSession = sessionStorage.getItem('isInSession');
    if (isInSession === null) {
        fetcher.get(
            url,
            token,
            false
        ).then(response => {
            if (response !== null) {
                sessionStorage.setItem('isInSession', "true");
            } else {
                sessionStorage.setItem('isInSession', "false");
            }
            makeSessionDetails(session, mainContent);
        });
        mainContent.replaceChildren(handlerViews.createLoaderView());
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
    const route = handlerUtils.createRoute(constants.API_SESSION_ROUTE, sid, pid);
    const url = handlerUtils.createURL(route);

    fetcher.put(url, token)
        .then( _ => {
            sessionStorage.setItem('isInSession', "true");
            window.location.reload();
        })
}

/**
 * Remove player from session
 * @param sid
 * @param pid_remove
 */
function removePlayerFromSession(sid, pid_remove = undefined) {
    let pid;
    if (!pid_remove) {
        pid = sessionStorage.getItem('pid');
    } else  {
        pid = pid_remove;
    }
    const token = sessionStorage.getItem('token');
    const route = handlerUtils.createRoute(constants.API_SESSION_ROUTE, sid, pid);
    const url = handlerUtils.createURL(route);

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
    const sid = requestUtils.getParams();
    const route = handlerUtils.createRoute(constants.API_SESSION_ROUTE, sid)
    const url = handlerUtils.createURL(route);
    const token = sessionStorage.getItem('token');

    fetcher
        .get(url, token)
        .then(session => {
            const container = sessionHandlerViews.createUpdateSessionView(session);
            container.onsubmit = (e) => handleUpdateSessionSubmit(e);
            mainContent.replaceChildren(container);
        });
    mainContent.replaceChildren(handlerViews.createLoaderView());
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
    const route = handlerUtils.createRoute(constants.API_SESSION_ROUTE, sid);
    const url = handlerUtils.createURL(route);
    const body = {
        capacity: capacity,
        date: date,
        pid: pid,
    };

    fetcher
        .put(url, token, body)
        .then(_ => handlerUtils.changeHash(`${constants.SESSION_ROUTE}/${sid}?offset=0`))
}

/**
 * Delete session by session id
 * @param sid
 */
function deleteSession(sid) {
    const route = handlerUtils.createRoute(constants.API_SESSION_ROUTE, sid);
    const url = handlerUtils.createURL(route);
    const token = sessionStorage.getItem('token');

    fetcher.del(url, token)
        .then(() => {
            handlerViews.showAlert("Session deleted successfully");
            handlerUtils.changeHash(constants.SESSION_SEARCH_ROUTE);
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
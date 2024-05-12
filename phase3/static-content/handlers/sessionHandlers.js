import handlerUtils from "./handlerUtils/handlerUtils.js";
import views from "../views/viewsCreators.js";
import requestUtils from "../utils/requestUtils.js";
import constants from "../constants/constants.js";
import sessionHandlerViews from "../views/handlerViews/sessionHandlerViews.js";
import {fetcher} from "../utils/fetchUtils.js";
import {isPlayerInSession, isPlayerOwner} from "./handlerUtils/sessionHandlersUtils.js";

/**
 * Search sessions by game id, player id, date and state
 *
 * @param mainContent main content of the page
 */
function searchSessions(mainContent) {
    const formContent =  sessionHandlerViews.createSessionFormContentView();
    const form = views.form({}, ...formContent);
    form.addEventListener('submit', (e) => handleSearchSessionsSubmit(e));
    mainContent.replaceChildren(views.div({class: "player-details-container"}, form));
}

/**
 * Handle search sessions submit event
 *
 * @param e event that triggered submit
 */
function handleSearchSessionsSubmit(e) {
    e.preventDefault();
    const params = new URLSearchParams();
    ['gameName', 'userName', 'date'].forEach(id => {
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
 * Handle create session form submit event
 *
 * @param e event that triggered submit
 * @param gid
 */
function handleCreateSessionSubmit(e, gid) {
    e.preventDefault();
    const capacity = document.getElementById('capacity').value;
    const date = document.getElementById('dateCreate').value;
    const url = `${constants.API_BASE_URL}${constants.SESSION_ROUTE}`;
    const body = {
        gid: gid.toString(),
        capacity: capacity,
        date: date,
        owner: constants.TEMPORARY_USER_ID.toString(),
    };
    fetcher
        .post(url, body, constants.TOKEN)
        .then(response => handleCreateSessionResponse(response))
}

/**
 * Get sessions by query parameters and display them in the main content area of the page
 *
 * @param mainContent main content of the page
 */
function getSessions(mainContent) {
    const query = requestUtils.getQuery();
    const queryString = handlerUtils.makeQueryString(query);
    const url = `${constants.API_BASE_URL}${constants.SESSION_ROUTE}?${queryString}`;
    fetcher.get(url, constants.TOKEN)
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
    const [sessionsView, nextPrevView] = sessionHandlerViews.createGetSessionsView(sessions);
    mainContent.replaceChildren(views.div({class: "player-details-container"}, sessionsView, nextPrevView));
}

/**
 * Get session details by session id and display them in the main content area of the page
 *
 * @param mainContent main content of the page
 */
function getSessionDetails(mainContent) {
    const url = `${constants.API_BASE_URL}${constants.SESSION_ID_ROUTE}${requestUtils.getParams()}`;
    fetcher.get(url, constants.TOKEN)
        .then(
            response =>
                handleGetSessionDetailsResponse(response, mainContent)
        )
}

/**
 * Handle get session details response from the server
 *
 * @param session response from the server
 * @param mainContent main content of the page
 */
function handleGetSessionDetailsResponse(session, mainContent) {
    const isOwner = isPlayerOwner(session);
    const url = `${constants.API_BASE_URL}${constants.SESSION_ID_ROUTE}${session.sid}/${constants.TEMPORARY_USER_ID}`;
    fetcher.get(url, constants.TOKEN)
        .then(isInSession => {
            return isInSession === true;
        }
        ).then(isInSession => {
            const playerListView = sessionHandlerViews.createPlayerListView(session);
            const sessionDetailsView = sessionHandlerViews.createSessionDetailsViews(session, playerListView, isOwner, isInSession);
            mainContent.replaceChildren(views.div({class: "player-details-container"}, sessionDetailsView));
        })
}

/**
 * Add player to session
 * @param sid
 */
function addPlayerToSession(sid) {
    const url = `${constants.API_BASE_URL}${constants.SESSION_ID_ROUTE}${sid}/${constants.TEMPORARY_USER_ID}`;
    fetcher.put(url, constants.TOKEN)
        .then( _ =>
            window.location.reload()
        )
}

/**
 * Remove player from session
 * @param sid
 */
function removePlayerFromSession(sid) {
    const url = `${constants.API_BASE_URL}${constants.SESSION_ID_ROUTE}${sid}/${constants.TEMPORARY_USER_ID}`;
    fetcher.del(url, constants.TOKEN)
        .then( _ =>
            window.location.reload()
        )
}

/**
 * Delete session by session id
 * @param sid
 */
function deleteSession(sid) {
    const url = constants.API_BASE_URL + constants.SESSION_ID_ROUTE + sid;
    fetcher.del(url, constants.TOKEN)
        .then(() => {
            window.alert("Session deleted successfully");
            handlerUtils.changeHash("#sessionSearch");
        })
        .catch(() => window.alert("Session could not be deleted"))
}

/**
 * Handle create session response
 * @param response
 */
function handleCreateSessionResponse(response) {
    handlerUtils.changeHash("#sessions/" + response.id + "?offset=0");
}

/**
 * Handle search sessions submit event
 * @param mainContent main content of the page
 * @param gid game id
 * @param gameName game name to display
 */
function createSession(mainContent, gid, gameName) {
    const [h1CreateSession, formCreateSession] = sessionHandlerViews.createCreateSessionView(gameName);
    formCreateSession.addEventListener('submit', (e) => handleCreateSessionSubmit(e, gid));
    mainContent.replaceChildren(views.div({class: "player-details-container"}, h1CreateSession, formCreateSession));
}

/**
 * Update session capacity or date
 * @param mainContent main content of the page
 */
function updateSession(mainContent) {
    const url = `${constants.API_BASE_URL}${constants.SESSION_ID_ROUTE}${requestUtils.getParams()}`;
    fetcher
        .get(url, constants.TOKEN)
        .then( session => {
            const [header, form] = sessionHandlerViews.createUpdateSessionView(session);
            form.addEventListener('submit', (e) => handleUpdateSessionSubmit(e));
            mainContent.replaceChildren(views.div({class: "player-details-container"}, header, form));
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
    const url = `${constants.API_BASE_URL}${constants.SESSION_ID_ROUTE}${sid}`;
    const body = {
        capacity: capacity,
        date: date
    };
    fetcher
        .put(url, constants.TOKEN, body)
        .then(_ => handlerUtils.changeHash("#sessions/" + sid + "?offset=0"))
}

export default {
    searchSessions,
    getSessions,
    getSessionDetails,
    createSession,
    updateSession,
    addPlayerToSession,
    removePlayerFromSession,
    deleteSession
};
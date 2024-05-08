import handlerUtils from "./handlerUtils/handlerUtils.js";
import views from "../views/viewsCreators.js";
import menu from "../navigation/menuLinks.js";
import requestUtils from "../utils/requestUtils.js";
import constants from "../constants/constants.js";
import sessionHandlerViews from "../views/handlerViews/sessionHandlerViews.js";
import handlerViews from "../views/handlerViews/handlerViews.js";
import {fetcher} from "../utils/fetchUtils.js";
import {isPlayerInSession, isPlayerOwner} from "./handlerUtils/sessionHandlersUtils.js";

/**
 * Search sessions by game id, player id, date and state
 *
 * @param mainContent main content of the page
 * @param mainHeader main header of the page
 */
function searchSessions(mainContent, mainHeader) {
    const h1 = handlerViews.createHeader("Search Sessions: ");
    const formContent = sessionHandlerViews.createSearchSessionView();
    const form = views.form({}, ...formContent);
    form.addEventListener('submit', (e) => handleSearchSessionsSubmit(e));

    mainContent.replaceChildren(views.div({class: "player-details-container"}, h1, form));
    mainHeader.replaceChildren(menu.get("playerSearch"), menu.get("home"), menu.get("gameSearch"));
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
 * @param mainHeader main header of the page
 */
function getSessions(mainContent, mainHeader) {
    const query = requestUtils.getQuery();
    const queryString = handlerUtils.makeQueryString(query);
    const url = `${constants.API_BASE_URL}${constants.SESSION_ROUTE}?${queryString}`;
    fetcher.get(url, constants.TOKEN)
        .then(
            response =>
                handleGetSessionsResponse(response, mainContent, mainHeader)
        )
}

/**
 * Handle get sessions response from the server
 *
 * @param sessions response from the server
 * @param mainContent main content of the page
 * @param mainHeader main header of the page
 */
function handleGetSessionsResponse(sessions, mainContent, mainHeader) {
    const [sessionsView, nextPrevView] = sessionHandlerViews.createGetSessionsView(sessions);
    mainContent.replaceChildren(views.div({class: "player-details-container"}, sessionsView, nextPrevView));
    mainHeader.replaceChildren(menu.get("playerSearch"), menu.get("home"), menu.get("sessionSearch"), menu.get("gameSearch"));
}

/**
 * Get session details by session id and display them in the main content area of the page
 *
 * @param mainContent
 * @param mainHeader
 */
function getSessionDetails(mainContent, mainHeader) {
    const url = `${constants.API_BASE_URL}${constants.SESSION_ID_ROUTE}${requestUtils.getParams()}`;
    fetcher.get(url, constants.TOKEN)
        .then(
            response =>
                handleGetSessionDetailsResponse(response, mainContent, mainHeader)
        )
}

/**
 * Handle get session details response from the server
 *
 * @param session response from the server
 * @param mainContent main content of the page
 * @param mainHeader main header of the page
 */
function handleGetSessionDetailsResponse(session, mainContent, mainHeader) {
    const isOwner =  isPlayerOwner(session);
    const isInSession = isPlayerInSession(session);

    const playerListView = sessionHandlerViews.createPlayerListView(session);
    const sessionDetailsView = sessionHandlerViews.createSessionDetailsViews(session, playerListView, isOwner, isInSession);
    mainContent.replaceChildren(views.div({class: "player-details-container"}, sessionDetailsView));
    mainHeader.replaceChildren(menu.get("playerSearch"), menu.get("home"), menu.get("gameSearch"));
}

/**
 * Handle create session response from the server
 * @param response response from the server
 */
function handleCreateSessionResponse(response) {
    response ? handlerUtils.changeHash("#sessions/" + response.id + "?offset=0") : alert("Failed to create session.");
}

/**
 * Handle search sessions submit event
 * @param mainContent main content of the page
 * @param mainHeader main header of the page
 * @param gid game id
 * @param gameName game name to display
 */
function createSession(mainContent, mainHeader, gid, gameName) {
    const [h1CreateSession, formCreateSession] = sessionHandlerViews.createCreateSessionView(gameName);
    formCreateSession.addEventListener('submit', (e) => handleCreateSessionSubmit(e, gid));
    mainContent.replaceChildren(views.div({class: "player-details-container"}, h1CreateSession, formCreateSession));
    mainHeader.replaceChildren(menu.get("playerSearch"), menu.get("home"), menu.get("gameSearch"), menu.get("sessionSearch"));
}

/**
 * Handle update session submit event
 * @param e event that triggered submit
 */
function handleUpdateSessionSubmit(e) {
    e.preventDefault();
    const sid = requestUtils.getQuery().get('sid');
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

/**
 * Update session capacity or date
 * @param mainContent main content of the page
 * @param mainHeader main header of the page
 */
function updateSession(mainContent, mainHeader) {
    const url = `${constants.API_BASE_URL}${constants.SESSION_ID_ROUTE}${requestUtils.getQuery().get('sid')}`;
    fetcher
        .get(url, constants.TOKEN)
        .then( session => {
            const [header, form] = sessionHandlerViews.createUpdateSessionForm(session);
            form.addEventListener('submit', (e) => handleUpdateSessionSubmit(e));
            mainContent.replaceChildren(views.div({class: "player-details-container"}, header, form));
            mainHeader.replaceChildren(
                menu.get("playerSearch"),
                menu.get("home"),
                menu.get("gameSearch"),
                menu.get("sessionSearch")
            );
        });
}

export default {
    searchSessions,
    getSessions,
    getSessionDetails,
    createSession,
    updateSession,
};
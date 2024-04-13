import handlerUtils from "./handlerUtils/handlerUtils.js";
import views from "../views/viewsCreators.js";
import menu from "../navigation/menuLinks.js";
import requestUtils from "../utils/requestUtils.js";
import constants from "../constants/constants.js";
import sessionHandlerViews from "../views/handlerViews/sessionHandlerViews.js";
import handlerViews from "../views/handlerViews/handlerViews.js";

/**
 * Search sessions by game id, player id, date and state
 *
 * @param mainContent main content of the page
 * @param mainHeader main header of the page
 */
function searchSessions(mainContent, mainHeader) {
    const h1 = handlerViews.createHeader("Search Sessions: ");
    const formContent = sessionHandlerViews.createSessionFormContentView();
    const form = views.form({}, ...formContent);
    mainContent.replaceChildren(h1, form);
    mainHeader.replaceChildren(menu.get("home"));
    form.addEventListener('submit', (e) => handleSearchSessionsSubmit(e));
}

/**
 * Handle search sessions submit event
 *
 * @param e event that triggered submit
 */
function handleSearchSessionsSubmit(e) {
    e.preventDefault();
    const { value: gid } = document.getElementById('gameId');
    const { value: pid } = document.getElementById('playerId');
    const { value: date } = document.getElementById('date');
    const { checked: open } = document.querySelector('input[name="state"][value="open"]');
    const { checked: close } = document.querySelector('input[name="state"][value="close"]');

    const params = new URLSearchParams();
    if (gid) params.set('gid', gid);
    if (pid) params.set('pid', pid);
    if (date) params.set('date', date.replace(':', '_'));
    if (open) params.set('state', 'open');
    if (close) params.set('state', 'close');
    params.set('offset', "0");

    window.location.hash = `#sessions?${params}`;
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
    const url = `${constants.API_BASE_URL}${constants.SESSION_ROUTE}?${queryString}&token=${constants.TOKEN}`;
    handlerUtils.executeCommandWithResponse(url, response => handleGetSessionsResponse(response, mainContent, mainHeader));
}

/**
 * Handle get sessions response from the server
 *
 * @param response response from the server
 * @param mainContent main content of the page
 * @param mainHeader main header of the page
 */
function handleGetSessionsResponse(response, mainContent, mainHeader) {
    response.json().then(sessions => {
        if (sessions.length === 0) {
            query.set("offset", 0)
            window.location.hash = `#sessions?${handlerUtils.makeQueryString(query)}`
            alert("No sessions found.")
        }
        const [sessionsView, nextPrevView] = sessionHandlerViews.createGetSessionsView(sessions);
        mainContent.replaceChildren(sessionsView, nextPrevView);
        mainHeader.replaceChildren(menu.get("home"), menu.get("sessionSearch"))
    });
}

/**
 * Get session details by session id and display them in the main content area of the page
 *
 * @param mainContent
 * @param mainHeader
 */
function getSessionDetails(mainContent, mainHeader) {
    const url = `${constants.API_BASE_URL}${constants.SESSION_ID_ROUTE}?sid=${requestUtils.getParams()}&token=${constants.TOKEN}`;
    handlerUtils.executeCommandWithResponse(url, response => handleGetSessionDetailsResponse(response, mainContent, mainHeader));
}

/**
 * Handle get session details response from the server
 *
 * @param response response from the server
 * @param mainContent main content of the page
 * @param mainHeader main header of the page
 */
function handleGetSessionDetailsResponse(response, mainContent, mainHeader) {
    response.json().then(
        session => {
            const playerListView = sessionHandlerViews.createPlayerListView(session);
            const sessionDetailsView = sessionHandlerViews.createSessionDetailsViews(session, playerListView);
            mainContent.replaceChildren(sessionDetailsView);
            mainHeader.replaceChildren(menu.get("home"));
        }
    );
}

export default {
    searchSessions,
    getSessions,
    getSessionDetails,
};
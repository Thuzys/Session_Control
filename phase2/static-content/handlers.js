import menu from "./menuLinks.js";
import requestUtils from "./requestUtils.js";
import views from './viewsCreators.js';
import handlerUtils from "./handlerUtils.js";

const API_BASE_URL = "http://localhost:8080/"
const PLAYER_ROUTE = "players/player"
const SESSION_ROUTE = "sessions"
const SESSION_ID_ROUTE = SESSION_ROUTE + "/session"
const TOKEN = "e247758f-02b6-4037-bd85-fc245b84d5f2"

function getHome(mainContent, headerContent) {
    const [h1, form] = handlerUtils.createHomeView();
    form.onsubmit = (e) => handleHomeSubmit(e);
    headerContent.replaceChildren(menu.get("gameSearch"), menu.get("sessionSearch"));
    mainContent.replaceChildren(h1, form);
}

function handleHomeSubmit(e) {
    e.preventDefault();
    const pid = document.getElementById("pid");
    if (pid.value === "") {
        alert("Please enter a player id");
        return;
    }
    window.location.hash = `#playerDetails/${pid.value}`;
}

function searchSessions(mainContent, mainHeader) {
    const h1 = handlerUtils.createHeader();
    const formContent = handlerUtils.createFormContent();
    const form = views.form({}, ...formContent);
    mainContent.replaceChildren(h1, form);
    mainHeader.replaceChildren(menu.get("home"));
    form.addEventListener('submit', (e) => handleSearchSessionsSubmit(e));
}

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
    if (date) params.set('date', date);
    if (open) params.set('state', 'open');
    if (close) params.set('state', 'close');
    params.set('offset', "0");

    window.location.hash = `#sessions?${params}`;
}

function getSessions(mainContent, mainHeader) {
    const query = requestUtils.getQuery();
    const queryString = handlerUtils.makeQueryString(query);
    const url = `${API_BASE_URL}${SESSION_ROUTE}?${queryString}&token=${TOKEN}`;
    handlerUtils.executeCommandWithResponse(url, response => handleGetSessionsResponse(response, mainContent, mainHeader));
}

function handleGetSessionsResponse(response, mainContent, mainHeader) {
    response.json().then(sessions => {
        if (sessions.length === 0) {
            query.set("offset", 0)
            window.location.hash = `#sessions?${handlerUtils.makeQueryString(query)}`
            alert("No sessions found.")
        }
        const [sessionsView, nextPrevView] = handlerUtils.createGetSessionsView(sessions);
        mainContent.replaceChildren(sessionsView, nextPrevView);
        mainHeader.replaceChildren(menu.get("home"), menu.get("sessionSearch"))
    });
}

function getSessionDetails(mainContent, mainHeader) {
    const url = `${API_BASE_URL}${SESSION_ID_ROUTE}?sid=${requestUtils.getParams()}&token=${TOKEN}`;
    handlerUtils.executeCommandWithResponse(url, response => handleGetSessionDetailsResponse(response, mainContent, mainHeader));
}

function handleGetSessionDetailsResponse(response, mainContent, mainHeader) {
    response.json().then(
        session => {
            const playerListView = handlerUtils.createPlayerListView(session);
            const sessionDetailsView = handlerUtils.createSessionDetailsViews(session, playerListView);
            mainContent.replaceChildren(sessionDetailsView);
            mainHeader.replaceChildren(menu.get("home"));
        }
    );
}

function getPlayerDetails(mainContent, mainHeader) {
    const url = `${API_BASE_URL}${PLAYER_ROUTE}?pid=${requestUtils.getParams()}&token=${TOKEN}`;
    handlerUtils.executeCommandWithResponse(url, response => handleGetPlayerDetailsResponse(response, mainContent, mainHeader));
}

function handleGetPlayerDetailsResponse(response, mainContent, mainHeader) {
    response.json().then(player => {
        const playerDetailsView = handlerUtils.createPlayerDetailsView(player);
        mainContent.replaceChildren(playerDetailsView);
        mainHeader.replaceChildren(menu.get("home"), menu.get("sessionSearch"));
    });
}

export const handlers = {
    getHome,
    searchSessions,
    getPlayerDetails,
    getSessions,
    getSessionDetails
}

export default handlers

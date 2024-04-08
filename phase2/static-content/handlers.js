import menu from "./menuLinks.js";
import views from './viewsCreators.js';
import handlerUtils from "./handlerUtils.js";
import requestUtils from "./requestUtils.js";

const API_BASE_URL = "http://localhost:8080/"
const TOKEN = "e247758f-02b6-4037-bd85-fc245b84d5f2"
const LIMIT = 10
const PLAYER_ROUTE = "players/player"
const SESSION_ROUTE = "sessions"
const SESSION_ID_ROUTE = SESSION_ROUTE + "/session"
const GAME_ROUTE = "games"
const GAME_ID_ROUTE = `${GAME_ROUTE}/game`

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
    const h1 = handlerUtils.createHeader("Search Sessions: ");
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

/**
 * Search games by developer and/or genre(s)
 *
 * @param mainContent
 * @param mainHeader
 */
function searchGames(mainContent, mainHeader) {
    const header = handlerUtils.createHeader("Search Games by developer and/or Genre(s): ")
    const inputDev =
        views.input({
            id: "InputDev",
            type: "text",
            placeholder: "Insert Developer Name"
        })
    const inputGenres =
        views.input({
            id: "InputGenres",
            type: "text",
            placeholder: "Insert Genre(s)"
        })
    const searchButton = views.button({
        id: "SearchGamesButton",
        type: "submit",
    }, "Search")

    handlerUtils.updateGameSearchButton(
        searchButton,
        inputDev,
        inputGenres
    )

    const form = views.form(
        {},
        inputDev,
        inputGenres,
        searchButton,
    )

    form.onsubmit = (e) => handleSearchGamesSubmit(e)

    mainContent.replaceChildren(header, form)
    mainHeader.replaceChildren(menu.get("home"))
}

/**
 * Handle search games submit
 *
 * @param e
 */
function handleSearchGamesSubmit(e) {
    e.preventDefault()
    const inputDev = document.getElementById("InputDev")
    const inputGenres = document.getElementById("InputGenres")

    const params = new URLSearchParams()
    if (inputDev.value) params.set("dev", inputDev.value)
    if (inputGenres.value) params.set("genres", inputGenres.value)
    params.set('offset', "0")

    handlerUtils.changeHash(`games?${params}`)
}

/**
 * Get games
 *
 * @param mainContent
 * @param mainHeader
 */
function getGames(mainContent, mainHeader) {
    // const query = handlerUtils.makeQueryString(requestUtils.getQuery())
    const query = window.location.hash.split("?")[1]
    const url = `${API_BASE_URL}${GAME_ROUTE}?${query}&token=${TOKEN}`

    handlerUtils.executeCommandWithResponse(url, response => {
        handleGetGamesResponse(response, mainContent)
    })

    mainHeader.replaceChildren(menu.get("home"), menu.get("gameSearch")) // Será que posso voltar para o gameSearch?
}

/**
 * Handle get games response
 *
 * @param response
 * @param mainContent
 */
function handleGetGamesResponse(response, mainContent) {
    response.json().then(games => {
        const header = handlerUtils.createHeader("Games: ")
        const gameList = views.ul()
        games.forEach(game => {
                gameList.appendChild(
                    views.li(
                        views.a({
                                href: `#games/${game.gid}`},
                            game.name
                        )
                    )
                )
            }
        )

        const pagination = handlerUtils.createPagination(
            requestUtils.getQuery(),
            "games",
            games.length === LIMIT
        )

        mainContent.replaceChildren(header, gameList, pagination)
    })
}

/**
 * Get game details
 *
 * @param mainContent
 * @param mainHeader
 */
function getGameDetails(mainContent, mainHeader){
    const gameId = requestUtils.getParams()
    const url = `${API_BASE_URL}${GAME_ID_ROUTE}?gid=${gameId}&token=${TOKEN}`

    handlerUtils.executeCommandWithResponse(url, response => {
        handleGetGameDetailsResponse(response, mainContent)
    })

    mainHeader.replaceChildren(menu.get("home"), menu.get("sessionSearch")) // ?? não deveria ir para as sessions do game especifico?
}

/**
 * Handle game details response
 *
 * @param response
 * @param mainContent
 */
function handleGetGameDetailsResponse(response, mainContent) {
    response.json().then(game => {
        const header = handlerUtils.createHeader("Game Details: ")
        const div = views.div(
            {},
            views.h2({}, `${game.name}`),
            views.p({}, `Developer: ${game.dev}`),
            views.p({}, `Genres: ${game.genres.join(",")}`)
        )

        mainContent.replaceChildren(header, div)
    })
}

export const handlers = {
    getHome,
    searchGames,
    getGames,
    getGameDetails,
    searchSessions,
    getPlayerDetails,
    getSessions,
    getSessionDetails
}

export default handlers

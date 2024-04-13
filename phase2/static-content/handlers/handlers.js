import menu from "../navigation/menuLinks.js";
import views from '../views/viewsCreators.js';
import handlerUtils from "./handlerUtils/handlerUtils.js";
import requestUtils from "../utils/requestUtils.js";
import constants from "../constants/constants.js";
import handlerViews from "../views/handlerViews/handlerViews.js";

function getHome(mainContent, headerContent) {
    const [h1, form] = handlerViews.createHomeView();
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
    window.location.hash = `#players/${pid.value}`;
}

/**
 * Search games by developer and/or genre(s)
 *
 * @param mainContent
 * @param mainHeader
 */
function searchGames(mainContent, mainHeader) {
    const header = handlerViews.createHeader("Search Games by developer and/or Genre(s): ")
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
    const query = handlerUtils.makeQueryString(requestUtils.getQuery())
    const url = `${constants.API_BASE_URL}${constants.GAME_ROUTE}?${query}&token=${constants.TOKEN}`

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
        const header = handlerViews.createHeader("Games: ")
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

        const pagination = handlerViews.createPagination(
            requestUtils.getQuery(),
            "games",
            games.length === constants.LIMIT
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
    const url = `${constants.API_BASE_URL}${constants.GAME_ID_ROUTE}?gid=${gameId}&token=${constants.TOKEN}`

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
        const header = handlerViews.createHeader("Game Details: ")
        const div = views.div(
            {},
            views.h2({}, `${game.name}`),
            views.p({}, `Developer: ${game.dev}`),
            views.p({}, `Genres: ${game.genres.join(",")}`),
            handlerViews.createBackButtonView(),
            handlerViews.sessionsButtonView("Sessions", `sessions?gid=${game.gid}`)
        )

        mainContent.replaceChildren(header, div)
    })
}

const handlers = {
    getHome,
    searchGames,
    getGames,
    getGameDetails,
}

export default handlers

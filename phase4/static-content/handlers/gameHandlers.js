import gameHandlerViews from "../views/handlerViews/gameHandlerViews.js";
import sessionHandlers from "./sessionHandlers.js";
import handlerUtils from "./handlerUtils/handlerUtils.js";
import requestUtils from "../utils/requestUtils.js";
import constants from "../constants/constants.js"
import { fetcher } from "../utils/fetchUtils.js";
import handlerViews from "../views/handlerViews/handlerViews.js";

/**
 * Create game
 *
 * @param mainContent main content of the page
 */
function createGame(mainContent) {
    const container = gameHandlerViews.createCreateGameView()
    container.onsubmit = (e) => handleCreateGameSubmit(e, mainContent)
    mainContent.replaceChildren(container)
}

/**
 * Handle create game submit
 *
 * @param e event that triggered submit
 * @param mainContent main content of the page
 */
function handleCreateGameSubmit(e, mainContent) {
    e.preventDefault()
    const inputName = document.getElementById("InputName")
    const inputDev = document.getElementById("InputDev")
    const selectedGenresView = document.getElementById("ul")
    const genres = handlerUtils.childrenToString(selectedGenresView.children)
    const game = {
        name: inputName.value,
        dev: inputDev.value,
        genres: genres
    }
    const url = handlerUtils.createURL(constants.API_GAME_ROUTE)
    const token = sessionStorage.getItem('token')

    fetcher
        .post(url, game, token)
        .then(response => handleCreateGameResponse(response))
    mainContent.replaceChildren(handlerViews.createLoaderView());
}

/**
 * Handle create game response
 * @param response response of the create game request
 */
function handleCreateGameResponse(response) {
    handlerUtils.changeHash(`${constants.GAME_ROUTE}/${response.id}`)
}

/**
 * Search games by parameters: developer, genres and name
 *
 * @param mainContent main content of the page
 */
function searchGames(mainContent) {
    const container = gameHandlerViews.createSearchGamesView()
    container.onsubmit = (e) => handleSearchGamesSubmit(e)
    mainContent.replaceChildren(container)
}

/**
 * Handle search games submit
 *
 * @param e event that triggered submit
 */
function handleSearchGamesSubmit(e) {
    e.preventDefault()
    const inputName = document.getElementById("InputName")
    const inputDev = document.getElementById("InputDev")
    const selectedGenresView = document.getElementById("ul")

    const params = new URLSearchParams()
    if (inputName.value)
        params.set("name", inputName.value)
    if (inputDev.value)
        params.set("dev", inputDev.value)
    if (selectedGenresView.children && selectedGenresView.children.length > 0)
        params.set("genres", handlerUtils.childrenToString(selectedGenresView.children))

    params.set('offset', "0")

    handlerUtils.changeHash(`${constants.GAME_ROUTE}?${params}`)
}

/**
 * Get games by query
 *
 * @param mainContent main content of the page
 */
function getGames(mainContent) {
    const url = handlerUtils.createURL(constants.API_GAME_ROUTE, requestUtils.getQuery())
    const token = sessionStorage.getItem('token')
    fetcher
        .get(url, token)
        .then(response =>
            handleGetGamesResponse(response, mainContent)
        );
    mainContent.replaceChildren(handlerViews.createLoaderView());
}

/**
 * Handle get games response
 *
 * @param games list of games in the response
 * @param mainContent main content of the page
 */
function handleGetGamesResponse(games, mainContent) {
    const container = gameHandlerViews.createGetGameView(games)
    mainContent.replaceChildren(container)
}

/**
 * Get game details
 *
 * @param mainContent main content of the page
 */
function getGameDetails(mainContent){
    const gid = requestUtils.getParams();
    const route = handlerUtils.createRoute(constants.API_GAME_ROUTE, gid)
    const url = handlerUtils.createURL(route)
    const token = sessionStorage.getItem('token')

    fetcher
        .get(url, token)
        .then(response =>
            handleGetGameDetailsResponse(response, mainContent)
        );
    mainContent.replaceChildren(handlerViews.createLoaderView());
}

/**
 * Handle game details response
 *
 * @param game game in the response
 * @param mainContent main content of the page
 */
function handleGetGameDetailsResponse(game, mainContent) {
    const container = gameHandlerViews.createGameDetailsView(game, sessionHandlers.createSession)
    mainContent.replaceChildren(container)
}

const gameHandlers = {
    searchGames,
    getGames,
    getGameDetails,
    createGame
}

export default gameHandlers

import gameHandlerViews from "../views/handlerViews/gameHandlerViews.js";
import handlerUtils from "./handlerUtils/handlerUtils.js";
import menu from "../navigation/menuLinks.js";
import requestUtils from "../utils/requestUtils.js";
import constants from "../constants/constants.js"
import { fetcher } from "../utils/fetchUtils.js";

/**
 * Create game
 *
 * @param mainContent
 * @param mainHeader
 */
function createGame(mainContent, mainHeader) {
    const [
        header,
        form
    ] = gameHandlerViews.createCreateGameView()

    form.onsubmit = (e) => handleCreateGameSubmit(e)

    mainContent.replaceChildren(header, form)
    mainHeader.replaceChildren(menu.get("home"), menu.get("gameSearch"))
}

/**
 * Handle create game submit
 *
 * @param e event that triggered submit
 */
function handleCreateGameSubmit(e) {
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

    const url = `${constants.API_BASE_URL}${constants.GAME_ROUTE}`

    fetcher
        .post(url, game, constants.TOKEN)
        .then(response => handleCreateGameResponse(response))
}

function handleCreateGameResponse(response) {
    alert(response.msg)
}

/**
 * Search games by developer and/or genre(s)
 *
 * @param mainContent
 * @param mainHeader
 */
function searchGames(mainContent, mainHeader) {
    const [
        header,
        form,
    ] = gameHandlerViews.createSearchGamesView()

    form.onsubmit = (e) => handleSearchGamesSubmit(e)

    mainContent.replaceChildren(header, form)
    mainHeader.replaceChildren(menu.get("playerSearch"), menu.get("home"), menu.get("createGame"), menu.get("sessionSearch"))
}

/**
 * Handle search games submit
 *
 * @param e
 * @param selectedGenres
 */
function handleSearchGamesSubmit(e, selectedGenres) {
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
    const url = `${constants.API_BASE_URL}${constants.GAME_ROUTE}?${query}`

    fetcher
        .get(url, constants.TOKEN)
        .then(response =>
            handleGetGamesResponse(response, mainContent)
        );

    mainHeader.replaceChildren(
        menu.get("playerSearch"), menu.get("home"),
        menu.get("gameSearch"), menu.get("sessionSearch")
    );
}

/**
 * Handle get games response
 *
 * @param games
 * @param mainContent
 */
function handleGetGamesResponse(games, mainContent) {
    const [
        header,
        gameList,
        pagination
    ] = gameHandlerViews.createGetGameView(games)

    mainContent.replaceChildren(header, gameList, pagination)
}

/**
 * Get game details
 *
 * @param mainContent
 * @param mainHeader
 */
function getGameDetails(mainContent, mainHeader){
    const gameId = requestUtils.getParams()
    const url = `${constants.API_BASE_URL}${constants.GAME_ID_ROUTE}${gameId}`

    fetcher
        .get(url, constants.TOKEN)
        .then(response =>
            handleGetGameDetailsResponse(response, mainContent)
        );

    mainHeader.replaceChildren(menu.get("playerSearch"), menu.get("home"), menu.get("sessionSearch"))
}

/**
 * Handle game details response
 *
 * @param game
 * @param mainContent
 */
function handleGetGameDetailsResponse(game, mainContent) {
    const [header, div] = gameHandlerViews.createGameDetailsView(game)
    mainContent.replaceChildren(header, div)
}

const gameHandlers = {
    createGame,
    searchGames,
    getGames,
    getGameDetails,
}

export default gameHandlers

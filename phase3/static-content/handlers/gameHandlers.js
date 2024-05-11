import gameHandlerViews from "../views/handlerViews/gameHandlerViews.js";
import handlerUtils from "./handlerUtils/handlerUtils.js";
import requestUtils from "../utils/requestUtils.js";
import constants from "../constants/constants.js"
import { fetcher } from "../utils/fetchUtils.js";
import views from "../views/viewsCreators.js";

/**
 * Search games by parameters: developer, genres and name
 *
 * @param mainContent main content of the page
 */
function searchGames(mainContent) {
    const container = views.div({class: "player-details-container"});
    const [
        header,
        form,
    ] = gameHandlerViews.createSearchGamesView()

    form.onsubmit = (e) => handleSearchGamesSubmit(e)

    container.replaceChildren(header, form)
    mainContent.replaceChildren(container)
}

/**
 * Handle search games submit
 *
 * @param e event that triggered submit
 * @param selectedGenres selected genres
 */
function handleSearchGamesSubmit(e, selectedGenres) {
    e.preventDefault()
    const inputDev = document.getElementById("InputDev")
    const selectedGenresView = document.getElementById("ul")

    const params = new URLSearchParams()
    if (inputDev.value)
        params.set("dev", inputDev.value)
    if (selectedGenresView.children && selectedGenresView.children.length > 0)
        params.set("genres", handlerUtils.childrenToString(selectedGenresView.children))

    params.set('offset', "0")

    handlerUtils.changeHash(`games?${params}`)
}

/**
 * Get games by query
 *
 * @param mainContent main content of the page
 */
function getGames(mainContent) {
    const query = handlerUtils.makeQueryString(requestUtils.getQuery())
    const url = `${constants.API_BASE_URL}${constants.GAME_ROUTE}?${query}`
    fetcher
        .get(url, constants.TOKEN)
        .then(response =>
            handleGetGamesResponse(response, mainContent)
        );
}

/**
 * Handle get games response
 *
 * @param games list of games in the response
 * @param mainContent main content of the page
 */
function handleGetGamesResponse(games, mainContent) {
    const container = views.div({class: "player-details-container"});
    const [
        header,
        hr,
        gameList,
        pagination
    ] = gameHandlerViews.createGetGameView(games)
    container.replaceChildren(header, hr, gameList, pagination)
    mainContent.replaceChildren(container)
}

/**
 * Get game details
 *
 * @param mainContent main content of the page
 */
function getGameDetails(mainContent){
    const gameId = requestUtils.getParams()
    const url = `${constants.API_BASE_URL}${constants.GAME_ID_ROUTE}${gameId}`
    fetcher
        .get(url, constants.TOKEN)
        .then(response =>
            handleGetGameDetailsResponse(response, mainContent)
        );
}

/**
 * Handle game details response
 *
 * @param game game in the response
 * @param mainContent main content of the page
 */
function handleGetGameDetailsResponse(game, mainContent) {
    const container = views.div({class: "player-details-container"});
    const [header, hr, div] = gameHandlerViews.createGameDetailsView(game)
    container.replaceChildren(header, hr, div)
    mainContent.replaceChildren(container)
}

const gameHandlers = {
    searchGames,
    getGames,
    getGameDetails,
}

export default gameHandlers

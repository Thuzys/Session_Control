import gameHandlerUtils from "./handlerUtils/gameHandlerUtils.js";
import handlerUtils from "./handlerUtils/handlerUtils.js";
import views from "../views/viewsCreators.js";
import menu from "../navigation/menuLinks.js";
import requestUtils from "../utils/requestUtils.js";
import constants from "../constants/constants.js"

/**
 * Search games by developer and/or genre(s)
 *
 * @param mainContent
 * @param mainHeader
 */
function searchGames(mainContent, mainHeader) {
    const [
        header,
        inputDev,
        inputGenres,
        searchButton
    ] = gameHandlerUtils.createSearchGamesView()

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

    form.onsubmit = (e) => gameHandlerUtils.handleSearchGamesSubmit(e)

    mainContent.replaceChildren(header, form)
    mainHeader.replaceChildren(menu.get("home"))
}

/**
 * Get games
 *
 * @param mainContent
 * @param mainHeader
 */
function getGames(mainContent, mainHeader) {
    const query = handlerUtils.makeQueryString(requestUtils.getQuery())
    // const query = window.location.hash.split("?")[1]
    const url = `${constants.API_BASE_URL}${constants.GAME_ROUTE}?${query}&token=${constants.TOKEN}`

    handlerUtils.executeCommandWithResponse(url, response => {
        gameHandlerUtils.handleGetGamesResponse(response, mainContent)
    })

    mainHeader.replaceChildren(menu.get("home"), menu.get("gameSearch")) // Será que posso voltar para o gameSearch?
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
        gameHandlerUtils.handleGetGameDetailsResponse(response, mainContent)
    })

    mainHeader.replaceChildren(menu.get("home"), menu.get("sessionSearch")) // ?? não deveria ir para as sessions do game especifico?
}

const gameHandlers = {
    searchGames,
    getGames,
    getGameDetails,
}
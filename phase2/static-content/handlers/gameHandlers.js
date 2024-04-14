import gameHandlerViews from "../views/handlerViews/gameHandlerViews.js";
import handlerUtils from "./handlerUtils/handlerUtils.js";
import menu from "../navigation/menuLinks.js";
import requestUtils from "../utils/requestUtils.js";
import constants from "../constants/constants.js"
import genres from "./handlerUtils/gameGenres.js"

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
    mainHeader.replaceChildren(menu.get("home"))
}

/**
<<<<<<< HEAD
 * Handle search games submit
 *
 * @param e
 * @param selectedGenres
 */
function handleSearchGamesSubmit(e, selectedGenres) {
    e.preventDefault()
    const inputDev = document.getElementById("InputDev")
    const selectedGenresView = document.getElementById("SelectedGenresView")

    const params = new URLSearchParams()
    if (inputDev.value)
        params.set("dev", inputDev.value)
    if (selectedGenresView.children.length !== 0)
        params.set("genres", handlerUtils.childrenToString(selectedGenresView.children))

    params.set('offset', "0")

    handlerUtils.changeHash(`games?${params}`)
}

/**
=======
>>>>>>> 27cbd9c2c5b3855721d08d61973a3229dff47fae
 * Get games
 *
 * @param mainContent
 * @param mainHeader
 */
function getGames(mainContent, mainHeader) {
    // const query = handlerUtils.makeQueryString(requestUtils.getQuery())
    const query = window.location.hash.split("?")[1]
    const url = `${constants.API_BASE_URL}${constants.GAME_ROUTE}?${query}&token=${constants.TOKEN}`

    handlerUtils.executeCommandWithResponse(url, response => {
        handleGetGamesResponse(response, mainContent)
    })

    mainHeader.replaceChildren(menu.get("home"), menu.get("gameSearch"))
}

/**
<<<<<<< HEAD
 * Handle get games response
 *
 * @param response
 * @param mainContent
 */
function handleGetGamesResponse(response, mainContent) {
    response.json().then(games => {
        const [
            header,
            gameList,
            pagination
        ] = gameHandlerViews.createGetGameView(games)

        mainContent.replaceChildren(header, gameList, pagination)
    })
}

/**
=======
>>>>>>> 27cbd9c2c5b3855721d08d61973a3229dff47fae
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

    mainHeader.replaceChildren(menu.get("home"), menu.get("sessionSearch"), menu.get("gameSearch"))
}

/**
 * Handle game details response
 *
 * @param response
 * @param mainContent
 */
function handleGetGameDetailsResponse(response, mainContent) {
    response.json().then(game => {
        const [header, div] = gameHandlerViews.createGameDetailsView(game)
        mainContent.replaceChildren(header, div)
    })
}

const gameHandlers = {
    searchGames,
    getGames,
    getGameDetails,
}

export default gameHandlers

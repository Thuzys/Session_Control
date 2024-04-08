import handlerUtils from "./handlerUtils";
import views from "./viewsCreators";
import requestUtils from "./requestUtils";
import constants from "./constants";

function createSearchGamesView() {
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

    return [header, inputDev, inputGenres, searchButton]
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
        ] = createGetGameView(games)

        mainContent.replaceChildren(header, gameList, pagination)
    })
}

/**
 * Create get game view
 *
 * @param games
 * @returns {any[]}
 */
function createGetGameView(games) {
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
        games.length === constants.LIMIT
    )

    return [header, gameList, pagination]
}

/**
 * Handle game details response
 *
 * @param response
 * @param mainContent
 */
function handleGetGameDetailsResponse(response, mainContent) {
    response.json().then(game => {
        const [header, div] = createGameDetailsView(game)
        mainContent.replaceChildren(header, div)
    })
}

/**
 * Create game details view
 *
 * @param game
 * @returns {any[]}
 */
function createGameDetailsView(game) {
    const header = handlerUtils.createHeader("Game Details: ")
    const div = views.div(
        {},
        views.h2({}, `${game.name}`),
        views.p({}, `Developer: ${game.dev}`),
        views.p({}, `Genres: ${game.genres.join(",")}`),
        handlerUtils.createBackButtonView()
    )
    return [header, div]
}


const gameHandlerUtils = {
    createSearchGamesView,
    handleSearchGamesSubmit,
    handleGetGamesResponse,
    handleGetGameDetailsResponse
}

export default gameHandlerUtils;

import handlerViews from "./handlerViews.js";
import views from "../viewsCreators.js";
import requestUtils from "../../utils/requestUtils.js";
import constants from "../../constants/constants.js";
import genres from "../../handlers/handlerUtils/gameGenres.js";

/**
 * Create search games view
 *
 * @returns {any[]}
 */
function createSearchGamesView() {
    const genresValues = Object.values(genres)

    const header = handlerViews.createHeader("Search Games by developer and/or Genre(s): ")

    const inputDev = views.input({
        id: "InputDev",
        type: "text",
        placeholder: "Insert Developer Name"
    })

    const addGenresButton = views.button({
        id: "AddGenresButton",
        type: "button",
        class: "submit-button",
    }, "Add")

    const genresList = views.datalist({
        id: "GenresList"
    })

    genresValues.forEach(genre => {
        genresList.appendChild(views.option({value: genre}))
    })

    const inputGenres = views.input({
        id: "InputGenres",
        type: "text",
        placeholder: "Insert Genre(s)",
        list: "GenresList"
    })

    const selectedGenresView = views.ul()

    const searchGamesButton = views.button({
        id: "SearchGamesButton",
        type: "submit",
    }, "Search")

    addGenresButton.addEventListener("click", () => {
        createGenresListener(selectedGenresView, inputGenres, genresValues)
    })

    //updateGameSearchButton(searchGamesButton, inputDev, selectedGenresView)

    const form = views.form(
        {},
        inputDev,
        views.p(),
        genresList,
        inputGenres,
        addGenresButton,
        selectedGenresView,
        views.p(),
        searchGamesButton
    )

    return [header, form]
}

/**
 * Toggle search button
 *
 * @param searchGamesButton
 * @param inputDev
 * @param selectedGenresView
 */
function toggleSearchButton(searchGamesButton, inputDev, selectedGenresView) {
    searchGamesButton.disabled = inputDev.value.trim() === "" && selectedGenresView.children.length === 0;
}

/**
 * Update game search button
 *
 * @param searchGamesButton
 * @param inputDev
 * @param selectedGenresView
 */
function updateGameSearchButton(searchGamesButton, inputDev, selectedGenresView) {
    const update = () => toggleSearchButton(searchGamesButton, inputDev, selectedGenresView)
    inputDev.addEventListener('input', update)
    selectedGenresView.addEventListener('DOMNodeInserted', update)
    selectedGenresView.addEventListener('DOMNodeRemoved', update)
}

/**
 * Create genres listener
 *
 * @param selectedGenresView
 * @param inputGenres
 * @param genresValues
 */
function createGenresListener(selectedGenresView, inputGenres, genresValues) {
    const selectedGenre = document.getElementById("InputGenres").value.trim()
    if (selectedGenre && !ulHasItem(selectedGenre, selectedGenresView.children) && genresValues.includes(selectedGenre)) {
        inputGenres.value = ""

        const removeButton = views.button({
            type: "button",
            class: "submit-button",
        }, "X")

        removeButton.onclick = () => {
            selectedGenresView.removeChild(genreView)
        }

        const genreView = views.div({}, selectedGenre)
        genreView.appendChild(removeButton)
        selectedGenresView.appendChild(genreView)
    }
}

/**
 * Check if ul has item
 *
 * @param item
 * @param children
 * @returns {boolean}
 */
function ulHasItem(item, children) {
    return Array.from(children).some(child => {
        if(!child.childNodes || child.childNodes.length === 0) return false
        return Array.from(child.childNodes).some(node => node.nodeType === Node.TEXT_NODE && node.data === item)
    })
}

/**
 * Create get game view
 *
 * @param games
 * @returns {any[]}
 */
function createGetGameView(games) {
    const header = handlerViews.createHeader("Games: ")
    const gameList = views.ul()
    games.forEach(game => {
            gameList.appendChild(
                views.li(
                    ...handlerViews.hrefConstructor("#games", game.gid, game.name)
                )
            )
        }
    )

    const pagination = handlerViews.createPagination(
        requestUtils.getQuery(),
        "games",
        games.length === constants.LIMIT
    )

    return [header, gameList, pagination]
}

/**
 * Create game details view
 *
 * @param game
 * @returns {any[]}
 */
function createGameDetailsView(game) {
    const header = handlerViews.createHeader("Game Details: ")
    const div = views.div(
        {},
        views.h2({}, `${game.name}`),
        views.p({}, `Developer: ${game.dev}`),
        views.p({}, `Genres: ${game.genres.join(",")}`),
        handlerViews.createBackButtonView(),
        handlerViews.sessionsButtonView("Sessions", `sessions?gid=${game.gid}&offset=0`)
    )
    return [header, div]
}

const gameHandlerViews = {
    createSearchGamesView,
    createGetGameView,
    createGameDetailsView
}

export default gameHandlerViews

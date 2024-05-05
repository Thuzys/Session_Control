import handlerViews from "./handlerViews.js";
import views from "../viewsCreators.js";
import requestUtils from "../../utils/requestUtils.js";
import constants from "../../constants/constants.js";
import genres from "../../handlers/handlerUtils/gameGenres.js";
import handlerUtils from "../../handlers/handlerUtils/handlerUtils.js";

const genresValues = Object.values(genres)

/**
 * Create create game view
 *
 * @returns {any[]}
 */
function createCreateGameView() {
    const header = handlerViews.createHeader("Create Game: ")

    const [
        addGenresButton,
        genresList,
        inputGenres,
        selectedGenresView
    ] = createGenresView()

    const form = views.form(
        {},
        views.input({
            id: "InputName",
            type: "text",
            placeholder: "Insert Game Name"
        }),
        views.p(),
        views.input({
            id: "InputDev",
            type: "text",
            placeholder: "Insert Developer Name"
        }),
        views.p(),
        genresList,
        inputGenres,
        addGenresButton,
        selectedGenresView,
        views.p(),
        views.button({
            type: "submit",
        }, "Create")
    )

    return [header, form]
}

/**
 * Create search games view
 *
 * @returns {any[]}
 */
function createSearchGamesView() {
    const header = handlerViews.createHeader("Search Games by developer and/or genre(s): ")

    const inputName = views.input({
        id: "InputName",
        type: "text",
        placeholder: "Insert Game Name"
    })

    const inputDev = views.input({
        id: "InputDev",
        type: "text",
        placeholder: "Insert Developer Name"
    })

    const searchGamesButton = views.button({
        id: "SearchGamesButton",
        type: "submit",
    }, "Search")

    const createGameButton = views.button({
        id: "CreateGameButton",
        type: "button",
    }, "Create Game")

    createGameButton.onclick = () => {
        handlerUtils.changeHash("createGame")
    }

    const [
        addGenresButton,
        genresList,
        inputGenres,
        selectedGenresView
    ] = createGenresView()

    // updateGameSearchButton(searchGamesButton, inputDev, selectedGenresView)

    const form = views.form(
        {},
        inputName,
        views.p(),
        inputDev,
        views.p(),
        genresList,
        inputGenres,
        addGenresButton,
        selectedGenresView,
        views.p(),
        searchGamesButton,
        createGameButton
    )

    return [header, form]
}

function createGenresView() {
    const addGenresButton = views.button({
        id: "AddGenresButton",
        type: "button",
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

    addGenresButton.addEventListener("click", () => {
        createGenresListener(selectedGenresView, inputGenres, genresValues)
    })

    return [addGenresButton, genresList, inputGenres, selectedGenresView]
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
    createGameDetailsView,
    createCreateGameView
}

export default gameHandlerViews

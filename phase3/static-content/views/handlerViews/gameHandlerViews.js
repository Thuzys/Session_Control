import handlerViews from "./handlerViews.js";
import views from "../viewsCreators.js";
import requestUtils from "../../utils/requestUtils.js";
import constants from "../../constants/constants.js";
import genres from "../../handlers/handlerUtils/gameGenres.js";
import sessionHandlers from "../../handlers/sessionHandlers.js";

/**
 * Create search games view
 *
 * @returns {any[]} search games view
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
        class: "general-button",
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
 * @param searchGamesButton search games button
 * @param inputDev input developer
 * @param selectedGenresView selected genres view
 */
function toggleSearchButton(searchGamesButton, inputDev, selectedGenresView) {
    searchGamesButton.disabled = inputDev.value.trim() === "" && selectedGenresView.children.length === 0;
}

/**
 * Update game search button
 *
 * @param searchGamesButton search games button
 * @param inputDev input developer
 * @param selectedGenresView selected genres view
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
 * @param selectedGenresView selected genres view
 * @param inputGenres input genres
 * @param genresValues genres values
 */
function createGenresListener(selectedGenresView, inputGenres, genresValues) {
    const selectedGenre = document.getElementById("InputGenres").value.trim()
    if (selectedGenre && !ulHasItem(selectedGenre, selectedGenresView.children) && genresValues.includes(selectedGenre)) {
        inputGenres.value = ""

        const removeButton = views.button({
            type: "button",
            class: "remove-button"
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
 * @param item item to check
 * @param children ul children
 * @returns {boolean} true if ul has item, false otherwise
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
 * @param games games to display
 * @returns {any[]} game view
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
 * @param game game to display
 * @returns {any[]} game details view
 */
function createGameDetailsView(game) {
    const header = handlerViews.createHeader("Game Details: ")

    const createSessionButton = views.button({type: "button", class: "general-button"}, "Create Session");
    createSessionButton.addEventListener('click', () => {
        sessionHandlers.createSession(
            document.getElementById("mainContent"),
            document.getElementById("mainHeader"),
            game.gid,
            game.name
        );
    });

    const div = views.div(
        {},
        views.h2({}, `${game.name}`),
        views.p({}, `Developer: ${game.dev}`),
        views.p({}, `Genres: ${game.genres.join(",")}`),
        handlerViews.createBackButtonView(),
        handlerViews.hrefButtonView("Sessions",
            `${constants.SESSION_ROUTE}?gid=${game.gid}&offset=0`),
        createSessionButton,
    )
    return [header, div]
}

const gameHandlerViews = {
    createSearchGamesView,
    createGetGameView,
    createGameDetailsView
}

export default gameHandlerViews

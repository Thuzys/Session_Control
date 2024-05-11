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

    const header = handlerViews.createHeader("Search Games")
    const hr = views.hr({class:"w3-opacity"})

    const inputDev = views.input({
        id: "InputDev",
        type: "text",
        placeholder: "Insert Developer Name"
    })

    const addGenresButton = views.button({
        id: "AddGenresButton",
        type: "button",
        class: "general-button",
    }, "Add")

    const genresHeader = views.h4({class:"w3-wide"}, "Genres Selected:")

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

    const selectedGenresView = views.ul({class: "w3-ul w3-border w3-center w3-hover-shadow"})

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
        hr,
        inputDev,
        views.p(),
        genresList,
        inputGenres,
        views.p({class:"line-height: 1px"}),
        addGenresButton,
        genresHeader,
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

        const p = views.p({class: "progress-bar-text"}, selectedGenre)

        p.onclick = () => {
            selectedGenresView.removeChild(p)
        }
        selectedGenresView.appendChild(p)
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
    const hr = views.hr({class:"w3-opacity"})
    const gameList = views.ul({class: "w3-ul w3-border w3-center w3-hover-shadow"})
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

    return [header, hr, gameList, pagination]
}

/**
 * Create game details view
 *
 * @param game game to display
 * @returns {any[]} game details view
 */
function createGameDetailsView(game) {
    const header = handlerViews.createHeader("Game Details: ")
    const hr = views.hr({class:"w3-opacity"})

    const createSessionButton = views.button({type: "button", class: "general-button"}, "Create Session");
    createSessionButton.addEventListener('click', () => {
        sessionHandlers.createSession(
            document.getElementById("mainContent"),
            game.gid,
            game.name
        );
    });

    const div = views.div(
        {},
        views.h2({class: "w3-wide blue-letters centered"}, game.name),
        views.ul({class: "w3-ul w3-border w3-center w3-hover-shadow"},
            views.li(views.h4({class: "w3-wide blue-letters"}, "Developer")),
            views.li(game.dev),
            views.li(views.h4({class: "w3-wide blue-letters"}, "Genres")),
            views.li(game.genres.join(","))
        ),
        handlerViews.createBackButtonView(),
        views.p(),
        handlerViews.hrefButtonView("Sessions",
            `${constants.SESSION_ROUTE}?gid=${game.gid}&offset=0`),
        views.p(),
        createSessionButton,
    )
    return [header, hr, div]
}

const gameHandlerViews = {
    createSearchGamesView,
    createGetGameView,
    createGameDetailsView
}

export default gameHandlerViews

import handlerViews from "./handlerViews.js";
import views from "../viewsCreators.js";
import requestUtils from "../../utils/requestUtils.js";
import constants from "../../constants/constants.js";
import genres from "../../handlers/handlerUtils/gameGenres.js";
import handlerUtils from "../../handlers/handlerUtils/handlerUtils.js";
import sessionHandlers from "../../handlers/sessionHandlers.js";

const genresValues = Object.values(genres)

/**
 * Create create game view
 *
 * @returns {any[]}
 */
function createCreateGameView() {
    const header = handlerViews.createHeader("Create Game: ")

    const inputName = handlerViews.createLabeledInput("InputName", "Insert Game Name")
    const inputDev = handlerViews.createLabeledInput("InputDev", "Insert Developer Name")
    const submitButton = views.button({
        type: "submit",
        disabled: true,
        class: "general-button"
    }, "Create")

    const toggleCreateGameButton = () => {
        handlerViews.toggleButtonState(
            submitButton,
            !inputName.value.trim() || !inputDev.value.trim() || selectedGenresView.children.length === 0,
        )
    }

    handlerViews.addToggleEventListeners(
        toggleCreateGameButton,
        inputName,
        inputDev
    )

    const [
        addGenresButton,
        genresList,
        inputGenres,
        selectedGenresView,
    ] = createGenresView(toggleCreateGameButton)

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
        submitButton
    )

    return [header, form]
}


/**
 * Create search games view
 *
 * @returns {any[]}
 */
function createSearchGamesView() {
    const header = handlerViews.createHeader("Search Games: ")
    const inputName = handlerViews.createLabeledInput("InputName", "Insert Game Name")
    const inputDev = handlerViews.createLabeledInput("InputDev", "Insert Developer Name")
    const searchGamesButton =
        views.button({
            id: "SearchGamesButton",
            type: "submit",
            disabled: true,
            class: "general-button"
        }, "Search")
    const createGameButton =
        views.button({
            id: "CreateGameButton",
            type: "button",
            class: "general-button",
        }, "Create Game")

    createGameButton.onclick = () => {
        handlerUtils.changeHash("createGame")
    }

    const toggleSearchGamesButton = () => {
        handlerViews.toggleButtonState(
            searchGamesButton,
            !inputName.value.trim() && !inputDev.value.trim() && selectedGenresView.children.length === 0
        )
    }

    handlerViews.addToggleEventListeners(
        toggleSearchGamesButton,
        inputName,
        inputDev
    )

    const [
        addGenresButton,
        genresList,
        inputGenres,
        selectedGenresView
    ] = createGenresView(toggleSearchGamesButton)

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

/**
 * Create genres view
 *
 * @returns {*[]}
 */
function createGenresView(toggleButton) {
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

    addGenresButton.addEventListener("click", () => {
        createGenresListener(selectedGenresView, inputGenres, genresValues, toggleButton)
    })

    return [addGenresButton, genresList, inputGenres, selectedGenresView]
}

/**
 * Create genres listener
 *
 * @param selectedGenresView
 * @param inputGenres
 * @param genresValues
 * @param toggleButton
 */
function createGenresListener(selectedGenresView, inputGenres, genresValues, toggleButton) {
    const selectedGenre = document.getElementById("InputGenres").value.trim()
    if (selectedGenre && !handlerViews.ulHasItem(selectedGenre, selectedGenresView.children) && genresValues.includes(selectedGenre)) {
        inputGenres.value = ""

        const removeButton = views.button({
            type: "button",
            class: "remove-button"
        }, "X")

        removeButton.onclick = () => {
            selectedGenresView.removeChild(genreView)
            toggleButton()
        }

        const genreView = views.div({}, selectedGenre)
        genreView.appendChild(removeButton)
        selectedGenresView.appendChild(genreView)

        toggleButton()
    }
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
    createGameDetailsView,
    createCreateGameView
}

export default gameHandlerViews

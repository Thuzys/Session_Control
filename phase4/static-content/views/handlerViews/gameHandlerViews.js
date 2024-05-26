import handlerViews from "./handlerViews.js";
import views from "../viewsCreators.js";
import requestUtils from "../../utils/requestUtils.js";
import constants from "../../constants/constants.js";
import handlerUtils from "../../handlers/handlerUtils/handlerUtils.js";
import sessionHandlers from "../../handlers/sessionHandlers.js";

/**
 * Create create game view
 *
 * @returns {HTMLElement} create game view
 */
function createCreateGameView() {
    const container = views.div({class: "player-details-container"})
    const header = handlerViews.createHeader("Create Game: ")
    const genresHeader = views.h4({class:"w3-wide centered"}, "Genres Selected")
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
        views.p(),
        addGenresButton,
        genresHeader,
        selectedGenresView,
        views.p(),
        submitButton
    )

    container.replaceChildren(header, form)
    return container
}


/**
 * Create search games view
 *
 * @returns {HTMLElement} search games view
 */
function createSearchGamesView() {
    const container = views.div({class: "player-details-container"});
    const header = handlerViews.createHeader("Search Games")
    const genresHeader = views.h4({class:"w3-wide centered"}, "Genres Selected")
    const hr = views.hr({class:"w3-opacity"})
    const inputName = handlerViews.createLabeledInput("InputName", "Insert Game Name")
    const inputDev = handlerViews.createLabeledInput("InputDev", "Insert Developer Name")
    const searchGamesButton =
        views.button(
            {
                id: "SearchGamesButton",
                type: "submit",
                disabled: true,
                class: "general-button"
            },
            "Search",
        )
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
        hr,
        inputName,
        views.p(),
        inputDev,
        views.p(),
        genresList,
        inputGenres,
        views.p(),
        addGenresButton,
        genresHeader,
        selectedGenresView,
        views.p(),
        searchGamesButton,
        views.p(),
        createGameButton
    )

    container.replaceChildren(header, form)
    return container
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
        class: "general-button",
    }, "Add")

    const genresList = views.datalist({
        id: "GenresList"
    })
    const genresValues = JSON.parse(sessionStorage.getItem('genres'))
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

        const p = views.p({class: "progress-bar-text"}, selectedGenre)

        p.onclick = () => {
            selectedGenresView.removeChild(p)
            toggleButton()
        }
        selectedGenresView.appendChild(p)

        selectedGenresView.appendChild(p)

        toggleButton()
    }
}

/**
 * Create get game view
 *
 * @param games games to display
 * @returns {HTMLElement} get game view
 */
function createGetGameView(games) {
    const container = views.div({class: "player-details-container"});
    const header = handlerViews.createHeader("Games")
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

    container.replaceChildren(header, hr, gameList, pagination)
    return container
}

/**
 * Create game details view
 *
 * @param game game to display
 * @returns {HTMLElement} game details view
 */
function createGameDetailsView(game) {
    const container = views.div({class: "player-details-container"});
    const header = handlerViews.createHeader("Game Details")
    const hr = views.hr({class:"w3-opacity"})

    const createSessionButton = views.button({type: "button", class: "general-button"}, "Create Session");
    createSessionButton.addEventListener('click', () => {
        sessionHandlers.createSession(
            document.getElementById("mainContent"),
            game.gid,
            game.name
        );
    });

    const genresList = views.ul({
        id: "GenresList"
    })

    game.genres.forEach(genre => {
        genresList.appendChild(views.li(genre))
    })

    const div = views.div(
        {},
        views.h2({class: "w3-wide blue-letters centered"}, game.name),
        views.ul({class: "w3-ul w3-border w3-center w3-hover-shadow"},
            views.li(views.div({},views.h4({class: "w3-wide blue-letters"}, "Developer"), views.li(game.dev))),
            views.li(views.div({}, views.h4({class: "w3-wide blue-letters"}, "Genres"), genresList)),
        ),
        views.p(),
        handlerViews.createBackButtonView(),
        views.p(),
        handlerViews.hrefButtonView("Sessions",
            `${constants.SESSION_ROUTE}?gid=${game.gid}&offset=0`),
        views.p(),
        createSessionButton
    )
    container.replaceChildren(header, hr, div)
    return container
}

const gameHandlerViews = {
    createSearchGamesView,
    createGetGameView,
    createGameDetailsView,
    createCreateGameView
}

export default gameHandlerViews

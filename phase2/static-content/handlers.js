import views from './viewsCreators.js'
import RequestUtils from "./requestUtils.js"
import handlerUtils from "./handlerUtils.js"

const API_BASE_URL = "http://localhost:8080"
const TOKEN = "e247758f-02b6-4037-bd85-fc245b84d5f2"

const GAME_ROUTE = "/games"
const GAME_ID_ROUTE = `${GAME_ROUTE}/game`

function executeCommandWithResponse(url, responseHandler) {
    fetch(url)
        .then(response => {
            if(handlerUtils.isOkResponse(response)) {
                responseHandler(response)
            } else {
                response.text().then(text => alert("Error fetching data: " + text));
            }
        })
}

function getHome(mainContent) {
    const h1 = document.createElement("h1")
    const text = document.createTextNode("Home")
    h1.appendChild(text)
    mainContent.replaceChildren(h1)
}

function gameSearchHandler(mainContent) {
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
            placeholder: "Insert Genre(s) separated by comma and no spaces"
        })
    const searchButton = views.button({
        id: "SearchGamesButton",
        type: "submit",
    }, "Search")

    handlerUtils.updateSearchButton(
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

    form.onsubmit = (e) => {
        e.preventDefault()
        const hash = "#games?"
        const params = new URLSearchParams()
        if(inputDev.value) params.set("dev", inputDev.value)
        if(inputGenres.value) params.set("genres", inputGenres.value)

        window.location.hash = `${hash}${params.toString()}`
    }

    mainContent.replaceChildren(form)
}

function getGamesHandler(mainContent) {
    const url = `${API_BASE_URL}${GAME_ROUTE}?${RequestUtils.getQuery()}&token=${TOKEN}`;

    executeCommandWithResponse(url, response => {
        response.json().then(games => {
            const gameList = views.ul();

            games.forEach(game => {
                const button = views.button({}, "View Details")
                button.addEventListener("click", () => { handlerUtils.changeHash(`games/${game.gid}`) })

                gameList.appendChild(
                    views.li(
                        views.h2({}, game.name),
                        button
                    )
                )
            })

            mainContent.replaceChildren(gameList);
        })
    })
}

function getGameDetailsHandler(mainContent){
    const pathParams = RequestUtils.getPathParams();
    const gameId = pathParams[0];
    const url = `${API_BASE_URL}${GAME_ID_ROUTE}?gid=${gameId}&token=${TOKEN}`;

    executeCommandWithResponse(url, response => {
        response.json().then(game => {
            const div = views.div(
                {},
                views.h1({}, `${game.name}`),
                views.p({}, `Developer: ${game.dev}`),
                views.p({}, `Genres: ${game.genres.join(",")}`)
            )

            mainContent.replaceChildren(div);
        })
    })
}

export const handlers = {
    getHome,
    gameSearchHandler,
    getGamesHandler,
    getGameDetailsHandler
}

export default handlers

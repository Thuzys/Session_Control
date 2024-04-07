import views from './viewsCreators.js';

const API_BASE_URL = "http://localhost:8080"
const TOKEN = "e247758f-02b6-4037-bd85-fc245b84d5f2"

const PLAYER_ROUTE = "/players"
const GAME_ROUTE = "/games"
const SESSION_ROUTE = "/sessions"
const PLAYER_ID_ROUTE = `/${PLAYER_ROUTE}/player`
const GAME_ID_ROUTE = `${GAME_ROUTE}/game`
const SESSION_ID_ROUTE = `/${SESSION_ROUTE}/session`
const SESSION_DELETE_ROUTE = "$SESSION_ROUTE/delete"
const SESSION_ID_PLAYER_DELETE_ROUTE = "$SESSION_DELETE_ROUTE/player"

function executeRequest(url) {
    return fetch(url)
        .then(res => res.json())
}

function getHome(mainContent){
    const h1 = document.createElement("h1")
    const text = document.createTextNode("Home")
    h1.appendChild(text)
    mainContent.replaceChildren(h1)
}

function searchSessions(mainContent) {
    const form = views.form(
        views.h1({textContent: "Search Sessions"}),
        views.label({qualifiedName: "for", value: "textbox"}, "Enter Game Id: "),
        views.input({type: "text", id: "gameId"}),
        views.p(),
        views.label({qualifiedName: "for", value: "textbox"}, "Enter Player Id: "),
        views.input({type: "text", id: "playerId"}),
        views.p(),
        views.label({qualifiedName: "for", value: "textbox"}, "Enter Date: "),
        views.input({type: "datetime-local", id: "date"}),
        views.p(),
        views.label({qualifiedName: "for", value: "textbox"}, "Enter State: "),
        views.radioButton({ name: "state", value: "open", checked: true }),
        views.radioButtonLabel("open", "Open"),
        views.radioButton({ name: "state", value: "close" }),
        views.radioButtonLabel("close", "Close"),
        views.radioButton({ name: "state", value: "both" }),
        views.radioButtonLabel("both", "Both"),
        views.p(),
        views.button({ type: "submit" }, "Search")
    )

    mainContent.replaceChildren(form)

    form.onsubmit = (e) => {
        e.preventDefault()
        const inputGid = document.getElementById('gameId');
        const inputPid = document.getElementById('playerId');
        const inputDate = document.getElementById('date');
        const inputOpen = document.querySelector('input[name="state"][value="open"]');
        const inputClose = document.querySelector('input[name="state"][value="close"]');

        let url = API_BASE_URL + SESSION_ROUTE
        if(inputGid.value) {
            url += "?gid=" + inputGid.value
        }
        if(inputPid.value) {
            url += "&pid=" + inputPid.value
        }
        if(inputDate.value) {
            url += "&date=" + inputDate.value
        }
        if(inputOpen.checked) {
            url += "&state=" + inputOpen.value
        }
        else if (inputClose.checked) {
            url += "&state=" + inputClose.value
        }
        url += TOKEN //only for testing
        getSessions(mainContent, url)
    }
}

function getSessions(mainContent, url) {
    executeRequest(url).then(sessions => {
        const div = document.createElement("div")
        const h1 = document.createElement("h1")
        const text = document.createTextNode("text")
        h1.appendChild(text)
        div.appendChild(h1)
        sessions.forEach(session => {
            const p = document.createElement("p")
            const a = document.createElement("a")
            const aText = document.createTextNode("a")
            a.appendChild(aText)
            a.href="#sessions/" + session.number
            p.appendChild(a)
            div.appendChild(p)
        })
        mainContent.replaceChildren(div)
    })
}

function searchGames(mainContent) {
    const inputDev = views.input({id: "InputDev", type: "text", placeholder: "Insert Developer Name"})
    const inputGenres = views.input({id: "InputGenres", type: "text", placeholder: "Insert Genre(s) separated by comma and no spaces"})
    const searchButton = views.button({id: "SearchGamesButton", type: "submit", text: "" }, "Search")

    const updateButtonState = () => {
        searchButton.disabled = !inputDev.value.trim() && !inputGenres.value.trim()
    }

    updateButton(updateButtonState, inputDev, inputGenres)

    const form = views.form(
        {},
        inputDev,
        inputGenres,
        searchButton,
        views.p({textContent: "Search Games by Developer and/or Genre(s)"}),
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

function updateButton(updateButtonState, inputDev, inputGenres) {
    updateButtonState()
    inputDev.addEventListener("input", updateButtonState)
    inputGenres.addEventListener("input", updateButtonState)
}

function getGames(mainContent) {
    const url = `${API_BASE_URL}${GAME_ROUTE}?${getQuery()}&token=${TOKEN}`;

    executeRequest(url).then(games => {
        const gameList = views.ul();

        games.forEach(game => {
            const button = views.button({}, "Details");
            button.addEventListener("click", () => {
                window.location.hash = `games/${game.gid}`;
            })
            const gameItem = views.li(
                views.div({},
                    views.h2({}, game.name),
                    button
                )
            );
            gameList.appendChild(gameItem);
        });

        mainContent.replaceChildren(gameList);
    })
}


function getGameDetails(mainContent){
    const pathParams = getPathParams();
    const gameId = pathParams[0];
    const url = `${API_BASE_URL}${GAME_ID_ROUTE}?gid=${gameId}&token=${TOKEN}`;

    executeRequest(url).then(game => {
        const gameDetails = views.div(
            {},
            views.h1({textContent: game.name}),
            views.p(),
            views.h2({textContent: game.dev}),
            views.p(),
            views.h2({textContent: game.genres.join(", ")}),
        );

        mainContent.replaceChildren(gameDetails);
    });
}

function getQuery(){
    return window.location.hash.replace("#", "").split("?")[1]
}

function getPathParams(){
    return window.location.hash.replace("#", "").split("?")[0].split("/").slice(1)
}

export const handlers = {
    getHome,
    searchGames,
    getGames,
    searchSessions,
    getGameDetails
}
export default handlers

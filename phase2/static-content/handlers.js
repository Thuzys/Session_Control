/*
This example creates the students views using directly the DOM Api
But you can create the views in a different way, for example, for the student details you can:
    createElement("ul",
        createElement("li", "Name : " + student.name),
        createElement("li", "Number : " + student.number)
    )
or
    ul(
        li("Name : " + student.name),
        li("Number : " + student.name)
    )
Note: You have to use the DOM Api, but not directly
*/

const API_BASE_URL = "http://localhost:8080"
const TOKEN = "e247758f-02b6-4037-bd85-fc245b84d5f2"

const PLAYER_ROUTE = "/players"
const GAME_ROUTE = "/games"
const SESSION_ROUTE = "/sessions"
const PLAYER_ID_ROUTE = "$PLAYER_ROUTE/player"
const GAME_ID_ROUTE = "$GAME_ROUTE/game"
const SESSION_ID_ROUTE = "$SESSION_ROUTE/session"
const SESSION_DELETE_ROUTE = "$SESSION_ROUTE/delete"
const SESSION_ID_PLAYER_DELETE_ROUTE = "$SESSION_DELETE_ROUTE/player"

function getHome(mainContent){
    const h1 = document.createElement("h1")
    const text = document.createTextNode("Home")
    h1.appendChild(text)
    mainContent.replaceChildren(h1)
}

function executeRequest(url) {
    return fetch(url)
        .then(res => res.json())
}

function searchGames(mainContent) {
    const form = document.createElement("form")

    const title = document.createElement("p")
    const titleText = document.createTextNode("Search Games by Developer and/or Genre(s)")
    title.appendChild(titleText)

    const inputDev = document.createElement("input")
    inputDev.type = "text"
    inputDev.placeholder = "Insert Developer Name"
    form.appendChild(inputDev)

    const inputGenres = document.createElement("input")
    inputGenres.type = "text"
    inputGenres.placeholder = "Insert Genre(s) separated by comma and no spaces"
    form.appendChild(inputGenres)

    const button = document.createElement("button")
    const buttonText = document.createTextNode("Search")
    button.appendChild(buttonText)
    form.appendChild(button)

    const areInputsEmpty = () => {
        return !inputDev.value.trim() && !inputGenres.value.trim();
    }

    button.disabled = areInputsEmpty()

    inputDev.addEventListener('input', () => {
        button.disabled = areInputsEmpty()
    });

    inputGenres.addEventListener('input', () => {
        button.disabled = areInputsEmpty()
    });

    form.onsubmit = (e) => {
        e.preventDefault()
        let hash = "#games?"
        if(inputDev.value){
            hash += "dev=" + inputDev.value
        }
        if(inputGenres.value){
            if(inputDev.value){
                hash += "&"
            }
            hash += "genres=" + inputGenres.value
        }

        window.location.hash = hash
    }

    mainContent.replaceChildren(form)
}

function getGames(mainContent){
    const url = `${API_BASE_URL}${GAME_ROUTE}?${getQuery()}&token=${TOKEN}`
    console.log(url)
    executeRequest(url).then(games => {
        const div = document.createElement("div")

        const h1 = document.createElement("h1")
        const text = document.createTextNode("Games")
        h1.appendChild(text)
        div.appendChild(h1)

        games.forEach(game => {
            const gameDiv = document.createElement("div")
            const title = document.createElement("h2")
            const titleText = document.createTextNode(game.name)
            title.appendChild(titleText)
            gameDiv.appendChild(title)

            const developer = document.createElement("p")
            const developerText = document.createTextNode("Developer: " + game.dev)
            developer.appendChild(developerText)
            gameDiv.appendChild(developer)

            const genres = document.createElement("p")
            const genresText = document.createTextNode("Genres: " + game.genres.join(", "))
            genres.appendChild(genresText)
            gameDiv.appendChild(genres)

            div.appendChild(gameDiv)
        })
        mainContent.replaceChildren(div)
    })
}

function getQuery(){
    return window.location.hash.replace("#", "").split("?")[1]
}

export const handlers = {
    getHome,
    searchGames,
    getGames,
}

export default handlers
import menu from "./menuLinks.js";
import requestUtils from "./requestUtils.js";
import views from './viewsCreators.js';
import handlerUtils from "./handlerUtils.js";

const API_BASE_URL = "http://localhost:8080/"
const PLAYER_ROUTE = "players/player"
const SESSION_ROUTE = "sessions"
const TOKEN = "e247758f-02b6-4037-bd85-fc245b84d5f2"

function executeRequest(url, execute){
    fetch(url).then(it => execute(it))
}

function searchSessions(mainContent) {
    const h1 = views.h1({}, "Search Sessions")
    const form = views.form(
        {},
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

    mainContent.replaceChildren(h1, form)

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
    executeRequest(url, response => {
        if (response.ok){
            const sessions = response.json()
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
        }
    })
}

function getHome(mainContent, headerContent){
    const h1 = views.h1({},"Home page.")
    const form = views.form({action: "#playerDetails", method: "get"},
        views.input({type: "text", id: "pid", maxLength: 10}),
        views.button({type: "submit"}, "Player Details")
    )
    form.onsubmit = (e) => {
        e.preventDefault()
        const pid = document.getElementById("pid")
        if (pid.value === "") {
            alert("Please enter a player id")
            return
        }
        window.location.hash = `#playerDetails/${pid.value}`
    }
    headerContent.replaceChildren(menu.get("home"), menu.get("gameSearch"), menu.get("sessionSearch"))
    mainContent.replaceChildren(h1, form)
}

function getPlayerDetails(mainContent, mainHeader){
    const url = `${API_BASE_URL}${PLAYER_ROUTE}?pid=${requestUtils.getQuery()}&token=${TOKEN}`
    executeRequest(url, response => {
        if (handlerUtils.isOkResponse(response)) {
            response.json().then(
                player => {
                    const h2 = views.h2(
                        {},
                        "Player Details"
                    )
                    const ul = views.ul(
                        views.li("Name: " + player.name),
                        views.li("Email: " + player.email.email),
                        views.li("Pid: " + player.pid),
                    )
                    mainContent.replaceChildren(
                        views.div(
                            {},
                            h2,
                            ul
                        )
                    )
                    mainHeader.replaceChildren(menu.get("home"), menu.get("sessionSearch"))
                }
            )
        }
        else {
            alert("Error fetching data:" + response.statusText)
        }
    })
}



export const handlers = {
    getHome,
    searchSessions,
    getPlayerDetails,
}

export default handlers

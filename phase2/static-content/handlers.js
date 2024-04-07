import menu from "./menuLinks.js";
import requestUtils from "./requestUtils.js";
import views from './viewsCreators.js';
import handlerUtils from "./handlerUtils.js";

const API_BASE_URL = "http://localhost:8080/"
const PLAYER_ROUTE = "players/player"
const SESSION_ROUTE = "sessions"
const SESSION_ID_ROUTE = SESSION_ROUTE + "/session"
const TOKEN = "e247758f-02b6-4037-bd85-fc245b84d5f2"
const LIMIT = 10


function executeRequest(url, execute){
    fetch(url).then(it => execute(it))
}

function searchSessions(mainContent, mainHeader) {
    const h1 = handlerUtils.createHeader();
    const formContent = handlerUtils.createFormContent();
    const form = views.form({}, ...formContent);
    mainContent.replaceChildren(h1, form);
    form.addEventListener('submit', (e) => handleSearchSessionsSubmit(e, mainContent));
}

function handleSearchSessionsSubmit(e, mainContent, mainHeader) {
    e.preventDefault();
    const { value: gid } = document.getElementById('gameId');
    const { value: pid } = document.getElementById('playerId');
    const { value: date } = document.getElementById('date');
    const { checked: open } = document.querySelector('input[name="state"][value="open"]');
    const { checked: close } = document.querySelector('input[name="state"][value="close"]');

    const params = new URLSearchParams();
    if (gid) params.set('gid', gid);
    if (pid) params.set('pid', pid);
    if (date) params.set('date', date);
    if (open) params.set('state', 'open');
    if (close) params.set('state', 'close');


    window.location.hash = `#sessions?${params}&offset=0`;
}

function getSessions(mainContent, mainHeader) {
    const url = `${API_BASE_URL}${SESSION_ROUTE}?${requestUtils.getParams()}&token=${TOKEN}`
    executeRequest(url, response => {
        if(handlerUtils.isOkResponse(response)) {
            response.json().then(sessions => {
                const div = views.div({},
                    views.h1({}, "Sessions Found:")
                );
                sessions.forEach(session => {
                    const sessionHref = handlerUtils.sessionHrefConstructor(session)
                    div.appendChild(views.form({}, ...sessionHref))
                });
                mainContent.replaceChildren(div);
                mainHeader.replaceChildren(menu.get("home"), menu.get("sessionSearch"))
            });
        } else {
            alert("Error fetching data: " + response.statusText);
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
    headerContent.replaceChildren(menu.get("gameSearch"), menu.get("sessionSearch"))
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

function createSessionDetails(mainContent, mainHeader) {
    const url = `${API_BASE_URL}${SESSION_ID_ROUTE}?sid=${requestUtils.getQuery()}&token=${TOKEN}`
    executeRequest(url, response => {
        if (handlerUtils.isOkResponse(response)) {
            response.json().then(
                session => {
                    const playerList = views.ul();
                    session.players.forEach(player => {
                        const playerLi = views.li(
                            views.a(
                                {href: `#playerDetails/${player.pid}`},
                                "Player ID: " + player.pid
                            )
                        );
                        playerList.appendChild(playerLi);
                    });
                    mainContent.replaceChildren(
                        views.div(
                            {},
                            views.h3({}, "Session ID: " + session.sid),
                            views.ul(
                                views.li("Capacity: " + session.capacity),
                                views.li(
                                    views.a(
                                        {href: `#gameDetails/${session.gid}`},
                                        "Game ID: " + session.gid
                                    )
                                ),
                                views.li("Date: " + session.date),
                                views.li("Players:"),
                                playerList
                            )
                        )
                    )
                    mainHeader.replaceChildren(menu.get("home"))
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
    getSessions,
    createSessionDetails
}

export default handlers

import { menu, utils } from "./utils.js";

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

const API_BASE_URL = "http://localhost:8080/"
const PLAYER_ROUTE = "players/player"
const TOKEN = "e247758f-02b6-4037-bd85-fc245b84d5f2"

function getHome(mainContent, mainHeader){
    const h1 = document.createElement("h1")
    const text = document.createTextNode("Home page.")
    h1.appendChild(text)
    const pid = document.createElement("input")
    const button = document.createElement("button")
    button.type = "submit"
    button.textContent = "Player Details"
    pid.type = "text"
    pid.name = "pid"
    pid.maxLength = 10
    const form = document.createElement("form")
    form.action = "#playerDetails"
    form.method = "get"
    form.appendChild(pid)
    form.appendChild(button)
    form.onsubmit = (e) => {
        e.preventDefault()
        window.location.hash = `#playerDetails?pid=${pid.value}`
    }
    mainHeader.innerHTML = ""
    mainHeader.appendChild(menu.get("home"))
    mainHeader.appendChild(menu.get("gameSearch"))
    mainHeader.appendChild(menu.get("sessionSearch"))
    mainContent.replaceChildren(h1, form)
}

function getPlayerDetails(mainContent, mainHeader){
    const url = `${API_BASE_URL}${PLAYER_ROUTE}?${utils.getQuery()}&token=${TOKEN}`
    executeRequest(url).then(player => {
        const h1 = document.createElement("h1")
        const text = document.createTextNode("Player Details:")
        h1.appendChild(text)
        const name = document.createElement("p")
        const nameText = document.createTextNode(`Name: ${player.name}`)
        name.appendChild(nameText)
        const email = document.createElement("p")
        const emailText = document.createTextNode(`Email: ${player.email.email}`)
        email.appendChild(emailText)
        const pid = document.createElement("p")
        const pidText = document.createTextNode(`Pid: ${player.pid}`)
        pid.appendChild(pidText)
        mainContent.replaceChildren(h1, name, email, pid)
    })
}

function executeRequest(url) {
    return fetch(url)
        .then(res => {
            if (res.ok) {
                return res.json()
            } else {
                throw new Error("Error fetching data:" + res.statusText)
            }
        } )
}

export const handlers = {
    getHome,
    getPlayerDetails,
}

export default handlers
import views from './viewsCreators.js';

const API_BASE_URL = "http://localhost:8080/"
const SESSION_ROUTE = "sessions"
const TOKEN = "&token=e247758f-02b6-4037-bd85-fc245b84d5f2"

function executeRequest(url, execute){
    fetch(url)
        .then(res => res.json())
        .then(it => execute(it))
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
    executeRequest(url, sessions => {
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

function getHome(mainContent){

    const h1 = document.createElement("h1")
    const text = document.createTextNode("Home")
    h1.appendChild(text)
    mainContent.replaceChildren(h1)
}

function getStudents(mainContent){
    fetch(API_BASE_URL + "students")
        .then(res => res.json())
        .then(students => {
            const div = document.createElement("div")

            const h1 = document.createElement("h1")
            const text = document.createTextNode("Students")
            h1.appendChild(text)
            div.appendChild(h1)

            students.forEach(s => {
                const p = document.createElement("p")
                const a = document.createElement("a")
                const aText = document.createTextNode("Link Example to students/" + s.number);
                a.appendChild(aText)
                a.href="#students/" + s.number
                p.appendChild(a)
                div.appendChild(p)
            })
            mainContent.replaceChildren(div)
        })
}

function getStudent(mainContent){
    fetch(API_BASE_URL + "students/10")
        .then(res => res.json())
        .then(student => {
            const ulStd = document.createElement("ul")

            const liName = document.createElement("li")
            const textName = document.createTextNode("Name : " + student.name)
            liName.appendChild(textName)

            const liNumber = document.createElement("li")
            const textNumber = document.createTextNode("Number : " + student.number)
            liNumber.appendChild(textNumber)

            ulStd.appendChild(liName)
            ulStd.appendChild(liNumber)

            mainContent.replaceChildren(ulStd)
    })
}

export const handlers = {
    getHome,
    getStudent,
    getStudents,
    searchSessions,
}
export default handlers

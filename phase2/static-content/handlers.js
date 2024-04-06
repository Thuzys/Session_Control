import views from './viewsCreators.js';
import handlerUtils from "./handlerUtils.js";

const API_BASE_URL = "http://localhost:8080/"
const SESSION_ROUTE = "sessions"
const TOKEN = "&token=e247758f-02b6-4037-bd85-fc245b84d5f2"

function executeRequest(url, execute){
    fetch(url).then(it => execute(it));
}

function searchSessions(mainContent) {
    const h1 = handlerUtils.createHeader();
    const formContent = handlerUtils.createFormContent();
    const form = views.form({}, ...formContent);
    mainContent.replaceChildren(h1, form);
    form.addEventListener('submit', (e) => handleSearchSessionsSubmit(e, mainContent));
}

function handleSearchSessionsSubmit(e, mainContent) {
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

    const url = `${API_BASE_URL}${SESSION_ROUTE}?${params}${TOKEN}`;
    // window.location.hash =
    getSessions(mainContent, url);
}

function getSessions(mainContent, url) {
    executeRequest(url, response => {
            response.json().then(sessions => {
                const div = views.div({},
                    views.h1({}, "Sessions Found:")
                );
                sessions.forEach(session => {
                    const sessionHref = handlerUtils.sessionHrefConstructor(session)
                    div.appendChild(views.form({}, ...sessionHref))
                    // const test = handlerUtils.createSessionDetails(session)
                    // div.appendChild(test)
                });
                mainContent.replaceChildren(div);
            });
        })
    }

function getHome(mainContent){

    const h1 = document.createElement("h1")
    const text = document.createTextNode("Home")
    h1.appendChild(text)
    mainContent.replaceChildren(h1)
}

export const handlers = {
    getHome,
    searchSessions,
}
export default handlers

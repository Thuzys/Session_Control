import views from "./viewsCreators.js";
import requestUtils from "./requestUtils.js";

const LIMIT = 10;

function changeHash(hash) {
    window.location.hash = hash;
}

const updateButtonState = (searchButton, inputDev, inputGenres) => {
    searchButton.disabled = !inputDev.value.trim() && !inputGenres.value.trim()
}

function updateGameSearchButton(searchButton, inputDev, inputGenres) {
    const update = () => updateButtonState(searchButton, inputDev, inputGenres)
    inputDev.addEventListener("input", update)
    inputGenres.addEventListener("input", update)
}

function isResponseOK(response) {
    return response.status >= 200 && response.status < 399
}

function createHeader(text) {
    return views.h1({}, text);
}

function createHomeView() {
    const h1 = views.h1({}, "Home page");
    const form = views.form({action: "#playerDetails", method: "get"},
        views.input({type: "text", id: "pid", maxLength: 10}),
        views.button({type: "submit"}, "Player Details")
    );
    return [h1, form];
}

function createStateInputs() {
    return [
        views.label({qualifiedName: "for", value: "textbox"}, "Enter State: "),
        views.radioButton({name: "state", value: "open", checked: true}),
        views.radioButtonLabel("open", "Open"),
        views.radioButton({name: "state", value: "close"}),
        views.radioButtonLabel("close", "Close"),
        views.radioButton({name: "state", value: "both"}),
        views.radioButtonLabel("both", "Both")
    ];
}

function createLabeledInput(labelText, inputType, inputId) {
    return [
        views.label({ qualifiedName: "for", value: inputId }, labelText),
        views.input({ type: inputType, id: inputId }),
        views.p()
    ];
}

function createFormContent() {
    const gidLabelInput = createLabeledInput("Enter Game Id: ", "text", "gameId");
    const pidLabelInput = createLabeledInput("Enter Player Id: ", "text", "playerId");
    const dateLabelInput = createLabeledInput("Enter Date: ", "datetime-local", "date");
    const stateLabelInputs = createStateInputs();

    return [
        ...gidLabelInput,
        ...pidLabelInput,
        ...dateLabelInput,
        ...stateLabelInputs,
        views.p(),
        views.button({ type: "submit" }, "Search")
    ];
}

function sessionHrefConstructor(session) {
    return [
        views.a({href: `#sessionDetails/${session.sid}`}, "Session" + session.sid),
        views.p()
        ]
}

function executeCommandWithResponse(url, responseHandler) {
    fetch(url)
        .then(response => {
            if(isResponseOK(response)) {
                responseHandler(response);
            } else {
                response.text().then(text => alert("Error fetching data: " + text));
            }
        })
}

function makeQueryString(query) {
    const queryString = new URLSearchParams();
    for (const [key, value] of query) {
        queryString.set(key, value);
    }
    return queryString;
}

function createPlayerListView(session) {
    const playerList = views.ul();
    if (session.players){
        session.players.forEach(player => {
            const playerLi = views.li(
                views.a(
                    {href: `#playerDetails/${player.pid}`},
                    "Player ID: " + player.pid
                )
            );
            playerList.appendChild(playerLi);
        });
    }
    return playerList;
}

function createSessionDetailsViews(session, playerList) {
    const backButton = createBackButtonView();
    return views.div(
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
        ),
        backButton
    );
}

function createBackButtonView() {
    const backButton = views.button({type: "button"}, "Back");
    backButton.addEventListener('click', () => {
        window.history.back();
    });
    return backButton;
}

function createPagination(query, hash, hasNext) {
    const prevButton = views.button({id: "prev", type: "button"}, "Previous")
    prevButton.addEventListener('click', () => {
        if (query.get("offset") > 0) {
            query.set("offset", query.get("offset") - LIMIT)
            window.location.hash = `${hash}?${handlerUtils.makeQueryString(query)}`
        }
    })

    const nextButton = views.button({id: "next", type: "button", enabled: hasNext}, "Next")
    nextButton.addEventListener('click', () => {
        if (hasNext) {
            query.set("offset", query.get("offset") + LIMIT)
            window.location.hash = `${hash}?${handlerUtils.makeQueryString(query)}`
        }
        nextButton.disabled = true;
    })

    return views.div(
        {},
        prevButton,
        nextButton,
    )
}

function createGetSessionsView(sessions) {
    const query = requestUtils.getQuery();
    const div = views.div({},
        views.h1({}, "Sessions Found:")
    );
    sessions.forEach(session => {
        const sessionHref = handlerUtils.sessionHrefConstructor(session)
        div.appendChild(views.form({}, ...sessionHref))
    });
    const prevButton = views.button({id: "prev", type: "button"}, "Previous")
    prevButton.addEventListener('click', () => {
        if (query.get("offset") > 0) {
            query.set("offset", query.get("offset") - LIMIT)
            window.location.hash = `#sessions?${handlerUtils.makeQueryString(query)}`
        }
    });
    const hasNext = sessions.length === LIMIT;
    const nextButton = views.button({id: "next", type: "button", enabled: hasNext}, "Next")
    nextButton.addEventListener('click', () => {
        if (hasNext) {
            query.set("offset", query.get("offset") + LIMIT)
            window.location.hash = `#sessions?${handlerUtils.makeQueryString(query)}`
        }
        nextButton.disabled = true;
    });

    const nextPrev = views.div(
        {},
        prevButton,
        nextButton,
    );
    return [div, nextPrev];
}

function createPlayerDetailsView(player) {
    const h2 = views.h2({}, "Player Details");
    const playerDetailsView = views.ul(
        views.li("Name: " + player.name),
        views.li("Email: " + player.email.email),
        views.li("Pid: " + player.pid),
    );
    const backButtonView = createBackButtonView();
    return views.div({}, h2, playerDetailsView, backButtonView);
}

const handlerUtils = {
    createBackButtonView,
    createPagination,
    sessionHrefConstructor,
    changeHash,
    updateGameSearchButton,
    createStateInputs,
    createHeader,
    createLabeledInput,
    createFormContent,
    executeCommandWithResponse,
    makeQueryString,
    createPlayerListView,
    createSessionDetailsViews,
    createGetSessionsView,
    createPlayerDetailsView,
    createHomeView,
}

export default handlerUtils
import views from "./viewsCreators.js";

function createHeader() {
    return views.h1({}, "Search Sessions: ");
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

function createSessionDetails(session) {
    const playerList = views.ul();

    session.players.forEach(player => {
        const playerLi = views.li("Player ID: " + player.pid);
        playerList.appendChild(playerLi);
    });

    return views.div({},
        views.h3({}, "Session ID: " + session.sid),
        views.ul(
            views.li("Capacity: " + session.capacity),
            views.li("Group ID: " + session.gid),
            views.li("Date: " + session.date),
            views.li("Players:"),
            playerList
        )
    );
}

function sessionHrefConstructor(session) {
    return [
        views.a({href: "#"}, "Session" + session.sid),
        views.p()
        ]
}

const handlerUtils = {
    createStateInputs,
    createHeader,
    createLabeledInput,
    createFormContent,
    createSessionDetails,
    sessionHrefConstructor
}

export default handlerUtils
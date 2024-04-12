import views from "../viewsCreators.js";
import requestUtils from "../../utils/requestUtils.js";
import handlerViews from "./handlerViews.js";
import constants from "../../constants/constants.js";

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

function createSessionFormContentView() {
    const gidLabelInput = handlerViews.createLabeledInput("Enter Game Id: ", "text", "gameId");
    const pidLabelInput = handlerViews.createLabeledInput("Enter Player Id: ", "text", "playerId");
    const dateLabelInput = handlerViews.createLabeledInput("Enter Date: ", "datetime-local", "date");
    const stateLabelInputs = createStateInputs();

    return [
        ...gidLabelInput,
        ...pidLabelInput,
        ...dateLabelInput,
        ...stateLabelInputs,
        views.p(),
        views.button({type: "submit", class: "submit-button"}, "Search")
    ];
}

function createSessionDetailsViews(session, playerList) {
    const backButton = handlerViews.createBackButtonView();
    return views.div(
        {},
        views.h3({}, "Session ID: " + session.sid),
        views.ul(
            views.li("Capacity: " + session.capacity),
            views.li(
                ...handlerViews.hrefConstructor("#games", session.gid, "Game ID: ")
            ),
            views.li("Date: " + session.date),
            views.li("Players:"),
            playerList
        ),
        backButton
    );
}

function createGetSessionsView(sessions) {
    const query = requestUtils.getQuery();
    const div = views.div({},
        views.h1({}, "Sessions Found:")
    );
    sessions.forEach(session => {
        const sessionHref = handlerViews.hrefConstructor("#sessionDetails", session.sid, "Session ID:")
        div.appendChild(views.form({}, ...sessionHref))
    });
    const nextPrev = handlerViews.createPagination(query, "#sessions", sessions.length === constants.LIMIT);
    return [div, nextPrev];
}

export function createPlayerListView(session) {
    const playerList = views.ul();
    if (session.players) {
        session.players.forEach(player => {
            const playerLi = views.li(
                ...handlerViews.hrefConstructor("#playerDetails", player.pid, "Player ID:")
            );
            playerList.appendChild(playerLi);
        });
    }
    return playerList;
}


const sessionHandlerViews = {
    createSessionFormContentView,
    createSessionDetailsViews,
    createGetSessionsView,
    createPlayerListView
}

export default sessionHandlerViews;


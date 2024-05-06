import views from "../viewsCreators.js";
import requestUtils from "../../utils/requestUtils.js";
import handlerViews from "./handlerViews.js";
import constants from "../../constants/constants.js";

function createStateInputs() {
    return [
        views.label({qualifiedName: "for", value: "textbox"}, "Enter State: "),
        views.radioButton({name: "state", value: "open"}),
        views.radioButtonLabel("open", "Open"),
        views.radioButton({name: "state", value: "close"}),
        views.radioButtonLabel("close", "Close"),
        views.radioButton({name: "state", value: "both", checked: true}),
        views.radioButtonLabel("both", "Both")
    ];
}

function createSessionFormContentView() {
    const gidLabelInput = handlerViews.createLabeledInput("Enter Game name: ", "text", "gameName");
    const pidLabelInput = handlerViews.createLabeledInput("Enter Player userName: ", "text", "userName");
    const dateLabelInput = handlerViews.createLabeledInput("Enter Date: ", "date", "date");
    const stateLabelInputs = createStateInputs();

    return [
        ...gidLabelInput,
        ...pidLabelInput,
        ...dateLabelInput,
        ...stateLabelInputs,
        views.p(),
        views.button({type: "submit", class: "general-button"}, "Search"),
    ];
}

function createSessionDetailsViews(session, playerList) {
    const backButton = handlerViews.createBackButtonView();

    return views.div(
        {},
        views.h3({}, "Session: " + session.gameInfo.name + " - " + session.owner.userName),
        views.ul(
            views.li(
                ...handlerViews.hrefConstructor(
                    "#games",
                    session.gameInfo.gid, `Game: ${session.gameInfo.name}`
                )
            ),
            views.li("Date: " + session.date),
            views.li("Owner: " + session.owner.userName),
            views.li("Capacity: " + session.capacity),
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
    sessions.slice(0, constants.ELEMENTS_PER_PAGE).forEach(session => {
        const sessionHref = handlerViews.hrefConstructor(
            "#sessions",
            session.sid, `Session: ${session.owner.userName} - ${session.date}`,
            0,
        );
        div.appendChild(views.form({}, ...sessionHref))
    });
    const nextPrev = handlerViews.createPagination(query, "#sessions", sessions.length === constants.LIMIT);
    return [div, nextPrev];
}

function createPlayerListView(session) {
    const div = views.div();
    const playerList = views.ul();
    if (session.players) {
        session.players
            .slice(0, constants.ELEMENTS_PER_PAGE_PLAYERS)
            .forEach(player => {
            const playerLi = views.li(
                ...handlerViews.hrefConstructor("#players", player.pid, player.userName)
            );
            playerList.appendChild(playerLi);
        });
    }
    div.appendChild(playerList);
    const nextPrev = handlerViews.createPagination(
        requestUtils.getQuery(),
        "#sessions/"+session.sid,
        session.players !== undefined && session.players.length > constants.LIMIT_PLAYERS,
        constants.ELEMENTS_PER_PAGE_PLAYERS
    );
    div.appendChild(nextPrev)
    return div;
}

function createCreateSessionView(gameName) {
    const header = handlerViews.createHeader("Create Session: ");
    const labelCapacity = views.input({type: "number", id: "capacity", placeholder: "Enter Capacity"})
    const labelDate = views.input({type: "date", id: "dateCreate", placeholder: "Enter Date"});
    const formContent = views.form(
        {},
        views.h3({}, gameName),
        labelCapacity,
        labelDate,
        views.p(),
        views.button({type: "submit", class: "submit-button"}, "Create")
    );

    return [header, formContent];
}


const sessionHandlerViews = {
    createSessionFormContentView,
    createSessionDetailsViews,
    createGetSessionsView,
    createPlayerListView,
    createCreateSessionView
}

export default sessionHandlerViews;


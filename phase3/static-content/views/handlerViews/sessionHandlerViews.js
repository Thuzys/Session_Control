import views from "../viewsCreators.js";
import requestUtils from "../../utils/requestUtils.js";
import handlerViews from "./handlerViews.js";
import constants from "../../constants/constants.js";

/**
 * Create state inputs view for session search
 * @returns {*[]} state inputs view
 */
function createStateInputsView() {
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

/**
 * Create search sessions view
 * @returns {*[]} session form content view
 */
function createSearchSessionView() {
    const gidLabelInput = handlerViews.createLabeledInput("Enter Game name: ", "text", "gameName");
    const pidLabelInput = handlerViews.createLabeledInput("Enter Player username: ", "text", "userName");
    const dateLabelInput = handlerViews.createLabeledInput("Enter Date: ", "date", "date");
    const stateLabelInputs = createStateInputsView();

    return [
        ...gidLabelInput,
        ...pidLabelInput,
        ...dateLabelInput,
        ...stateLabelInputs,
        views.p(),
        views.button({type: "submit", class: "general-button"}, "Search"),
    ];
}

/**
 * Create session details view
 * @param session session data
 * @param playerList player list view
 * @param isOwner if true, create delete session button
 * @param isInSession if true, create leave session button
 * @returns {HTMLDivElement}
 */
function createSessionDetailsViews(session, playerList, isOwner, isInSession) {
    const backButton = handlerViews.createBackButtonView();
    const deleteSessionButton = handlerViews.createDeleteOrLeaveSessionButtonView(session);
    const leaveSessionButton = handlerViews.createDeleteOrLeaveSessionButtonView(session, true);
    const updateButton = handlerViews.hrefButtonView("Update", "#updateSession?sid=" + session.sid);
    const div = views.div(
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

    if (isOwner) {
        div.appendChild(deleteSessionButton);
        div.appendChild(updateButton);
    } else if (isInSession){
        div.appendChild(leaveSessionButton);
    }

    return div;
}

/**
 * Create get sessions view
 * @param sessions sessions data
 * @returns {(HTMLDivElement|*)[]} sessions view
 */
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

/**
 * Create player list view
 * @param session session data
 * @returns {HTMLDivElement}
 */
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
        session.players !== undefined && session.players.length >= constants.LIMIT_PLAYERS,
        constants.ELEMENTS_PER_PAGE_PLAYERS
    );
    div.appendChild(nextPrev)
    return div;
}

/**
 * Create the create session view
 * @param gameName game name
 * @returns {(HTMLHeadingElement|HTMLFormElement)[]}
 */
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
        views.button({type: "submit", class: "general-button"}, "Create")
    );

    return [header, formContent];
}

/**
 * Create the update session view
 * @param session session data
 * @returns {(HTMLHeadingElement|HTMLFormElement)[]}
 */
function createUpdateSessionView(session) {
    const header = handlerViews.createHeader("Update Session: ");
    const labelCapacity = views.input({type: "number", id: "capacity", placeholder: "Enter Capacity", value: session.capacity})
    const labelDate = views.input({type: "date", id: "dateChange", placeholder: "Enter Date", value: session.date});
    const formContent = views.form(
        {},
        views.h3({}, session.gameInfo.name),
        labelCapacity,
        labelDate,
        views.p(),
        views.button({type: "submit", class: "general-button"}, "Update")
    );

    return [header, formContent];
}

const sessionHandlerViews = {
    createSearchSessionView,
    createSessionDetailsViews,
    createGetSessionsView,
    createPlayerListView,
    createCreateSessionView,
    createUpdateSessionView,
}

export default sessionHandlerViews;


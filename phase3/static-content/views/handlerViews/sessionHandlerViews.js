import views from "../viewsCreators.js";
import requestUtils from "../../utils/requestUtils.js";
import handlerViews from "./handlerViews.js";
import constants from "../../constants/constants.js";
import handlerUtils from "../../handlers/handlerUtils/handlerUtils.js";

/**
 * Create session form content view
 *
 * @returns {*[]}
 */
function createSessionFormContentView() {
    const gidInput = handlerViews.createLabeledInput("gameName", "Enter Game name");
    const pidInput = handlerViews.createLabeledInput("userName", "Enter Player name");
    const dateInput = views.input({ type: "date", id: "date", placeholder: "Enter Date" });

    const stateLabel = views.label({ qualifiedName: "for", value: "state" }, "Enter State: ");
    const radioOpen = handlerViews.createLabeledRadioButton("state", "open", "open");
    const radioClose = handlerViews.createLabeledRadioButton("state", "close", "close");
    const radioBoth = handlerViews.createLabeledRadioButton("state", "both", "both");

    const searchButton = views.button({
        type: "submit",
        class: "general-button",
        disabled: true
    }, "Search");

    const toggleSearchButton = () => {
        handlerViews.toggleButtonState(
            searchButton,
            !canSearchSessions(
                gidInput.value.trim(),
                pidInput.value.trim(),
                dateInput.value.trim(),
                radioOpen.radioButton.checked || radioClose.radioButton.checked || radioBoth.radioButton.checked
            )
        )
    };

    handlerViews.addToggleEventListeners(
        toggleSearchButton,
        gidInput,
        pidInput,
        dateInput,
        radioOpen.radioButton,
        radioClose.radioButton,
        radioBoth.radioButton
    );

    return [
        gidInput,
        views.p(),
        pidInput,
        views.p(),
        dateInput,
        views.p(),
        stateLabel,
        radioOpen.radioButton,
        radioOpen.label,
        radioClose.radioButton,
        radioClose.label,
        radioBoth.radioButton,
        radioBoth.label,
        views.p(),
        searchButton
    ];
}

/**
 * Function to check if search can be performed
 *
 * @param gidInputValue
 * @param pidInputValue
 * @param dateInputValue
 * @param stateInputValue
 * @returns {*}
 */
function canSearchSessions(gidInputValue, pidInputValue, dateInputValue, stateInputValue) {
    return gidInputValue || pidInputValue || dateInputValue || stateInputValue;
}

/**
 * Create session details views
 *
 * @param session
 * @param playerList
 * @param isOwner
 * @param isInSession
 * @returns {any}
 */
function createSessionDetailsViews(session, playerList, isOwner, isInSession) {
    const backButton = handlerViews.createBackButtonView();
    const deleteSessionButton = handlerViews.createDeleteSessionButtonView(session);
    const leaveSessionButton = handlerViews.createLeaveSessionButtonView(session);
    const updateButton = handlerViews.createUpdateSessionButtonView(session);
    const joinSessionButton = createJoinSessionButtonView(session);

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
    } else {
        div.appendChild(joinSessionButton);
    }

    return div;
}

function createJoinSessionButtonView() {
    const joinSessionButton = views.button(
        {type: "submit", class: "general-button"},
        "Join Session"
    );
    joinSessionButton.addEventListener('click', () => {
        handlerUtils.changeHash("#joinSession");
    });
    return joinSessionButton;
}

/**
 * Create get sessions view
 *
 * @param sessions
 * @returns {any[]}
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
 *
 * @param session
 * @returns {any}
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
 * Create create session view
 *
 * @param gameName
 * @returns {(*)[]}
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
        views.button({type: "submit", class: "submit-button"}, "Create")
    );

    return [header, formContent];
}

/**
 * Function to check if session can be updated
 *
 * @param labelCapacity
 * @param labelDate
 * @param session
 * @returns {boolean}
 */
function canUpdateSession(labelCapacity, labelDate, session) {
    return (parseInt(labelCapacity.value) !== session.capacity && labelCapacity.value.trim() !== "")
        || (labelDate.value !== session.date && labelDate.value.trim() !== "")

}

/**
 * Create update session form
 *
 * @param session
 * @returns {(*)[]}
 */
function createUpdateSessionForm(session) {
    const header = handlerViews.createHeader("Update Session: ");
    const labelCapacity =
        views.input({type: "number", id: "capacity", placeholder: "Enter Capacity", value: session.capacity})
    const labelDate =
        views.input({type: "date", id: "dateChange", placeholder: "Enter Date", value: session.date});
    const updateSessionButton =
        views.button(
            {type: "submit", class: "general-button", disabled: true},
            "Update"
        )

    const toggleUpdateButton = () => {
        handlerViews.toggleButtonState(
            updateSessionButton,
            !canUpdateSession(labelCapacity, labelDate, session)
        )
    };

    handlerViews.addToggleEventListeners(
        toggleUpdateButton,
        labelCapacity,
        labelDate
    );

    const formContent = views.form(
        {},
        views.h3({}, session.gameInfo.name),
        labelCapacity,
        views.p(),
        labelDate,
        views.p(),
        updateSessionButton
    );

    return [header, formContent];
}

const sessionHandlerViews = {
    createSessionFormContentView,
    createSessionDetailsViews,
    createGetSessionsView,
    createPlayerListView,
    createCreateSessionView,
    createUpdateSessionForm,
}

export default sessionHandlerViews;

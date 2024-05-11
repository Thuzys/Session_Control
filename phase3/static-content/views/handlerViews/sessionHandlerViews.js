import views from "../viewsCreators.js";
import requestUtils from "../../utils/requestUtils.js";
import handlerViews from "./handlerViews.js";
import constants from "../../constants/constants.js";
import sessionHandlers from "../../handlers/sessionHandlers.js";

/**
 * Create session form content view
 * @returns {*[]}
 */
function createSessionFormContentView() {
    const header = handlerViews.createHeader("Search Sessions");
    const gidInput = handlerViews.createLabeledInput("gameName", "Enter Game name");
    const pidInput = handlerViews.createLabeledInput("userName", "Enter Player name");
    const dateInput = views.input({ type: "date", id: "date", placeholder: "Enter Date" });

    const stateLabel = views.h5({class: "w3-wide padding-left"}, "Enter State");
    const radioOpen = handlerViews.createRadioButton("open", "OPEN");
    const radioClose = handlerViews.createRadioButton("close", "CLOSE");

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
                radioOpen.children[0].checked || radioClose.children[0].checked
            )
        )
    };

    handlerViews.addToggleEventListeners(
        toggleSearchButton,
        gidInput,
        pidInput,
        dateInput,
        radioOpen.children[0],
        radioClose.children[0],
    );

    return [
        header,
        views.hr({class:"w3-opacity"}),
        gidInput,
        views.p(),
        pidInput,
        views.p(),
        dateInput,
        views.p(),
        views.div({class: "w3-row-padding w3-margin-bottom w3-center background"},
            stateLabel,
            views.hr({class:"w3-opacity"}),
            radioOpen,
            views.p(),
            radioClose,
            views.p(),
        ),
        views.p(),
        searchButton
    ];
}

/**
 * Checks if search can be performed
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
 * @param session
 * @param playerList
 * @param isOwner
 * @param isInSession
 * @returns {any}
 */
function createSessionDetailsViews(session, playerList, isOwner, isInSession) {
    const deleteSessionButton = handlerViews.createDeleteOrLeaveSessionButtonView(session);
    const leaveSessionButton = handlerViews.createDeleteOrLeaveSessionButtonView(session, true);
    const updateButton = handlerViews.createUpdateSessionButtonView(session);
    const joinSessionButton = createJoinSessionButtonView(session);
    const div = views.div(
        {},
        handlerViews.createHeader(session.owner.userName + "´s Session"),
        views.hr({class:"w3-opacity)"}),
        views.div({class: "w3-margin-bottom"},
        views.ul({class: "w3-ul w3-border w3-center w3-hover-shadow"},
            views.li(views.h3({class: "w3-wide blue-letters"}, "Game")),
            views.li(
                ...handlerViews.hrefConstructor(
                    "#games",
                    session.gameInfo.gid, `${session.gameInfo.name}`
                )
            ),
            views.li(views.h3({class: "w3-wide blue-letters"}, "Date")),
            views.li(session.date),
            views.li(views.h3({class: "w3-wide blue-letters"}, "Owner")),
            views.li(session.owner.userName),
            views.li(views.h3({class: "w3-wide blue-letters"}, "Capacity")),
            views.li(session.capacity.toString()),
            views.li(views.h3({class: "w3-wide blue-letters"}, "Players")),
            playerList
        ),
        )
    );

    if (isOwner) {
        div.appendChild(deleteSessionButton);
        div.appendChild(views.p());
        div.appendChild(updateButton);
    } else if (isInSession){
        div.appendChild(leaveSessionButton);
    } else {
        div.appendChild(joinSessionButton);
    }

    return div;
}


/**
 * Create join session button view
 * @param session
 * @returns {HTMLButtonElement}
 */
function createJoinSessionButtonView(session) {

    const joinSessionButton = views.button(
        {type: "submit", class: "general-button"},
        "Join Session"
    );
    joinSessionButton.addEventListener('click', () => {
        sessionHandlers.addPlayerToSession(session.sid);
    });
    return joinSessionButton;
}

/**
 * Create get sessions view
 * @param sessions sessions data
 * @returns {(HTMLDivElement|*)[]} sessions view
 */
function createGetSessionsView(sessions) {
    const query = requestUtils.getQuery();
    const div = views.div({class: "pagination-sessions-min-height"},
        handlerViews.createHeader("Sessions Found: "),
        views.hr({class:"w3-opacity"})
    );
    const sessionsElems = views.ul({class: "centered-list w3-ul w3-border w3-center w3-hover-shadow"});
    sessions
        .slice(0, constants.ELEMENTS_PER_PAGE)
        .forEach(session => {
        const sessionHref =
            views.li(
                ...handlerViews.hrefConstructor(
                "#sessions",
                session.sid, session.owner.userName + "´s Session" + " - " + session.date,
                0,
            ));
        sessionsElems.appendChild(sessionHref);
    });
    div.appendChild(sessionsElems);
    const nextPrev = handlerViews.createPagination(query, "#sessions", sessions.length === constants.LIMIT);
    return [div, nextPrev];
}

/**
 * Create player list view
 * @param session session data
 * @returns {HTMLDivElement}
 */
function createPlayerListView(session) {
    const div = views.div({class: "pagination-players-min-height"})
    const playerList = views.ul({class:"pagination-players-min-height"});
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
    const hr = views.hr({class:"w3-opacity"})
    const labelCapacity = views.input({type: "number", id: "capacity", placeholder: "Enter Capacity"})
    const labelDate = views.input({type: "date", id: "dateCreate", placeholder: "Enter Date"});
    const formContent = views.form(
        {},
        hr,
        views.h4({class: "w3-wide blue-letters"}, "Game"),
        views.p({}, gameName.toString()),
        views.h4({class: "w3-wide blue-letters"}, "Capacity"),
        labelCapacity,
        views.p(),
        views.h4({class: "w3-wide blue-letters"}, "Date"),
        labelDate,
        views.p(),
        views.button({type: "submit", class: "general-button"}, "Create")
    );

    return [header, formContent];
}

/**
 * Function to check if session can be updated
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
 * Create the update session view
 * @param session session data
 * @returns {(HTMLHeadingElement|HTMLFormElement)[]}
 */
function createUpdateSessionView(session) {
    const header = handlerViews.createHeader("Update Session: ");
    const labelCapacity = views.input({type: "number", id: "capacity", placeholder: "Enter Capacity", value: session.capacity})
    const labelDate = views.input({type: "date", id: "dateChange", placeholder: "Enter Date", value: session.date});

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
        views.hr({class:"w3-opacity"}),
        views.h4({class: "w3-wide blue-letters"}, "Game"),
        views.p({}, session.gameInfo.name),
        views.h4({class: "w3-wide blue-letters"}, "Capacity"),
        labelCapacity,
        views.p(),
        views.h4({class: "w3-wide blue-letters"}, "Date"),
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
    createUpdateSessionView,
}

export default sessionHandlerViews;

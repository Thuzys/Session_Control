import views from "../viewsCreators.js";
import requestUtils from "../../utils/requestUtils.js";
import handlerViews from "./handlerViews.js";
import constants from "../../constants/constants.js";
import sessionHandlers from "../../handlers/sessionHandlers.js";
import handlerUtils from "../../handlers/handlerUtils/handlerUtils.js";

/**
 * Create session form content view
 * @returns {HTMLDivElement} session form content view
 */
function createSessionFormContentView() {
    const container = views.div({class: "player-details-container"});
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

    const form = views.form(
        {},
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
    );
    container.replaceChildren(form);
    return container;
}

/**
 * Checks if search can be performed
 * @param gidInputValue game id
 * @param pidInputValue player id
 * @param dateInputValue date
 * @param stateInputValue state
 * @returns {*} true if search can be performed
 */
function canSearchSessions(gidInputValue, pidInputValue, dateInputValue, stateInputValue) {
    return gidInputValue || pidInputValue || dateInputValue || stateInputValue;
}

/**
 * Create session details views
 * @param session session data
 * @param playerList player list data
 * @param isOwner is owner of the session
 * @param isInSession is in session
 * @returns {HTMLDivElement} session details view
 */
function createSessionDetailsView(session, playerList, isOwner, isInSession) {
    const container = views.div({class: "player-details-container"});
    const deleteSessionButton = createDeleteOrLeaveSessionButtonView(session);
    const leaveSessionButton = createDeleteOrLeaveSessionButtonView(session, true);
    const updateButton = createUpdateSessionButtonView(session);
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

    container.replaceChildren(div);
    return container;
}


/**
 * Create join session button view
 * @param session session to join
 * @returns {HTMLButtonElement} join session button view
 */
function createJoinSessionButtonView(session) {

    const joinSessionButton = views.button(
        {type: "submit", class: "general-button"},
        "Join Session"
    );
    joinSessionButton.addEventListener('click', () => {
        sessionStorage.setItem('isInSession', 'true');
        sessionHandlers.addPlayerToSession(session.sid);
    });
    return joinSessionButton;
}

/**
 * Create get sessions view
 * @param sessions sessions data
 * @returns {HTMLDivElement} get sessions view
 */
function createGetSessionsView(sessions) {
    const container = views.div({class: "player-details-container"});
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
    const nextPrev = handlerViews.createPagination(query, "#sessions", sessions.length === constants.LIMIT);
    div.appendChild(sessionsElems);
    container.replaceChildren(div, nextPrev);
    return container;
}

/**
 * Create player list view
 * @param session session data
 * @returns {HTMLDivElement} player list view
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
 * @returns {HTMLDivElement} create session view
 */
function createCreateSessionView(gameName) {
    const container = views.div({class: "player-details-container"});
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
    container.replaceChildren(header, formContent);
    return container;
}

/**
 * Function to check if session can be updated
 * @param labelCapacity capacity
 * @param labelDate date
 * @param session session data
 * @returns {boolean} true if session can be updated
 */
function canUpdateSession(labelCapacity, labelDate, session) {
    return (parseInt(labelCapacity.value) !== session.capacity && labelCapacity.value.trim() !== "")
        || (labelDate.value !== session.date && labelDate.value.trim() !== "")

}

/**
 * Create the update session view
 * @param session session data
 * @returns {HTMLDivElement} update session view
 */
function createUpdateSessionView(session) {
    const container = views.div({class: "player-details-container"});
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

    container.replaceChildren(header, formContent);
    return container;
}

/**
 * Create update session button view
 * @param session
 * @returns {HTMLButtonElement}
 */
function createUpdateSessionButtonView(session) {
    const updateSessionButton = views.button({type: "submit", class: "general-button"}, "Update Session");
    updateSessionButton.addEventListener('click', (e) => {
        e.preventDefault();
        handlerUtils.changeHash("#updateSession/" + session.sid)
    });
    return updateSessionButton;
}

/**
 * Create delete or leave session button view
 * @param session session to delete or leave
 * @param isLeaveButton if true create leave button, else create delete button
 * @returns {*} delete session button view
 */
function createDeleteOrLeaveSessionButtonView(session, isLeaveButton = false) {
    const buttonText = isLeaveButton ? "Leave Session" : "Delete Session";
    const button = views.button({type: "submit", class: "general-button"}, buttonText);
    button.addEventListener('click', (e) => {
        e.preventDefault();
        const url = constants.API_BASE_URL + constants.SESSION_ID_ROUTE + session.sid;
        if (isLeaveButton) {
            sessionStorage.setItem('isInSession', 'false');
            sessionHandlers.removePlayerFromSession(session.sid);
        } else {
            sessionHandlers.deleteSession(session.sid);
        }
    });
    return button;
}

const sessionHandlerViews = {
    createSessionFormContentView,
    createSessionDetailsView,
    createGetSessionsView,
    createPlayerListView,
    createCreateSessionView,
    createUpdateSessionView,
}

export default sessionHandlerViews;

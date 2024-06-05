import views from "../viewsCreators.js";
import requestUtils from "../../utils/requestUtils.js";
import handlerViews from "./handlerViews.js";
import constants from "../../constants/constants.js";
import handlerUtils from "../../handlers/handlerUtils/handlerUtils.js";

/**
 * Create session form content view
 * @returns {HTMLDivElement} session form content view
 */
function createSearchSessionsView() {
    const container = views.div({class: "player-details-container"});
    const header = handlerViews.createHeader("Search Sessions");
    const gidInput = handlerViews.createLabeledInput("text", "gameName", "Enter Game name");
    const pidInput = handlerViews.createLabeledInput("text", "username", "Enter Player name");
    const dateInput = handlerViews.createLabeledInput("date", "date", "Enter Date");

    const stateLabel = views.h5({class: "w3-wide padding-left enter-state"}, "Enter State");
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
        views.div({class: "w3-row-padding w3-margin-bottom w3-center background-state"},
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
 * @param addPlayerToSession add player to session
 * @param removePlayerFromSession remove player from session
 * @param deleteSession delete session
 * @returns {HTMLDivElement} session details view
 */
function createSessionDetailsView(
    session,
    playerList,
    isOwner,
    isInSession,
    addPlayerToSession,
    removePlayerFromSession,
    deleteSession,
) {
    const container = views.div({class: "player-details-container"});
    const deleteSessionButton = createDeleteOrLeaveSessionButtonView(session, false, deleteSession);
    const leaveSessionButton = createDeleteOrLeaveSessionButtonView(session, true, undefined, removePlayerFromSession);
    const backToSessionsButton = handlerViews.createBackButtonView(sessionStorage.getItem('back'))
    const updateButton = createUpdateSessionButtonView(session);
    const joinSessionButton = createJoinSessionButtonView(session, addPlayerToSession);
    const div = views.div(
        {},
        handlerViews.createHeader(session.owner.username + "´s Session"),
        views.hr({class:"w3-opacity)"}),
        views.div({class: "w3-margin-bottom"},
            views.ul({class: "w3-ul w3-border w3-center w3-hover-shadow"},
                views.li(
                    views.div({id: "gameTest"}, views.h3({class: "w3-wide blue-letters"}, "Game"),
                        views.li(
                            ...handlerViews.hrefConstructor(
                                `#${constants.GAME_ROUTE}`,
                                session.gameInfo.gid,
                                `${session.gameInfo.name}`
                            )
                        ),
                    ),
                ),
                views.li(views.div({id: "dateTest"}, views.h3({class: "w3-wide blue-letters"}, "Date"), views.li(session.date))),
                views.li(views.div({id: "ownerTest"}, views.h3({class: "w3-wide blue-letters"}, "Owner"), views.li(session.owner.username))),
                views.li(views.div({id: "capacityTest"}, views.h3({class: "w3-wide blue-letters"}, "Capacity"), views.li(session.capacity.toString()))),
                views.li(views.div({id: "playersTest"}, views.h3({class: "w3-wide blue-letters"}, "Players"), playerList)),
            ),
        )
    );

    if (isOwner) {
        div.appendChild(deleteSessionButton);
        div.appendChild(views.p());
        div.appendChild(updateButton);
        div.appendChild(views.p());
    } else if (isInSession){
        div.appendChild(leaveSessionButton);
        div.appendChild(views.p())
    } else {
        const date = new Date(session.date);
        const currentDate = new Date();
        if (currentDate <= date) {
            div.appendChild(joinSessionButton);
            div.appendChild(views.p())
        }
    }
    div.appendChild(backToSessionsButton);
    container.replaceChildren(div);
    return container;
}


/**
 * Create join session button view
 * @param session session to join
 * @param addPlayerToSession add player to session function
 * @returns {HTMLButtonElement} join session button view
 */
function createJoinSessionButtonView(session, addPlayerToSession) {
    const joinSessionButton = views.button(
        {type: "submit", class: "general-button", id: "joinButtonTest"},
        "Join Session"
    );
    joinSessionButton.addEventListener('click', () => {
        if (addPlayerToSession) {
            addPlayerToSession(session.sid)
        }
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
        handlerViews.createHeader("Sessions Found"),
        views.hr({class:"w3-opacity"})
    );
    const sessionsElems = views.ul({class: "centered-list w3-ul w3-border w3-center w3-hover-shadow"});
    sessions
        .slice(0, constants.ELEMENTS_PER_PAGE)
        .forEach(session => {
        const sessionHref =
            views.li(
                ...handlerViews.hrefConstructor(
                `#${constants.SESSION_ROUTE}`,
                session.sid, session.owner.username + "´s Session" + " - " + session.date,
                0,
            ));
        sessionsElems.appendChild(sessionHref);
    });
    const nextPrev = handlerViews.createPagination(query, `#${constants.SESSION_ROUTE}`, sessions.length === constants.LIMIT);
    div.appendChild(sessionsElems);
    container.replaceChildren(div, nextPrev);
    sessionStorage.setItem('back', window.location.hash);
    return container;
}

/**
 * Create player list view
 * @param session session data
 * @param removePlayerFromSession remove player from session function
 * @returns {HTMLDivElement} player list view
 */
function createPlayerListView(session, removePlayerFromSession = undefined) {
    const div = views.div({class: "pagination-players-min-height"})
    const playerList = views.ul({class:"pagination-players-min-height"});
    if (session.players) {
        session.players
            .slice(0, constants.ELEMENTS_PER_PAGE_PLAYERS)
            .forEach(player => {
                if (!removePlayerFromSession || removePlayerFromSession && player.pid === parseInt(sessionStorage.getItem('pid'))) {
                    const playerLi = views.li(
                        ...handlerViews.hrefConstructor(`#${constants.PLAYER_ROUTE}`, player.pid, player.username)
                    );
                    playerList.appendChild(playerLi);
                } else {
                    const button = views.button(
                        {type: "click", class: "remove-button", id: "remove_player", value: player.pid},
                        "x"
                    )
                    button.addEventListener('click', () => {
                        removePlayerFromSession(session.sid, player.pid)
                    });
                    const playerLi = views.li(
                        views.div(
                            {class: "player-list"},
                            ...handlerViews.hrefConstructor(`#${constants.PLAYER_ROUTE}`, player.pid, player.username),
                            button
                        )
                    )
                    playerList.appendChild(playerLi);
                }
        });
    }
    div.appendChild(playerList);
    const nextPrev = handlerViews.createPagination(
        requestUtils.getQuery(),
        `${constants.SESSION_ROUTE}/${session.sid}`,
        session.players !== undefined && session.players.length >= constants.LIMIT_PLAYERS,
        constants.ELEMENTS_PER_PAGE_PLAYERS
    );
    div.appendChild(nextPrev)
    return div;
}

/**
 * Function to check if session can be created
 *
 * @param labelCapacity capacity
 * @param labelDate date
 * @returns {boolean} true if session can be created
 */
function canCreateSession(labelCapacity, labelDate) {
    return labelCapacity.value.trim() !== "" && labelDate.value.trim() !== "";
}

/**
 * Create the create session view
 * @param gameName game name
 * @returns {HTMLDivElement} create session view
 */
function createCreateSessionView(gameName) {
    const container = views.div({class: "player-details-container"});
    const header = handlerViews.createHeader("Create Session");
    const labelCapacity = handlerViews.createLabeledInput("number", "capacity", "Enter Capacity");
    const labelDate = handlerViews.createLabeledInput("date", "dateCreate", "Enter Date");
    const createSessionButton = views.button({
        type: "submit",
        class: "general-button",
        disabled: true
    }, "Create");

    const toggleCreateSessionButton = () => {
        handlerViews.toggleButtonState(
            createSessionButton,
            !canCreateSession(labelCapacity, labelDate)
        )
    }

    handlerViews.addToggleEventListeners(
        toggleCreateSessionButton,
        labelCapacity,
        labelDate
    );

    const formContent = views.form(
        {},
        views.hr({class:"w3-opacity"}),
        views.h4({class: "w3-wide blue-letters"}, "Game"),
        views.p({id: "gameNameTest"}, gameName.toString()),
        views.h4({class: "w3-wide blue-letters"}, "Capacity"),
        labelCapacity,
        views.p(),
        views.h4({class: "w3-wide blue-letters"}, "Date"),
        labelDate,
        views.p(),
        createSessionButton
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
    const header = handlerViews.createHeader("Update Session");
    const labelCapacity = handlerViews.createLabeledInput("number", "capacity", "Enter Capacity", session.capacity);
    const labelDate = handlerViews.createLabeledInput("date", "dateChange", "Enter Date", session.date);

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
        views.p({id: "gameNameTest"}, session.gameInfo.name),
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
    const updateSessionButton = views.button({type: "submit", class: "general-button", id: "updateButtonTest"}, "Update Session");
    updateSessionButton.addEventListener('click', (e) => {
        e.preventDefault();
        handlerUtils.changeHash(`updateSession/${session.sid}`)
    });
    return updateSessionButton;
}

/**
 * Create delete or leave session button view
 * @param session session to delete or leave
 * @param isLeaveButton if true create leave button, else create delete button
 * @param deleteSession delete session function
 * @param removePlayerFromSession remove player from session function
 * @returns {*} delete session button view
 */
function createDeleteOrLeaveSessionButtonView(
    session,
    isLeaveButton = false,
    deleteSession = undefined,
    removePlayerFromSession = undefined
) {
    const buttonText = isLeaveButton ? "Leave Session" : "Delete Session";
    const buttonId = isLeaveButton ? "leaveButtonTest" : "deleteButtonTest";
    const button = views.button({type: "submit", class: "general-button", id: buttonId}, buttonText);
    button.addEventListener('click', (e) => {
        e.preventDefault();
        if (isLeaveButton) {
            sessionStorage.setItem('isInSession', 'false');
            if (removePlayerFromSession) {
                removePlayerFromSession(session.sid);
            }
        } else {
            if (deleteSession) {
                deleteSession(session.sid);
            }
        }
    });
    return button;
}

const sessionHandlerViews = {
    createSearchSessionsView,
    createSessionDetailsView,
    createGetSessionsView,
    createPlayerListView,
    createCreateSessionView,
    createUpdateSessionView,
}

export default sessionHandlerViews;

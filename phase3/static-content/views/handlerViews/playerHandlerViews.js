import constants from "../../constants/constants.js";
import handlerViews from "./handlerViews.js";
import views from "../viewsCreators.js";

/**
 * Create player details view
 * @param player player data
 * @param backButton if true, create back button
 * @returns {*} player details view
 */
function createPlayerDetailsView(player, backButton = true) {
    const headerText = backButton ? "Player Details" : "Your Information";
    const sessionText = backButton ? "View Player's Sessions" : "Your Sessions";
    const hr = views.hr({class:"w3-opacity"})
    const container = views.div({class: "player-details-container"});

    const header = handlerViews.createHeader(headerText);

    const detailsList = views.ul({class: "w3-ul w3-border w3-center w3-hover-shadow"},
        views.li(views.h3({class: "w3-wide blue-letters"}, "Name")),
        views.li(player.name),
        views.li(views.h3({class: "w3-wide blue-letters"}, "Username")),
        views.li(player.userName),
        views.li(views.h3({class: "w3-wide blue-letters"}, "Email")),
        views.li(player.email),
    );

    const sessionsButton = handlerViews.hrefButtonView(
        sessionText,
        `${constants.SESSION_ROUTE}?pid=${player.pid}&offset=0`
    );

    container.replaceChildren(header, hr, detailsList, views.p());

    if (backButton) {
        const backButtonView = handlerViews.createBackButtonView();
        container.appendChild(sessionsButton);
        container.appendChild(views.p());
        container.appendChild(backButtonView);
    } else {
        const createSessionHref = handlerViews.hrefButtonView("Choose a game to create a session", `#gameSearch`);
        container.appendChild(sessionsButton);
        container.appendChild(views.p());
        container.appendChild(createSessionHref);
    }
    return container;
}

/**
 * Create search player view
 * @returns {*[]}
 */
function createSearchPlayerView() {
    const h1 = handlerViews.createHeader("Search Player")
    const playerInput =
        views.input({type: "text", id: "pid", placeholder: "username"});
    const searchButton =
        views.button(
            {type: "submit", class: "general-button", disabled: true},
            "Player Details"
        );

    const toggleSearchPlayerButton = () => {
        handlerViews.toggleButtonState(searchButton, !playerInput.value.trim());
    }

    playerInput.addEventListener("input", toggleSearchPlayerButton);

    const form =
        views.form(
            {action: "#playerDetails", method: "get"},
            views.hr({class: "w3-opacity"}),
            playerInput,
            views.p(),
            searchButton
        );
    return [h1, form];
}

const playerHandlerViews = {
    createPlayerDetailsView,
    createSearchPlayerView,
}
export default playerHandlerViews;
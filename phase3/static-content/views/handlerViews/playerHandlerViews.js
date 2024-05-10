import constants from "../../constants/constants.js";
import views from "../viewsCreators.js";
import handlerViews from "./handlerViews.js";

function createPlayerDetailsView(player, backButton = true) {
    const headerText = backButton ? "Player Details:" : "Your Information:";
    const container = views.div({class: "player-details-container"});

    const header = views.h2({class: "player-details-header"}, headerText);

    const detailsList = views.ul({class: "player-details-list"},
        views.li( "UserName: " + player.userName),
        views.li( "Email: " + player.email),
    );

    const nameHeader = views.h3({class: "player-name-header"}, player.name);

    const sessionsButton = handlerViews.hrefButtonView(
        "Sessions",
        `${constants.SESSION_ROUTE}?pid=${player.pid}&offset=0`
    );

    container.replaceChildren(header, nameHeader, detailsList, sessionsButton);

    if (backButton) {
        const backButtonView = handlerViews.createBackButtonView();
        container.appendChild(backButtonView);
    }

    return container;
}

/**
 * Create search player view
 *
 * @returns {*[]}
 */
function createSearchPlayerView() {
    const h1 = handlerViews.createHeader("Search Player: ")
    const playerInput =
        views.input({type: "text", id: "pid", maxLength: 10, placeholder: "Player username"});
    const searchButton =
        views.button(
            {type: "submit", class: "submit-button", disabled: true},
            "Player Details"
        );

    const toggleSearchPlayerButton = () => {
        handlerViews.toggleButtonState(searchButton, !playerInput.value.trim());
    }

    playerInput.addEventListener("input", toggleSearchPlayerButton);

    const form =
        views.form(
            {action: "#playerDetails", method: "get"},
            playerInput,
            searchButton
        );
    return [h1, form];
}


const playerHandlerViews = {
    createPlayerDetailsView,
    createSearchPlayerView
}
export default playerHandlerViews;
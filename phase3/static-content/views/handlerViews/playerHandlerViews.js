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


const playerHandlerViews = {
    createPlayerDetailsView
}
export default playerHandlerViews;
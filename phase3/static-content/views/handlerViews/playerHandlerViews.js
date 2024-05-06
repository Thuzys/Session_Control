import constants from "../../constants/constants.js";
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

    container.appendChild(header);
    container.appendChild(nameHeader);
    container.appendChild(detailsList);
    container.appendChild(sessionsButton);

    if (backButton) {
        const backButtonView = handlerViews.createBackButtonView();
        container.appendChild(backButtonView);
    }

    return container;
}

import views from "../viewsCreators.js";

import handlerViews from "./handlerViews.js";

const playerHandlerViews = {
    createPlayerDetailsView
}
export default playerHandlerViews;
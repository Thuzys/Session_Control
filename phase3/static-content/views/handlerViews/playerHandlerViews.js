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
    const hr = views.hr({class:"w3-opacity"})
    const container = views.div({class: "player-details-container"});

    const header = handlerViews.createHeader(headerText);

    const detailsList = views.ul({class: "w3-ul w3-border w3-center w3-hover-shadow"},
        views.li(views.h3({class: "w3-wide blue-letters"}, "Username")),
        views.li(player.userName),
        views.li(views.h3({class: "w3-wide blue-letters"}, "Email")),
        views.li(player.email),
    );

    const nameHeader = views.h3({class: "w3-wide centered blue-letters"}, player.name);

    const sessionsButton = handlerViews.hrefButtonView(
        "Sessions",
        `${constants.SESSION_ROUTE}?pid=${player.pid}&offset=0`
    );

    container.replaceChildren(header, hr, nameHeader, detailsList, views.p(), sessionsButton, views.p());

    if (backButton) {
        const backButtonView = handlerViews.createBackButtonView();
        container.appendChild(backButtonView);
    } else {
        const createSessionHref = handlerViews.hrefButtonView("Choose a game to create a session", `#gameSearch`);
        container.appendChild(createSessionHref);
    }

    return container;
}

/**
 * Create search player view
 * @returns {*[]} search player view
 */
function createSearchPlayerView() {
    const h1 = handlerViews.createHeader("Search Player");
    const form =
        views.form({action: "#playerDetails", method: "get"},
            views.hr({class: "w3-opacity"}),
            views.input({type: "text", id: "pid", maxLength: 10, placeholder: "username"}),
            views.p(),
            views.button({type: "submit", class: "general-button"}, "Search")
        );
    return [h1, form];
}

const playerHandlerViews = {
    createPlayerDetailsView,
    createSearchPlayerView
}
export default playerHandlerViews;
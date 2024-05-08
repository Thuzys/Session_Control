import constants from "../../constants/constants.js";
import views from "../viewsCreators.js";
import handlerViews from "./handlerViews.js";

function createPlayerDetailsView(player, backButton = true) {
    const h2 = views.h2({}, "Player Details");
    const playerDetailsView = views.ul(
        views.li("Username: " + player.userName),
        views.li("Email: " + player.email),
    );
    const backButtonView = handlerViews.createBackButtonView();
    const sessionsButtonView =
        handlerViews
            .sessionsButtonView(
                "Sessions",
                `${constants.SESSION_ROUTE}?pid=` + player.pid + "&offset=0"
            );
    const div  = views.div({},
        h2,
        views.h3({}, player.name),
        playerDetailsView,
        sessionsButtonView,
    );
    if (backButton) {
        div.appendChild(backButtonView);
    }
    return div;
}

const playerHandlerViews = {
    createPlayerDetailsView
}
export default playerHandlerViews;
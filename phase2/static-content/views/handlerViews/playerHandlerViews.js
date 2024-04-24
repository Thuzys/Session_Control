function createPlayerDetailsView(player) {
    const h2 = views.h2({}, "Player Details");
    const playerDetailsView = views.ul(
        views.li("Name: " + player.name),
        views.li("Email: " + player.email.email),
        views.li("Pid: " + player.pid),
    );
    const backButtonView = handlerViews.createBackButtonView();
    const sessionsButtonView = handlerViews.sessionsButtonView("Sessions", "sessions?pid=" + player.pid + "&offset=0");
    const gamesButtonView = handlerViews.gamesButtonView("Games", "games?pid=" + player.pid + "&offset=0");
    return views.div({},
        h2,
        playerDetailsView,
        backButtonView,
        sessionsButtonView,
        gamesButtonView,
    );
}
import views from "../viewsCreators.js";

import handlerViews from "./handlerViews.js";

const playerHandlerViews = {
    createPlayerDetailsView
}
export default playerHandlerViews;
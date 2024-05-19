import router from "./router/router.js";
import requestUtils from "./utils/requestUtils.js";
import playerHandlers from "./handlers/playerHandlers.js";
import sessionHandlers from "./handlers/sessionHandlers.js";
import gameHandlers from "./handlers/gameHandlers.js";
import contactHandlers from "./handlers/contactHandlers.js";
import navigationViews from "./navigation/navigationViews.js";
import homeHandlers from "./handlers/homeHandlers.js";

window.addEventListener('load', loadHandler)
window.addEventListener('hashchange', hashChangeHandler)
// window.addEventListener('beforeunload', function () {
//     homeHandlers.logOut()
// });

/**
 * Load handler routes
  */
function loadHandler(){
    router.addRouteHandler("logIn", homeHandlers.logIn)
    router.addRouteHandler("register", homeHandlers.register)
    router.addRouteHandler("logOut", homeHandlers.logOut)
    router.addRouteHandler("players/home", homeHandlers.getHome)
    router.addRouteHandler("playerSearch", playerHandlers.searchPlayer)
    router.addRouteHandler("createGame", gameHandlers.createGame)
    router.addRouteHandler("players", playerHandlers.getPlayerDetails)
    router.addRouteHandler("gameSearch", gameHandlers.searchGames)
    router.addRouteHandler("games", gameHandlers.getGames)
    router.addRouteHandler("games/:gid", gameHandlers.getGameDetails)
    router.addRouteHandler("players/:pid", playerHandlers.getPlayerDetailsByPid)
    router.addRouteHandler("sessionSearch", sessionHandlers.searchSessions)
    router.addRouteHandler("updateSession/:sid", sessionHandlers.updateSession)
    router.addRouteHandler("sessions", sessionHandlers.getSessions)
    router.addRouteHandler("sessions/:sid", sessionHandlers.getSessionDetails)
    router.addRouteHandler("contacts", contactHandlers.getContacts)
    router.addDefaultNotFoundRouteHandler((dummy1, dummy2) => window.location.hash = "logIn")

    hashChangeHandler()
}

const routesRequiringAuth = [
    "playerSearch",
    "createGame",
    "players",
    "gameSearch",
    "games",
    "games/:gid",
    "players/:pid",
    "sessionSearch",
    "updateSession/:sid",
    "sessions",
    "sessions/:sid",
    "contacts"
];

/**
 * Hash change handler
 */
function hashChangeHandler(){
    const navigationBar = navigationViews.createNavigationBarView();
    if( sessionStorage.getItem('isAuthenticated') === 'true' ) {
        document.body.insertBefore(navigationBar, document.getElementById("mainContent"));
    }
    const mainContent = document.getElementById("mainContent")
    const path =  requestUtils.getPath()
    const handler = router.getRouteHandler(path)

    if (routesRequiringAuth.includes(path)) {
        const isAuthenticated = sessionStorage.getItem('isAuthenticated') === 'true';
        if (!isAuthenticated) {
            window.location.hash = "logIn";
            return;
        }
    }

    handler(mainContent)
}


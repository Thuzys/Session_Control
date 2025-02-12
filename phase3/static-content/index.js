import router from "./router/router.js";
import requestUtils from "./utils/requestUtils.js";
import playerHandlers from "./handlers/playerHandlers.js";
import sessionHandlers from "./handlers/sessionHandlers.js";
import gameHandlers from "./handlers/gameHandlers.js";
import contactHandlers from "./handlers/contactHandlers.js";

window.addEventListener('load', loadHandler)
window.addEventListener('hashchange', hashChangeHandler)

/**
 * Load handler routes
  */
function loadHandler(){
    router.addRouteHandler("players/home", playerHandlers.getHome)
    router.addRouteHandler("playerSearch", playerHandlers.searchPlayer)
    router.addRouteHandler("players", playerHandlers.getPlayerDetails)
    router.addRouteHandler("gameSearch", gameHandlers.searchGames)
    router.addRouteHandler("games", gameHandlers.getGames)
    router.addRouteHandler("games/:gid", gameHandlers.getGameDetails)
    router.addRouteHandler("players/:pid", playerHandlers.getPlayerDetailsByPid)
    router.addRouteHandler("sessionSearch", sessionHandlers.searchSessions)
    router.addRouteHandler("updateSession", sessionHandlers.updateSession)
    router.addRouteHandler("sessions", sessionHandlers.getSessions)
    router.addRouteHandler("sessions/:sid", sessionHandlers.getSessionDetails)
    router.addRouteHandler("contacts", contactHandlers.getContacts)
    router.addDefaultNotFoundRouteHandler((dummy1, dummy2) => window.location.hash = "players/home")

    hashChangeHandler()
}

/**
 * Handle hash change event
 */
function hashChangeHandler(){
    const mainContent = document.getElementById("mainContent")
    const mainHeader = document.getElementById("mainHeader")
    const path =  requestUtils.getPath()
    const handler = router.getRouteHandler(path)
    handler(mainContent, mainHeader)
}

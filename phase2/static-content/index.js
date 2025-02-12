import router from "./router/router.js";
import handlers from "./handlers/handlers.js";
import requestUtils from "./utils/requestUtils.js";
import playerHandlers from "./handlers/playerHandlers.js";
import sessionHandlers from "./handlers/sessionHandlers.js";
import gameHandlers from "./handlers/gameHandlers.js";

window.addEventListener('load', loadHandler)
window.addEventListener('hashchange', hashChangeHandler)

function loadHandler(){
    router.addRouteHandler("players/home", handlers.getHome)
    router.addRouteHandler("gameSearch", gameHandlers.searchGames)
    router.addRouteHandler("games", gameHandlers.getGames)
    router.addRouteHandler("games/:gid", gameHandlers.getGameDetails)
    router.addRouteHandler("players/:pid", playerHandlers.getPlayerDetails)
    router.addRouteHandler("sessionSearch", sessionHandlers.searchSessions)
    router.addRouteHandler("sessions", sessionHandlers.getSessions)
    router.addRouteHandler("sessions/:sid", sessionHandlers.getSessionDetails)
    router.addDefaultNotFoundRouteHandler((dummy1, dummy2) => window.location.hash = "players/home")

    hashChangeHandler()
}

function hashChangeHandler(){
    const mainContent = document.getElementById("mainContent")
    const mainHeader = document.getElementById("mainHeader")
    const path =  requestUtils.getPath()
    const handler = router.getRouteHandler(path)
    handler(mainContent, mainHeader)
}

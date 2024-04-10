import router from "./router/router.js";
import handlers from "./handlers/handlers.js";
import requestUtils from "./utils/requestUtils.js";
import playerHandlers from "./handlers/playerHandlers.js";
import sessionHandlers from "./handlers/sessionHandlers.js";

window.addEventListener('load', loadHandler)
window.addEventListener('hashchange', hashChangeHandler)

function loadHandler(){

    router.addRouteHandler("home", handlers.getHome)
    router.addRouteHandler("playerHome", handlers.getHome)
    router.addRouteHandler("gameSearch", handlers.searchGames)
    router.addRouteHandler("games", handlers.getGames)
    router.addRouteHandler("games/:gid", handlers.getGameDetails)
    router.addDefaultNotFoundRouteHandler(() => window.location.hash = "playerHome")
    router.addRouteHandler("playerHome", handlers.getHome)
    router.addRouteHandler("playerDetails/:pid", playerHandlers.getPlayerDetails)
    router.addRouteHandler("sessionSearch", sessionHandlers.searchSessions)
    router.addRouteHandler("sessions", sessionHandlers.getSessions)
    router.addRouteHandler("sessionDetails/:sid", sessionHandlers.getSessionDetails)
    router.addDefaultNotFoundRouteHandler((dummy1, dummy2) => window.location.hash = "home")

    hashChangeHandler()
}

function hashChangeHandler(){
    const mainContent = document.getElementById("mainContent")
    const mainHeader = document.getElementById("mainHeader")
    const path =  requestUtils.getPath()
    const handler = router.getRouteHandler(path)
    handler(mainContent, mainHeader)
}

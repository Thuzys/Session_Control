import router from "./router.js";
import handlers from "./handlers.js";
import RequestUtils from "./requestUtils.js";

// For more information on ES6 modules, see https://www.javascripttutorial.net/es6/es6-modules/ or
// https://www.w3schools.com/js/js_modules.asp

window.addEventListener('load', loadHandler)
window.addEventListener('hashchange', hashChangeHandler)

function loadHandler(){

    router.addRouteHandler("home", handlers.getHome)
    router.addDefaultNotFoundRouteHandler(() => window.location.hash = "home")
    router.addRouteHandler("playerHome", handlers.getHome)
    router.addRouteHandler("gameSearch", handlers.gameSearchHandler)
    router.addRouteHandler("games", handlers.getGamesHandler)
    router.addRouteHandler("games/:gid", handlers.getGameDetailsHandler)
    router.addDefaultNotFoundRouteHandler(() => window.location.hash = "playerHome")

    hashChangeHandler()
}

function hashChangeHandler(){
    const mainContent = document.getElementById("mainContent")
    const path = RequestUtils.getPath()
    const handler = router.getRouteHandler(path)
    handler(mainContent)
}
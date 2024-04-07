import router from "./router.js";
import handlers from "./handlers.js";

// For more information on ES6 modules, see https://www.javascripttutorial.net/es6/es6-modules/ or
// https://www.w3schools.com/js/js_modules.asp

window.addEventListener('load', loadHandler)
window.addEventListener('hashchange', hashChangeHandler)

function loadHandler(){

    router.addRouteHandler("home", handlers.getHome)
    router.addRouteHandler("sessionSearch", handlers.searchSessions)
    router.addDefaultNotFoundRouteHandler(() => window.location.hash = "home")
    router.addRouteHandler("playerHome", handlers.getHome)
    router.addRouteHandler("gameSearch", handlers.searchGames)
    router.addRouteHandler("games", handlers.getGames)
    router.addRouteHandler("games/:gid", handlers.getGameDetails)
    router.addDefaultNotFoundRouteHandler(() => window.location.hash = "playerHome")

    hashChangeHandler()
}

function hashChangeHandler(){
    const mainContent = document.getElementById("mainContent")
    const path = getPath()
    const handler = router.getRouteHandler(path)
    handler(mainContent)
}

function getPath(){
    return window.location.hash.replace("#", "").split("?")[0]
}
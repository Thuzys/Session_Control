import router from "./router.js";
import handlers from "./handlers.js";
import utils from "./utils.js";

// For more information on ES6 modules, see https://www.javascripttutorial.net/es6/es6-modules/ or
// https://www.w3schools.com/js/js_modules.asp

window.addEventListener('load', loadHandler)
window.addEventListener('hashchange', hashChangeHandler)

function loadHandler(){

    router.addRouteHandler("playerHome", handlers.getHome)
    router.addRouteHandler("playerDetails", handlers.getPlayerDetails)
    router.addDefaultNotFoundRouteHandler((dummy1, dummy2) => window.location.hash = "home")

    hashChangeHandler()
}

function hashChangeHandler(){
    const mainContent = document.getElementById("mainContent")
    const mainHeader = document.getElementById("mainHeader")
    const path =  utils.getPath()
    const handler = router.getRouteHandler(path)
    handler(mainContent, mainHeader)
}

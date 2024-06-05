import router from "./router/router.js";
import requestUtils from "./utils/requestUtils.js";
import playerHandlers from "./handlers/playerHandlers.js";
import sessionHandlers from "./handlers/sessionHandlers.js";
import gameHandlers from "./handlers/gameHandlers.js";
import contactHandlers from "./handlers/contactHandlers.js";
import navigationViews from "./navigation/navigationViews.js";
import homeHandlers from "./handlers/homeHandlers.js";
import {fetcher} from "./utils/fetchUtils.js";
import constants from "./constants/constants.js";
import createBB8Toggle from "./views/handlerViews/switchLightModeView.js";
import handlerUtils from "./handlers/handlerUtils/handlerUtils.js";

window.addEventListener('load', loadHandler)
window.addEventListener('hashchange', hashChangeHandler)

/**
 * Create a toggle switch for light mode and dark mode
 *
 * @type {HTMLLabelElement}
 */
const bb8Toggle = createBB8Toggle();
window.onload = function() {
    if (localStorage.getItem('lightMode') === 'disabled') {
        bb8Toggle.querySelector('.bb8-toggle__checkbox').checked = true;
    }
    document.body.appendChild(bb8Toggle);
}

/**
 * Load handler routes
 */
function loadHandler() {
    const url = handlerUtils.createURL(constants.API_GENRES_ROUTE);
    fetcher.get(url, undefined, false).then(data => {
        sessionStorage.setItem('genres', JSON.stringify(data))
        router.addRouteHandler(constants.LOGIN_ROUTE, homeHandlers.logIn)
        router.addRouteHandler(constants.REGISTER_ROUTE, homeHandlers.register)
        router.addRouteHandler(constants.LOGOUT_ROUTE, homeHandlers.logOut)
        router.addRouteHandler(constants.PLAYERS_HOME_ROUTE, homeHandlers.getHome)
        router.addRouteHandler(constants.PLAYER_SEARCH_ROUTE, playerHandlers.searchPlayer)
        router.addRouteHandler(constants.CREATE_GAME_ROUTE, gameHandlers.createGame)
        router.addRouteHandler(constants.PLAYER_ROUTE, playerHandlers.getPlayerDetails)
        router.addRouteHandler(constants.GAME_SEARCH_ROUTE, gameHandlers.searchGames)
        router.addRouteHandler(constants.GAME_ROUTE, gameHandlers.getGames)
        router.addRouteHandler(constants.GAMES_ID_ROUTE, gameHandlers.getGameDetails)
        router.addRouteHandler(constants.PLAYERS_ID_ROUTE, playerHandlers.getPlayerDetailsByPid)
        router.addRouteHandler(constants.SESSION_SEARCH_ROUTE, sessionHandlers.searchSessions)
        router.addRouteHandler(constants.UPDATE_SESSION_ROUTE, sessionHandlers.updateSession)
        router.addRouteHandler(constants.SESSION_ROUTE, sessionHandlers.getSessions)
        router.addRouteHandler(constants.SESSIONS_ID_ROUTE, sessionHandlers.getSessionDetails)
        router.addRouteHandler(constants.CONTACTS_ROUTE, contactHandlers.getContacts)
        router.addDefaultNotFoundRouteHandler((_, _1) => {
            window.location.hash = sessionStorage.getItem('isAuthenticated') === 'true' ?
                constants.PLAYERS_HOME_ROUTE : constants.LOGIN_ROUTE;
        })
        hashChangeHandler()
    })
}

/**
 * Routes that require authentication
 *
 * @type {string[]} routes that require authentication
 */
const routesRequiringAuth = [
    constants.PLAYER_SEARCH_ROUTE,
    constants.CREATE_GAME_ROUTE,
    constants.PLAYER_ROUTE,
    constants.GAME_SEARCH_ROUTE,
    constants.GAME_ROUTE,
    constants.GAMES_ID_ROUTE,
    constants.PLAYERS_ID_ROUTE,
    constants.SESSION_SEARCH_ROUTE,
    constants.UPDATE_SESSION_ROUTE,
    constants.SESSION_ROUTE,
    constants.SESSIONS_ID_ROUTE,
    constants.CONTACTS_ROUTE,
    constants.LOGOUT_ROUTE,
    constants.PLAYERS_HOME_ROUTE
];

/**
 * Hash change handler
 */
function hashChangeHandler() {
    const existingNavigationBar = document.getElementById('navBar');
    if (!existingNavigationBar && sessionStorage.getItem('isAuthenticated') === 'true') {
        const navigationBar = navigationViews.createNavigationBarView();
        document.body.insertBefore(navigationBar, document.getElementById("mainContent"));
    }
    if (!location.hash.includes('sessions/')) {
        sessionStorage.removeItem('isInSession');
    }

    const mainContent = document.getElementById("mainContent")
    const path = requestUtils.getPath()
    const handler = router.getRouteHandler(path)

    if (routesRequiringAuth.includes(path)) {
        const isAuthenticated = sessionStorage.getItem('isAuthenticated') === 'true';
        if (!isAuthenticated) {
            handlerUtils.changeHash(constants.LOGIN_ROUTE)
            return;
        }
    }

    handler(mainContent)
}


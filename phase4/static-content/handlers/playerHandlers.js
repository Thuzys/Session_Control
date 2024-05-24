import requestUtils from "../utils/requestUtils.js";
import constants from "../constants/constants.js";
import playerHandlerViews from "../views/handlerViews/playerHandlerViews.js";
import {fetcher} from "../utils/fetchUtils.js";
import handlerUtils from "./handlerUtils/handlerUtils.js";

/**
 * Get player details by player id
 *
 * @param mainContent main content of the page
 */
function getPlayerDetailsByPid(mainContent) {
    const url = `${constants.API_BASE_URL}${constants.PLAYER_ID_ROUTE}${requestUtils.getParams()}`;
    const token = sessionStorage.getItem('token');
    fetcher
        .get(url, token)
        .then(
            response =>
                handleGetPlayerDetailsResponse(response, mainContent, true)
        );
}

/**
 * Get player details
 *
 * @param mainContent main content of the page
 */
function getPlayerDetails(mainContent) {
    const route = constants.PLAYER_ID_ROUTE.substring(0, constants.PLAYER_ID_ROUTE.length - 1);
    const url = `${constants.API_BASE_URL}${route}?${handlerUtils.makeQueryString(requestUtils.getQuery())}`;
    const token = sessionStorage.getItem('token');
    fetcher
        .get(url, token)
        .then(
            response =>
                handleGetPlayerDetailsResponse(response, mainContent, true)
        );
}

/**
 * Handle player details response
 *
 * @param player response from the server
 * @param mainContent main content of the page
 * @param isSearch boolean to check if the response is from a search
 */
function handleGetPlayerDetailsResponse(player, mainContent, isSearch = false) {
    const playerDetailsView = playerHandlerViews.createPlayerDetailsView(player, isSearch);
    mainContent.replaceChildren(playerDetailsView);
}

/**
 * Search player
 * @param mainContent main content of the page
 */
function searchPlayer(mainContent) {
    const container = playerHandlerViews.createSearchPlayerView();
    container.onsubmit = (e) => handlePlayerSearchSubmit(e);
    mainContent.replaceChildren(container);
}

/**
 * Handle player details home submit.
 *
 * @param e event that triggered submit
 */
function handlePlayerSearchSubmit(e) {
    e.preventDefault();
    const pid = document.getElementById("pid");
    if (pid.value === "") {
        return;
    }
    handlerUtils.changeHash(`#players?username=${pid.value}`);
}


export default {
    getPlayerDetails,
    searchPlayer,
    getPlayerDetailsByPid,
    handleGetPlayerDetailsResponse
}
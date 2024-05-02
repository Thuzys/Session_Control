import requestUtils from "../utils/requestUtils.js";
import menu from "../navigation/menuLinks.js";
import constants from "../constants/constants.js";
import playerHandlerViews from "../views/handlerViews/playerHandlerViews.js";
import {fetcher} from "../utils/fetchUtils.js";
import handlerViews from "../views/handlerViews/handlerViews.js";
import handlerUtils from "./handlerUtils/handlerUtils.js";

/**
 * Get player details
 *
 * @param mainContent main content of the page
 * @param mainHeader main header of the page
 */
function getPlayerDetails(mainContent, mainHeader) {
    const url = `${constants.API_BASE_URL}${constants.PLAYER_ID_ROUTE}${requestUtils.getParams()}`;
    fetcher
        .get(url, constants.TOKEN)
        .then(
            response =>
                handleGetPlayerDetailsResponse(response, mainContent, mainHeader, true)
        );
}

function searchPlayer(mainContent, mainHeader) {
    const [h1, form] = handlerViews.createHomeView();
    form.onsubmit = (e) => handleHomeSubmit(e);
    mainContent.replaceChildren(h1, form);
    mainHeader.replaceChildren(menu.get("sessionSearch"), menu.get("home"),  menu.get("gameSearch"));
}

/**
 * Handle player details response
 *
 * @param player response from the server
 * @param mainContent main content of the page
 * @param mainHeader the main header of the page
 * @param isSearch boolean to check if the response is from a search
 */
function handleGetPlayerDetailsResponse(player, mainContent, mainHeader, isSearch = false) {
    const playerDetailsView = playerHandlerViews.createPlayerDetailsView(player, isSearch);
    mainContent.replaceChildren(playerDetailsView);
    if (isSearch) {
        mainHeader.replaceChildren(menu.get("sessionSearch"), menu.get("home"), menu.get("gameSearch"));
    } else {
        mainHeader.replaceChildren(menu.get("sessionSearch"), menu.get("gameSearch"), menu.get("playerSearch"));
    }
}

function getHome(mainContent, mainHeader) {
    const url = `${constants.API_BASE_URL}${constants.PLAYER_ID_ROUTE}1`;
    fetcher
        .get(url, constants.TOKEN)
        .then(
            response =>
                handleGetPlayerDetailsResponse(response, mainContent, mainHeader)
        );
}

/**
 * Handle player details home submit.
 *
 * @param e
 */
function handleHomeSubmit(e) {
    e.preventDefault();
    const pid = document.getElementById("pid");
    if (pid.value === "") {
        alert("Please enter a player id");
        return;
    }
    handlerUtils.changeHash(`#players/${pid.value}`);
}


export default {
    getPlayerDetails,
    searchPlayer,
    getHome
}
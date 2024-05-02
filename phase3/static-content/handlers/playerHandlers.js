import requestUtils from "../utils/requestUtils.js";
import menu from "../navigation/menuLinks.js";
import constants from "../constants/constants.js";
import playerHandlerViews from "../views/handlerViews/playerHandlerViews.js";
import {fetcher} from "../utils/fetchUtils.js";

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
                handleGetPlayerDetailsResponse(response, mainContent, mainHeader)
        );
}

/**
 * Handle player details response
 *
 * @param player response from the server
 * @param mainContent main content of the page
 * @param mainHeader the main header of the page
 */
function handleGetPlayerDetailsResponse(player, mainContent, mainHeader) {
    const playerDetailsView = playerHandlerViews.createPlayerDetailsView(player);
    mainContent.replaceChildren(playerDetailsView);
    mainHeader.replaceChildren(menu.get("sessionSearch"), menu.get("home"),  menu.get("gameSearch"));
}

export default {
    getPlayerDetails
}
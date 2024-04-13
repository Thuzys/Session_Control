import requestUtils from "../utils/requestUtils.js";
import handlerUtils from "./handlerUtils/handlerUtils.js";
import menu from "../navigation/menuLinks.js";
import constants from "../constants/constants.js";
import playerHandlerViews from "../views/handlerViews/playerHandlerViews.js";

/**
 * Get player details
 *
 * @param mainContent main content of the page
 * @param mainHeader main header of the page
 */
function getPlayerDetails(mainContent, mainHeader) {
    const url =
        `${constants.API_BASE_URL}${constants.PLAYER_ROUTE}?pid=${requestUtils.getParams()}&token=${constants.TOKEN}`;
    handlerUtils.executeCommandWithResponse(
        url,
            response => handleGetPlayerDetailsResponse(response, mainContent, mainHeader)
    );
}

/**
 * Handle player details response
 *
 * @param response response from the server
 * @param mainContent main content of the page
 * @param mainHeader the main header of the page
 */
function handleGetPlayerDetailsResponse(response, mainContent, mainHeader) {
    response.json().then(player => {
        const playerDetailsView = playerHandlerViews.createPlayerDetailsView(player);
        mainContent.replaceChildren(playerDetailsView);
        mainHeader.replaceChildren(menu.get("sessionSearch"), menu.get("home"),  menu.get("gameSearch"));
    });
}

export default {
    getPlayerDetails
}
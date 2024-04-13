import menu from "../navigation/menuLinks.js";
import handlerViews from "../views/handlerViews/handlerViews.js";

/**
 * Get home page
 *
 * @param mainContent
 * @param headerContent
 */
function getHome(mainContent, headerContent) {
    const [h1, form] = handlerViews.createHomeView();
    form.onsubmit = (e) => handleHomeSubmit(e);
    headerContent.replaceChildren(menu.get("gameSearch"), menu.get("sessionSearch"));
    mainContent.replaceChildren(h1, form);
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
    window.location.hash = `#players/${pid.value}`;
}

const handlers = {
    getHome,
}

export default handlers

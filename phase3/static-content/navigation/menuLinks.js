import views from "../views/viewsCreators.js";

/**
 * Navigation bar links for the application menu
 * @type {Map<String, any>}
 */
export const menu = new Map()
menu.set(
    "home",
    views.a({href: "#players/home", class: "navigation-bar"}, "Home")
)
menu.set(
    "gameSearch",
    views.a({href: "#gameSearch", class: "navigation-bar"}, "Search Game")
)
menu.set(
    "sessionSearch",
    views.a({href: "#sessionSearch", class: "navigation-bar"}, "Search Session")
)

menu.set(
    "playerSearch",
    views.a({href: "#playerSearch", class: "navigation-bar"}, "Search Player")
)

menu.set(
    "contacts",
    views.a({href: "#contacts", class: "navigation-bar"}, "Contacts")
)

export default menu

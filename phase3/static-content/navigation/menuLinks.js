import views from "../views/viewsCreators.js";

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
    "createGame",
    views.a({href: "#createGame", class: "navigation-bar"}, "Create Game")
)

export default menu

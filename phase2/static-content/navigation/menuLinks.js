import views from "../views/viewsCreators.js";

export const menu = new Map()
menu.set(
    "home",
    views.a({href: "#playerHome", class: "navigation-bar"}, "Home")
)
menu.set(
    "playerDetails",
    views.a({href: "#playerDetails", class: "navigation-bar"}, "Player Details")
)
menu.set(
    "gameSearch",
    views.a({href: "#gameSearch", class: "navigation-bar"}, "Search Game")
)
menu.set(
    "sessionSearch",
    views.a({href: "#sessionSearch", class: "navigation-bar"}, "Search Session")
)

export default menu

import views from "./viewsCreators.js";

export const menu = new Map()
menu.set(
    "home",
    views.a({href: "#playerHome", className: "link-primary"}, "Home")
)
menu.set(
    "playerDetails",
    views.a({href: "#playerDetails", className: "link-primary"}, "Player Details")
)
menu.set(
    "gameSearch",
    views.a({href: "#gameSearch", className: "link-primary"}, "Search Game")
)
menu.set(
    "sessionSearch",
    views.a({href: "#sessionSearch", className: "link-primary"}, "Search Session")
)

export default menu

function makeHeaderLink(text, href, className) {
    const header = document.createElement("a")
    header.href = href
    header.className = className
    header.textContent = text
    return header
}

export const menu = new Map()
menu.set("home", makeHeaderLink("Home", "#playerHome", "link-primary"))
menu.set("playerDetails", makeHeaderLink("Player Details", "#playerDetails", "link-primary"))
menu.set("gameSearch", makeHeaderLink("Search Game", "#gameSearch", "link-primary"))
menu.set("sessionSearch", makeHeaderLink("Search Session", "#sessionSearch", "link-primary"))

function getQuery() {
    return window.location.hash.replace("#", "").split("?")[1]
}

function getPath(){
    return window.location.hash.replace("#", "").split("?")[0]
}

export const utils = {
    menu,
    getQuery,
    getPath
}

export default utils

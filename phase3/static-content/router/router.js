const routes = []
let notFoundRouteHandler = (mainContent, mainHeader) => { throw "Route handler for unknown routes not defined" }

function addRouteHandler(path, handler){
    routes.push({path, handler})
}

function addDefaultNotFoundRouteHandler(notFoundRH) {
    notFoundRouteHandler = notFoundRH
}

function getRouteHandler(path){
    const route = routes.find(r => r.path == path) || findRoute(path)
    // const usePath = makeUsePath(path)
    // if (path !== usePath)
    //     window.location.replace(`#${usePath}`)
    return route ? route.handler : notFoundRouteHandler
}

function makeUsePath(path) {
    if (path === "") return path
    const route = routes.map(route => route.path).find(routePath => {
        const routePathParts = routePath.split("/")
        const pathParts = path.split("/")
        if (routePathParts[0] !== pathParts[0]) return false
        return routePathParts
            .slice(1)
            .every((routePart) => routePart.startsWith(":"))
    }).split("/")[0]
    if (!route.includes(":")) return route
    else return route.split("/")[0]
}

function findRoute(path) {
    return routes.find(route => {
        const routePath = route.path.split("/")
        const pathParams = path.split("/")
        if (routePath.length !== pathParams.length || routePath[0] !== pathParams[0]) return false

        return routePath.slice(1).every((routePart) => routePart.startsWith(":"))
    })
}

const router = {
    addRouteHandler,
    getRouteHandler,
    addDefaultNotFoundRouteHandler
}

export default router

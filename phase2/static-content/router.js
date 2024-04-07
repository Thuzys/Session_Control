const routes = []
let notFoundRouteHandler = () => { throw "Route handler for unknown routes not defined" }

function addRouteHandler(path, handler){
    routes.push({path, handler})
}
function addDefaultNotFoundRouteHandler(notFoundRH) {
    notFoundRouteHandler = notFoundRH
}

function getRouteHandler(path){
    const route = routes.find(r => r.path == path) || findRoute(path)
    console.log(route)
    return route ? route.handler : notFoundRouteHandler
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

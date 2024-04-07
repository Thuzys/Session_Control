const routes = []
let notFoundRouteHandler = (mainContent, mainHeader) => { throw "Route handler for unknown routes not defined" }

function addRouteHandler(path, handler){
    routes.push({path, handler})
}

function addDefaultNotFoundRouteHandler(notFoundRH) {
    notFoundRouteHandler = notFoundRH
}

function findRoute(path) {
    return routes.find(route => {
        const routePath = route.path.split("/")
        const pathParams = path.split("/")
        if (routePath.length !== pathParams.length) return false

        return routePath.slice(1).every((routePart) => routePart.startsWith(":"))
    })
}

function getRouteHandler(path){
    const route = routes.find(r => r.path == path) || findRoute(path)
    return route ? route.handler : notFoundRouteHandler
}

const router = {
    addRouteHandler,
    getRouteHandler,
    addDefaultNotFoundRouteHandler
}

export default router

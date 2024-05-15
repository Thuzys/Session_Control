/**
 * Routes used in the app
 * @type {[{path: string, handler: function}]}
 */
const routes = []

/**
 * The default route handler for unknown routes
 * @param mainContent main content of the page
 * @param mainHeader main header of the page
 */
let notFoundRouteHandler = (mainContent, mainHeader) => { throw "Route handler for unknown routes not defined" }

/**
 * Add the default not found route handler
 * @param notFoundRH the default not found route handler
 */
function addDefaultNotFoundRouteHandler(notFoundRH) {
    notFoundRouteHandler = notFoundRH
}

/**
 * Add a route handler to the routes
 * @param path the path of the route
 * @param handler the handler of the route
 */
function addRouteHandler(path, handler){
    routes.push({path, handler})
}

/**
 * Get the route handler for the given path
 * @param path the path of the route
 * @returns {Function|(function(*, *): never)|*} the route handler
 */
function getRouteHandler(path){
    const route =
        routes.find(r => r.path === path) || findRoute(path)
    return route ? route.handler : notFoundRouteHandler
}

/**
 * Find the route handler for the given path
 * @param path the path of the route
 * @returns {{path: string, handler: Function}} the route handler
 */
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

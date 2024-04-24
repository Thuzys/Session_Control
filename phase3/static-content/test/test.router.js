import router from "../router/router.js";
import handlers from "../handlers/handlers.js";
import playerHandlers from "../handlers/playerHandlers.js";
import sessionHandlers from "../handlers/sessionHandlers.js";
import gameHandlers from "../handlers/gameHandlers.js";

describe('router', function () {

    it('should add and find the routes', function () {
        router.addRouteHandler("players/home", handlers.getHome)
        router.addRouteHandler("gameSearch", gameHandlers.searchGames)
        router.addRouteHandler("games", gameHandlers.getGames)
        router.addRouteHandler("games/:gid", gameHandlers.getGameDetails)
        router.addRouteHandler("players/:pid", playerHandlers.getPlayerDetails)
        router.addRouteHandler("sessionSearch", sessionHandlers.searchSessions)
        router.addRouteHandler("sessions", sessionHandlers.getSessions)
        router.addRouteHandler("sessions/:sid", sessionHandlers.getSessionDetails)

        const handlerName = [
            ["players/home", "getHome"],
            ["gameSearch", "searchGames"],
            ["games", "getGames"],
            ["games/:gid", "getGameDetails"],
            ["players/:pid", "getPlayerDetails"],
            ["sessionSearch", "searchSessions"],
            ["sessions", "getSessions"],
            ["sessions/:sid", "getSessionDetails"]
        ]

        handlerName.forEach( testItem => {
            const handler = router.getRouteHandler(testItem[0])
            handler.name.should.equal(testItem[1])
        })
    })
    it('should find notFoundRouteHandler', function () {
        router.addDefaultNotFoundRouteHandler(handlers.getHome)
        const handler = router.getRouteHandler("unknown")
        handler.name.should.equal("getHome")
    })
});
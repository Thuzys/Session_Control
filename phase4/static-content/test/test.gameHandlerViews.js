import gameHandlerViews from "../views/handlerViews/gameHandlerViews.js";

describe('Test gameHandlerViews', function() {
    it('should create get game view', function () {
        const games = [{gid: "1", name: "game1", dev: "dev1", genres: ["genre1"]}, {gid: "2", name: "game2", dev: "dev2", genres: ["genre2"]}];
        const view = gameHandlerViews.createGetGameView(games);
        const ul = view.querySelector("#ul");

        ul.children[0].textContent.should.equal("game1");
        ul.children[1].textContent.should.equal("game2");
        ul.children[0].children[0].href.substring(ul.children[0].children[0].href.length - 8).should.equal(`#games/${games[0].gid}`);
        ul.children[1].children[0].href.substring(ul.children[1].children[0].href.length - 8).should.equal(`#games/${games[1].gid}`);
    });

    it('should create game details view', function () {
        const game = {gid: "1", name: "game1", dev: "dev1", genres: ["genre1"]};
        const createSession = () => {};
        const view = gameHandlerViews.createGameDetailsView(game, createSession);
        const div = view.querySelector("#GameDetails");

        div.children[0].textContent.should.equal(game.name);
        div.children[1].textContent.should.equal("Developer" + game.dev + "Genres" + game.genres.join(", "));
    });
});
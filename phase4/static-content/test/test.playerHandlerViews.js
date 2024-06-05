import playerHandlerViews from "../views/handlerViews/playerHandlerViews.js";

describe('Test playerHandlerViews', function() {
    it('should create player handler views', function () {
        const player = {
            name: "Player name",
            username: "username",
            email: "email",
            pid: "pid"
        }

        const playerDetailsView = playerHandlerViews.createPlayerDetailsView(player)
        const ul = playerDetailsView.querySelector("#ul")

        ul.children[0].textContent.should.equal("Name" + player.name)
        ul.children[1].textContent.should.equal("Username" + player.username)
        ul.children[2].textContent.should.equal("Email" + player.email)
    });
});

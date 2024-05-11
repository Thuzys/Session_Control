import playerHandlerViews from "../views/handlerViews/playerHandlerViews.js";

describe('Test playerHandlerViews', function() {
    it('should create a playerDetailsView', function() {
        const player = {
            name: "Player name",
            userName: "username",
            email: "email",
            pid: "pid"
        }

        const playerDetailsView = playerHandlerViews.createPlayerDetailsView(player)

        playerDetailsView.tagName.should.equal("DIV")
        const children = playerDetailsView.children
        children.length.should.equal(5)
        children[0].tagName.should.equal("H2")
        children[0].textContent.should.equal("Player Details:")
        children[1].tagName.should.equal("H3")
        children[1].textContent.should.equal("Player name")
        children[2].tagName.should.equal("UL")
        children[2].children[0].textContent.should.equal("UserName: username")
        children[2].children[1].textContent.should.equal("Email: email")
        children[3].tagName.should.equal("BUTTON")
        children[3].textContent.should.equal("Sessions")
        children[4].tagName.should.equal("BUTTON")
        children[4].textContent.should.equal("Back")
    })
})
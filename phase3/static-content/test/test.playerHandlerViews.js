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
        children.length.should.equal(7)
        children[0].tagName.should.equal("H2")
        children[0].textContent.should.equal("Player Details")
        children[1].tagName.should.equal("HR")
        children[2].tagName.should.equal("UL")
        children[2].children.length.should.equal(6)
        children[2].children[0].tagName.should.equal("LI")
        children[2].children[0].textContent.should.equal("Name")
        children[2].children[1].tagName.should.equal("LI")
        children[2].children[1].textContent.should.equal("Player name")
        children[2].children[2].tagName.should.equal("LI")
        children[2].children[2].textContent.should.equal("Username")
        children[2].children[3].tagName.should.equal("LI")
        children[2].children[3].textContent.should.equal("username")
        children[2].children[4].tagName.should.equal("LI")
        children[2].children[4].textContent.should.equal("Email")
        children[2].children[5].tagName.should.equal("LI")
        children[2].children[5].textContent.should.equal("email")
        children[3].tagName.should.equal("P")
        children[4].tagName.should.equal("BUTTON")
        children[4].textContent.should.equal("View Player's Sessions")
        children[5].tagName.should.equal("P")
        children[6].tagName.should.equal("BUTTON")
        children[6].textContent.should.equal("Back")
    })
})
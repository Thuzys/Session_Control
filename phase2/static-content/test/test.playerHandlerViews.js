import playerHandlerViews from "../views/handlerViews/playerHandlerViews.js";

describe('Test playerHandlerViews', function() {
    it('should create a playerDetailsView', function() {
        const player = {
            name: "name",
            email: {email: "email"},
            pid: "pid"
        }
        const query = "query"

        const playerDetailsView = playerHandlerViews.createPlayerDetailsView(player)

        playerDetailsView.tagName.should.equal("DIV")
        const children = playerDetailsView.children
        children.length.should.equal(4)
        children[0].tagName.should.equal("H2")
        children[0].textContent.should.equal("Player Details")
        children[1].tagName.should.equal("UL")
        children[1].children[0].textContent.should.equal("Name: name")
        children[1].children[1].textContent.should.equal("Email: email")
        children[1].children[2].textContent.should.equal("Pid: pid")
        children[2].tagName.should.equal("BUTTON")
        children[2].textContent.should.equal("Back")
        children[3].tagName.should.equal("BUTTON")
        children[3].textContent.should.equal("Sessions")
    })
})
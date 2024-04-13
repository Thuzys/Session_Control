import handlers from "../handlers/handlers.js";


describe('Test handlers', function() {

    it('should find getHome children', function() {

        const mainContent = document.createElement("div")
        const headerContent = document.createElement("div")

        handlers.getHome(mainContent, headerContent)

        const mainContendChildren = mainContent.children

        mainContendChildren.length.should.equal(2)
        mainContendChildren[0].tagName.should.equal("H1")
        mainContendChildren[1].tagName.should.equal("FORM")

        const mainHeaderChildren = headerContent.children

        mainHeaderChildren.length.should.equal(2)
        mainHeaderChildren[0].tagName.should.equal("A")
        mainHeaderChildren[1].tagName.should.equal("A")

    })
})
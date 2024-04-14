import handlers from "../handlers/handlers.js";


describe('Test handlers', function() {

    it('should alter the mainContent and headerContent correctly', function() {

        const mainContent = document.createElement("div")
        const headerContent = document.createElement("div")

        handlers.getHome(mainContent, headerContent)

        const mainContentChildren = mainContent.children

        mainContentChildren.length.should.equal(2)
        mainContentChildren[0].tagName.should.equal("H1")
        mainContentChildren[1].tagName.should.equal("FORM")

        mainContentChildren[1].onsubmit.should.not.equal(null)

        const mainHeaderChildren = headerContent.children

        mainHeaderChildren.length.should.equal(2)
        mainHeaderChildren[0].tagName.should.equal("A")
        mainHeaderChildren[1].tagName.should.equal("A")

    })
})
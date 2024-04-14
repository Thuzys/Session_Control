import gameHandlers from "../handlers/gameHandlers.js";

describe('Test gameHandlers', function() {
    it('searchGames should alter the mainContent and headerContent correctly', function () {
        const mainContent = document.createElement("div")
        const headerContent = document.createElement("div")

        gameHandlers.searchGames(mainContent, headerContent)

        const mainContentChildren = mainContent.children
        mainContentChildren.length.should.equal(2)
        mainContentChildren[0].tagName.should.equal("H1")
        mainContentChildren[1].tagName.should.equal("FORM")
        mainContentChildren[1].onsubmit.should.not.equal(null)

        const headerContentChildren = headerContent.children
        headerContentChildren.length.should.equal(1)
        headerContentChildren[0].tagName.should.equal("A")
    });

    it('getGames should alter the mainContent and headerContent correctly', function () {
        const mainContent = document.createElement("div")
        const headerContent = document.createElement("div")

        gameHandlers.getGames(mainContent, headerContent)

        const mainContentChildren = mainContent.children
        mainContentChildren.length.should.equal(3)
        mainContentChildren[0].tagName.should.equal("H1")
        mainContentChildren[1].tagName.should.equal("UL")
        mainContentChildren[2].tagName.should.equal("DIV")

        const headerContentChildren = headerContent.children
        headerContentChildren.length.should.equal(2)
        headerContentChildren[0].tagName.should.equal("A")
        headerContentChildren[1].tagName.should.equal("A")
    });

    it('getGameDetails should alter the mainContent and headerContent correctly', function () {
        const mainContent = document.createElement("div")
        const headerContent = document.createElement("div")

        gameHandlers.getGameDetails(mainContent, headerContent)

        const mainContentChildren = mainContent.children
        mainContentChildren.length.should.equal(2)
        mainContentChildren[0].tagName.should.equal("H1")
        mainContentChildren[1].tagName.should.equal("DIV")

        const headerContentChildren = headerContent.children
        headerContentChildren.length.should.equal(3)
        headerContentChildren[0].tagName.should.equal("A")
        headerContentChildren[1].tagName.should.equal("A")
        headerContentChildren[2].tagName.should.equal("A")
    });
});
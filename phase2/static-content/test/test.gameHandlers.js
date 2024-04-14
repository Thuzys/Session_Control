import gameHandlers from "../handlers/gameHandlers.js";

describe('Test gameHandlers', function() {
    function simulateGameSearchFormSubmission(mainContentChildren, dev, genres) {
        const e = new Event('submit');
        window.document.getElementById = function(id) {
            switch (id) {
                case 'InputDev': return { value: dev };
                case 'SelectedGenresView': return { children: genres };
                default: return null;
            }
        };
        mainContentChildren[1].dispatchEvent(e);
    }

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

    it('searchGames should change the hash correctly', function () {
        const mainContent = document.createElement("div")
        const headerContent = document.createElement("div")

        gameHandlers.searchGames(mainContent, headerContent)
        const genres = [document.createElement("div"), document.createElement("div")]
        simulateGameSearchFormSubmission(mainContent.children, 'dev', genres)

        window.location.hash.should.equal('#games?dev=dev&genres=%2C&offset=0');
    });
});
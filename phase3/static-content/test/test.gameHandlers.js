import gameHandlers from "../handlers/gameHandlers.js";
import views from "../views/viewsCreators.js";

describe('Test gameHandlers', function() {
    function simulateGameSearchFormSubmission(mainContentChildren, dev, name, genres) {
        const e = new Event('submit');
        window.document.getElementById = function(id) {
            switch (id) {
                case 'InputName': return { value: name };
                case 'InputDev': return { value: dev };
                case 'ul': return { children: genres };
                default: return null;
            }
        };
        mainContentChildren[0].dispatchEvent(e);
    }

    it('searchGames should alter the mainContent correctly', function () {
        const mainContent = document.createElement("div")

        gameHandlers.searchGames(mainContent)

        const mainContentChildren = mainContent.children
        mainContentChildren.length.should.equal(1)
        mainContentChildren[0].tagName.should.equal("DIV")
        const divChildren = mainContentChildren[0].children
        divChildren.length.should.equal(2)
    });

    it('searchGames should change the hash correctly', function () {
        const mainContent = document.createElement("div")

        gameHandlers.searchGames(mainContent)
        simulateGameSearchFormSubmission(
            mainContent.children,
            'dev',
            'name',
            views.ul([views.li('genre1'), views.li('genre2')]).children
        )

        window.location.hash.should.equal('#games?dev=dev&name=name&genres=undefined,undefined&offset=0');
    });

    it('createGame should change the hash correctly', function () {
        const inputName = document.createElement("input")
        const inputDev = document.createElement("input")
        const selectedGenresView = document.createElement("div")
        const mainContent = document.createElement("div")

        inputName.value = 'name'
        inputDev.value = 'dev'
        selectedGenresView.children = [document.createElement("div"), document.createElement("div")]

        gameHandlers.createGame(inputName, inputDev, selectedGenresView, mainContent)

        window.location.hash.should.equal('#games/undefined');
    })
});
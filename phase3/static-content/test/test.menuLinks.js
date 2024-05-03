import menu from "../navigation/menuLinks.js";

describe('Test menuLinks', function() {
    it('should create a menu', function() {
        menu.size.should.equal(4)
        const home = menu.get("home")
        home.tagName.should.equal("A")
        home.href.split('#')[1].should.equal("players/home")
        home.textContent.should.equal("Home")
        const gameSearch = menu.get("gameSearch")
        gameSearch.tagName.should.equal("A")
        gameSearch.href.split('#')[1].should.equal("gameSearch")
        gameSearch.textContent.should.equal("Search Game")
        const sessionSearch = menu.get("sessionSearch")
        sessionSearch.tagName.should.equal("A")
        sessionSearch.href.split('#')[1].should.equal("sessionSearch")
        sessionSearch.textContent.should.equal("Search Session")
    })
});

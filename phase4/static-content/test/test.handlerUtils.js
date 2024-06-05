import handlerUtils from "../handlers/handlerUtils/handlerUtils.js";

describe('Test handlerUtils', function() {
    it('childrenToString should return a string of the text nodes of the children', function () {
        const div = document.createElement("div")
        div.innerHTML = "<p>Test</p><p>Test2</p>"

        const result = handlerUtils.childrenToString(div.children)
        result.should.equal("Test,Test2")
    });

    it('makeQueryString should return a query string from a query object', function () {
        const query = new Map([["test", "test2"]])
        const result = handlerUtils.makeQueryString(query)
        result.toString().should.equal("test=test2")
    });

    it('changeHash should change the window location hash', function () {
        const hash = "#test"
        handlerUtils.changeHash(hash)
        window.location.hash.should.equal(hash)
    });

    it('createURL should return a URL from a route and query', function () {
        const route = "test"
        const query = new Map([["test", "test2"]])
        const result = handlerUtils.createURL(route, query)
        result.should.equal(`${window.location.protocol}//${window.location.host}/${route}?test=test2`)
    });

    it('createRoute should return a route from a URL', function () {
        const basePath = "test"
        const pathParam1 = "test1"
        const pathParam2 = "test2"
        const result = handlerUtils.createRoute(basePath, pathParam1, pathParam2)
        result.should.equal(`${basePath}/${pathParam1}/${pathParam2}`)
    });
});
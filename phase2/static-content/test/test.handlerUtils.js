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

    it('executeCommandWithResponse should call the responseHandler if the response is OK', function () {
        // TODO()
    });
});
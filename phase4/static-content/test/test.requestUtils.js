import requestUtils from "../utils/requestUtils.js";

describe('Test requestUtils', function() {
    it('should return the correct path', function() {
        window.location.hash = "#/test/firstElement/secondElement"
        const path = requestUtils.getPath("/test")
        path.length.should.be.above(2)
    })
    it('should return the correct query', function() {
        window.location.hash = "#/test/firstElement/secondElement?offset=1&name=John"
        const query = requestUtils.getQuery()
        query.get("offset").should.equal(1)
        query.get("name").should.equal("John")
    })
    it('should return the request path', function () {
        window.location.hash = "#/test/firstElement/secondElement?offset=1&name=John"
        const path = requestUtils.getPath()
        path.should.contain("test/firstElement/secondElement")
    })
})

describe('getParams', () => {
    it('should return the correct parameter from the URL hash', () => {
        window.location.hash = '#/param1/param2'
        const result = requestUtils.getParams();
        result.should.equal('param1');
    });

    it('should return an empty string if there is no parameter', () => {
        window.location.hash = '#/';
        const result = requestUtils.getParams();
        result.should.equal('');
    });
});

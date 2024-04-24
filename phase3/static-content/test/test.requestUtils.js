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
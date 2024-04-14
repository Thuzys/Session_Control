import sessionHandlers from "../handlers/sessionHandlers.js";
import testUtils from "./testUtils.js";

function simulateSessionFormSubmission(mainContentChildren, gameId, playerId, date, state) {
    const e = new Event('submit');
    window.document.getElementById = function(id) {
        switch(id) {
            case 'gameId': return { value: gameId };
            case 'playerId': return { value: playerId };
            case 'date': return { value: date };
            default: return null;
        }
    };
    window.document.querySelector = function(query) {
        if (query === 'input[name="state"][value="open"]') {
            return { checked: state === 'open' };
        } else if (query === 'input[name="state"][value="close"]') {
            return { checked: state === 'close' };
        }
    };
    mainContentChildren[1].dispatchEvent(e);
}

describe('Test sessionHandlers', function() {
    it('should alter the mainContent and headerContent correctly', function() {
        const mainContentChildren = testUtils.setupTest(sessionHandlers.searchSessions);

        mainContentChildren.length.should.equal(2);
        mainContentChildren[0].tagName.should.equal("H1");
        mainContentChildren[0].textContent.should.equal("Search Sessions: ");
        mainContentChildren[1].tagName.should.equal("FORM");
        mainContentChildren[1].submit.should.not.equal(null);
    });

    it('handleSearchSessionsSubmit sets the correct window location hash', function() {
        const mainContentChildren = testUtils.setupTest(sessionHandlers.searchSessions);
        simulateSessionFormSubmission(mainContentChildren, '1', '2', '2024-04-13T22_50', 'open');
        window.location.hash.should.equal('#sessions?gid=1&pid=2&date=2024-04-13T22_50&state=open&offset=0');
    });

    it('should throw an error when mainContent is null', function() {
        testUtils.testErrorWhenNull(sessionHandlers.searchSessions, null, document.createElement("div"));
    });

    it('should throw an error when headerContent is null', function() {
        testUtils.testErrorWhenNull(sessionHandlers.searchSessions, document.createElement("div"), null);
    });

    it('should handle form submission with empty gameId', function() {
        const mainContentChildren = testUtils.setupTest(sessionHandlers.searchSessions);
        simulateSessionFormSubmission(mainContentChildren, '', '2', '2024-04-13T22_50', 'open');
        window.location.hash.should.equal('#sessions?pid=2&date=2024-04-13T22_50&state=open&offset=0');
    });

    it('should handle form submission with state set to close', function() {
        const mainContentChildren = testUtils.setupTest(sessionHandlers.searchSessions);
        simulateSessionFormSubmission(mainContentChildren, '1', '2', '2024-04-13T22_50', 'close');
        window.location.hash.should.equal('#sessions?gid=1&pid=2&date=2024-04-13T22_50&state=close&offset=0');
    });

    it('should handle form submission with empty playerId', function() {
        const mainContentChildren = testUtils.setupTest(sessionHandlers.searchSessions);
        simulateSessionFormSubmission(mainContentChildren, '1', '', '2024-04-13T22_50', 'open');
        window.location.hash.should.equal('#sessions?gid=1&date=2024-04-13T22_50&state=open&offset=0');
    });

    it('should handle form submission with empty date', function() {
        const mainContentChildren = testUtils.setupTest(sessionHandlers.searchSessions);
        simulateSessionFormSubmission(mainContentChildren, '1', '2', '', 'open');
        window.location.hash.should.equal('#sessions?gid=1&pid=2&state=open&offset=0');
    });

    it('should handle form submission with empty state', function() {
        const mainContentChildren = testUtils.setupTest(sessionHandlers.searchSessions);
        simulateSessionFormSubmission(mainContentChildren, '1', '2', '2024-04-13T22_50', '');
        window.location.hash.should.equal('#sessions?gid=1&pid=2&date=2024-04-13T22_50&offset=0');
    });
});
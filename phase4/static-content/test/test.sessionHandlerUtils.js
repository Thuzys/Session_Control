import {isPlayerOwner} from "../handlers/handlerUtils/sessionHandlersUtils.js";

describe('Test sessionHandlersUtils', function() {
    it('isPlayerOwner should return true if the player is the owner', function () {
        const session = {owner: {pid: 1}};
        sessionStorage.setItem('pid', 1);
        isPlayerOwner(session).should.be.true;
    });
});
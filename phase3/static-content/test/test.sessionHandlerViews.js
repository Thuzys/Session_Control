import sessionHandlerViews from "../views/handlerViews/sessionHandlerViews.js";

describe('Test sessionHandlerViews', function() {
    it('should create a sessionFormContentView', function() {
        const sessionFormContentView = sessionHandlerViews.createSessionFormContentView()
        sessionFormContentView.length.should.equal(18)
        sessionFormContentView[0].tagName.should.equal("LABEL")
        sessionFormContentView[1].tagName.should.equal("INPUT")
        sessionFormContentView[2].tagName.should.equal("P")
        sessionFormContentView[3].tagName.should.equal("LABEL")
        sessionFormContentView[4].tagName.should.equal("INPUT")
        sessionFormContentView[5].tagName.should.equal("P")
        sessionFormContentView[6].tagName.should.equal("LABEL")
        sessionFormContentView[7].tagName.should.equal("INPUT")
        sessionFormContentView[8].tagName.should.equal("P")
        sessionFormContentView[9].tagName.should.equal("LABEL")
        sessionFormContentView[10].tagName.should.equal("INPUT")
        sessionFormContentView[11].tagName.should.equal("LABEL")
        sessionFormContentView[12].tagName.should.equal("INPUT")
        sessionFormContentView[13].tagName.should.equal("LABEL")
        sessionFormContentView[14].tagName.should.equal("INPUT")
        sessionFormContentView[15].tagName.should.equal("LABEL")
        sessionFormContentView[16].tagName.should.equal("P")
        sessionFormContentView[17].tagName.should.equal("BUTTON")
    })

    it('should create a sessionView', function() {
        const session = [{
            sid: "1",
            owner: {
                username: "username"
            },
            gameInfo: {
                name: "name",
                gid: "1",
            },
            date: "date",
        }]
        window.location.hash = ""
        const sessionView = sessionHandlerViews.createGetSessionsView(session)
        sessionView.length.should.equal(2)
        sessionView[0].tagName.should.equal("DIV")
        sessionView[1].tagName.should.equal("DIV")
        sessionView[1].children[0].tagName.should.equal("BUTTON")
        sessionView[1].children[1].tagName.should.equal("BUTTON")
    })

    it('should create a sessionDetailsView', function() {
        const session = {
            sid: "1",
            owner: {
                username: "username"
            },
            gameInfo: {
                name: "name",
                gid: "1",
            },
            date: "date",
        }
        const player = [1, 2, 3]
        const sessionDetailsView = sessionHandlerViews.createSessionDetailsViews(session, player)
        sessionDetailsView.tagName.should.equal("DIV")
        const children = sessionDetailsView.children
        children.length.should.equal(2)
        children[0].tagName.should.equal("H3")
        children[1].tagName.should.equal("UL")
    })

    it('should create a PlayerListView', function() {
        const session = {
            players: [1, 2, 3]
        }
        const playerListView = sessionHandlerViews.createPlayerListView(session)
        playerListView.tagName.should.equal("DIV")
        playerListView.children.length.should.equal(2)
        const ul = playerListView.children[0]
        ul.children[0].tagName.should.equal("LI")
        ul.children[1].tagName.should.equal("LI")
        ul.children[2].tagName.should.equal("LI")
        playerListView.children[1].tagName.should.equal("DIV")
    })

    it('should create correct href for each session', function() {
        const sessions = [
            { sid: '1' },
            { sid: '2' },
            { sid: '3' }
        ];
        const [div, nextPrev] = sessionHandlerViews.createGetSessionsView(sessions);
        const anchors = div.querySelectorAll('a');
        for (let i = 0; i < sessions.length; i++) {
            const href = anchors[i].getAttribute('href');
            href.should.equal(`#sessions/${sessions[i].sid}`);
        }
    });
})
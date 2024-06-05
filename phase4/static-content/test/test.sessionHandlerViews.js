import sessionHandlerViews from "../views/handlerViews/sessionHandlerViews.js";

describe('Test sessionHandlerViews', function() {
    it('should create a sessionFormContentView', function() {
        const sessionFormContentView = sessionHandlerViews.createSessionFormContentView()
        sessionFormContentView.length.should.equal(11)
        sessionFormContentView[0].tagName.should.equal("H2")
        sessionFormContentView[1].tagName.should.equal("HR")
        sessionFormContentView[2].tagName.should.equal("INPUT")
        sessionFormContentView[3].tagName.should.equal("P")
        sessionFormContentView[4].tagName.should.equal("INPUT")
        sessionFormContentView[5].tagName.should.equal("P")
        sessionFormContentView[6].tagName.should.equal("INPUT")
        sessionFormContentView[7].tagName.should.equal("P")
        sessionFormContentView[8].tagName.should.equal("DIV")
        const divChildren = sessionFormContentView[8].children
        divChildren[0].tagName.should.equal("H5")
        divChildren[1].tagName.should.equal("HR")
        divChildren[2].tagName.should.equal("LABEL")
        divChildren[3].tagName.should.equal("P")
        divChildren[4].tagName.should.equal("LABEL")
        divChildren[5].tagName.should.equal("P")
        sessionFormContentView[9].tagName.should.equal("P")
        sessionFormContentView[10].tagName.should.equal("BUTTON")
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

    it('should create a sessionDetailsView - not an owner or in session', function() {
        const session = {
            sid: "1",
            capacity: 3,
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
        const isOwner = false
        const isInSession = false
        const sessionDetailsDiv = sessionHandlerViews.createSessionDetailsView(session, player, isOwner, isInSession)
        const divChildren = sessionDetailsDiv.children
        divChildren.length.should.equal(4)
        divChildren[0].tagName.should.equal("H2")
        divChildren[1].tagName.should.equal("HR")
        divChildren[2].tagName.should.equal("DIV")
        divChildren[2].children[0].tagName.should.equal("UL")
        divChildren[3].tagName.should.equal("BUTTON")
    })

    it('should create a sessionDetailsView - owner', function() {
        const session = {
            sid: "1",
            capacity: 3,
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
        const isOwner = true
        const isInSession = false
        const sessionDetailsDiv = sessionHandlerViews.createSessionDetailsView(session, player, isOwner, isInSession)
        const divChildren = sessionDetailsDiv.children
        divChildren.length.should.equal(6)
        divChildren[0].tagName.should.equal("H2")
        divChildren[1].tagName.should.equal("HR")
        divChildren[2].tagName.should.equal("DIV")
        divChildren[2].children[0].tagName.should.equal("UL")
        divChildren[3].tagName.should.equal("BUTTON")
        divChildren[4].tagName.should.equal("P")
        divChildren[5].tagName.should.equal("BUTTON")
    })

    it('should create a sessionDetailsView with the correct sid, capacity, owner, gameName and date', function() {
        const owner = "username"
        const gameName = "game"
        const date = "date"
        const capacity = 3
        const session = {
            sid: "1",
            capacity: capacity,
            owner: {
                username: owner
            },
            gameInfo: {
                name: gameName,
                gid: "1",
            },
            date: date,
        }
        const player = [1, 2, 3]
        const isOwner = false
        const isInSession = true
        const sessionDetailsDiv = sessionHandlerViews.createSessionDetailsView(session, player, isOwner, isInSession)
        const sessionNameElement = sessionDetailsDiv.querySelector("#headerTest");
        const gameNameElement = sessionDetailsDiv.querySelector("#gameTest");
        const dateElement = sessionDetailsDiv.querySelector("#dateTest");
        const ownerElement = sessionDetailsDiv.querySelector("#ownerTest");
        const capacityElement = sessionDetailsDiv.querySelector("#capacityTest");
        sessionNameElement.textContent.should.equal(owner + "´s Session")
        gameNameElement.textContent.should.equal("Game" + gameName)
        dateElement.textContent.should.equal("Date" + date)
        ownerElement.textContent.should.equal("Owner" + owner)
        capacityElement.textContent.should.equal("Capacity" + capacity)
    })

    it('should create a PlayerListView with correct player names', function() {
        // Crie uma sessão com uma lista de três jogadores
        const session = {
            players: [
                { pid: 1, username: 'Player 1' },
                { pid: 2, username: 'Player 2' },
                { pid: 3, username: 'Player 3' }
            ]
        }


        sessionStorage.setItem('pid','1')

        // Chame a função createPlayerListView com a sessão
        const playerListView = sessionHandlerViews.createPlayerListView(session)

        // Verifique se a visualização retornada contém os nomes dos jogadores no elemento correto
        const ul = playerListView.children[1] // O elemento UL é o segundo filho do div retornado
        ul.children[0].children[0].textContent.should.equal('Player 1') // O nome do primeiro jogador deve estar no primeiro filho do UL
        ul.children[1].children[0].textContent.should.equal('Player 2') // O nome do segundo jogador deve estar no segundo filho do UL
        ul.children[2].children[0].textContent.should.equal('Player 3') // O nome do terceiro jogador deve estar no terceiro filho do UL
    })

    it('should create a updateSessionView', function() {
        const session = {
            sid: "1",
            capacity: 3,
            owner: {
                username: "username"
            },
            gameInfo: {
                name: "name",
                gid: "1",
            },
            date: "date",
        }
        const updateSessionView = sessionHandlerViews.createUpdateSessionView(session)
        updateSessionView.length.should.equal(2)
        updateSessionView[0].tagName.should.equal("H2")
        updateSessionView[1].tagName.should.equal("FORM")
    })

    it('should create a createSessionView', function() {
        const createSessionView = sessionHandlerViews.createCreateSessionView("Elden Ring")
        createSessionView.length.should.equal(2)
        createSessionView[0].tagName.should.equal("H2")
        createSessionView[1].tagName.should.equal("FORM")
    })
})
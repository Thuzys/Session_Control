import sessionHandlerViews from "../views/handlerViews/sessionHandlerViews.js";

const owner = "username"
const gameName = "game"
const date = "date"
const capacity = 3

describe('Test sessionHandlerViews', function() {
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
            date: "2025-10-20",
        }

        sessionStorage.setItem('pid','4')

        const player = [1, 2, 3]
        const isOwner = false
        const isInSession = false
        const sessionDetailsDiv = sessionHandlerViews.createSessionDetailsView(session, player, isOwner, isInSession)
        const joinButton = sessionDetailsDiv.querySelector("#joinButtonTest");
        joinButton.should.exist
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
        const deleteButton = sessionDetailsDiv.querySelector("#deleteButtonTest");
        const updateButton = sessionDetailsDiv.querySelector("#updateButtonTest");
        deleteButton.should.exist
        updateButton.should.exist
    })

    it('should create a sessionDetailsView -  in session but not an owner', function() {
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
            date: "2025-10-20",
        }

        sessionStorage.setItem('pid','2')

        const player = [1, 2, 3]
        const isOwner = false
        const isInSession = true
        const sessionDetailsDiv = sessionHandlerViews.createSessionDetailsView(session, player, isOwner, isInSession)
        const leaveButton = sessionDetailsDiv.querySelector("#leaveButtonTest");
        leaveButton.should.exist
    })

    it('should create a sessionDetailsView with the correct sid, capacity, owner, gameName and date', function() {
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

    it('should create a updateSessionView with the correct gameName', function() {
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
        const gameNameElement = updateSessionView.querySelector("#gameNameTest");
        gameNameElement.textContent.should.equal("name")
    })

    it('should create a createSessionView with the correct gameName', function() {
        const createSessionView = sessionHandlerViews.createCreateSessionView("Elden Ring")
        const gameNameElement = createSessionView.querySelector("#gameNameTest");
        gameNameElement.textContent.should.equal("Elden Ring")
    })
})
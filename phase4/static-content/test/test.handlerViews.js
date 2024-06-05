import handlerViews from "../views/handlerViews/handlerViews.js";
import views from "../views/viewsCreators.js";

describe('Test handlerViews', function() {
    it('should create a header', function() {
        const header = handlerViews.createHeader("Home page");
        header.tagName.should.equal("H2");
        header.textContent.should.equal("Home page");
    })
    it('should create a href', function() {
        const href = handlerViews.hrefConstructor("base", "id", "textBase");

        href.length.should.equal(2);
        href[0].tagName.should.equal("A");
        href[0].href.should.contain("base/id");
        href[0].textContent.should.equal("textBase");
        href[1].tagName.should.equal("P");
    })
    it('should create a backButton', function() {
        const backButton = handlerViews.createBackButtonView();

        backButton.tagName.should.equal("BUTTON");
        backButton.type.should.equal("button");
        backButton.textContent.should.equal("Back");
    })
    it('should create a href Button', function() {
        const sessionsButton = handlerViews.hrefButtonView("text", "query");

        sessionsButton.tagName.should.equal("BUTTON");
        sessionsButton.type.should.equal("button");
        sessionsButton.textContent.should.equal("text");

    })
    it('should create a pagination', function() {
        const query = new Map();
        query.set("offset", 0)
        const pagination = handlerViews.createPagination(query, "hash", true);

        pagination.tagName.should.equal("DIV");
        const buttons = pagination.children;
        buttons.length.should.equal(2);
        buttons[0].tagName.should.equal("BUTTON");
        buttons[0].id.should.equal("prev");
        buttons[0].type.should.equal("button");
        buttons[0].textContent.should.equal("<");
        buttons[1].tagName.should.equal("BUTTON");
        buttons[1].id.should.equal("next");
        buttons[1].type.should.equal("button");
        buttons[1].textContent.should.equal(">");
    })

    it('should create custom alert', function () {
        handlerViews.showAlert("message");

        const alertModal = document.getElementById("alertModal");
        alertModal.style.display.should.equal("flex");
        const alertMessage = document.getElementById("alertMessage");
        alertMessage.textContent.should.equal("message");
        const alertButtons = document.getElementsByClassName("alert-buttons");
        alertButtons.length.should.equal(1);
        const alertButton = document.getElementsByTagName("button");
        alertButton.length.should.equal(1);
        alertButton[0].textContent.should.equal("OK");
        alertButton[0].click();
        alertModal.style.display.should.equal("none");
    });

    it('should verify if ul has item', function () {
        const ul = views.ul()
        const item = views.li("item")
        ul.appendChild(item)
        const result = handlerViews.ulHasItem("item", ul.children);

        result.should.equal(true);
    });

    it('should create a radio button', function() {
        const radio = handlerViews.createRadioButton("open", "openState");

        radio.tagName.should.equal("LABEL");
        radio.textContent.should.equal("openState");
        const input = radio.children[0];
        input.tagName.should.equal("INPUT");
        input.type.should.equal("radio");
        input.name.should.equal("state");
    })
})
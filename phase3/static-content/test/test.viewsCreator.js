import views from "../views/viewsCreators.js";

describe('viewsCreator test', function() {
    it ('should contains all the views', function() {
        views.should.contain
            .keys(
                "ul",
                "li",
                "form",
                "input",
                "label",
                "radioButton",
                "radioButtonLabel",
                "p",
                "div"
            );
    })
    it ('should create a view for test', function() {

        // Test the views with examples from the handlers
        const h1 = views.h1({}, "Home page");
        h1.tagName.should.equal("H1");
        h1.textContent.should.equal("Home page");

        const form = views.form({action: "#playerDetails", method: "get"},
            views.input({type: "text", id: "pid", maxLength: 10}),
            views.button({type: "submit", class: "submit-button"}, "Player Details")
        );
        form.tagName.should.equal("FORM");
        const action = form.action.split("#")[1]
        action.should.equal("playerDetails");
        form.method.should.equal("get");
        form.children.length.should.equal(2);
        form.children[0].tagName.should.equal("INPUT");
        form.children[0].type.should.equal("text");
        form.children[0].id.should.equal("pid");
        form.children[0].maxLength.should.equal(10);
        form.children[1].tagName.should.equal("BUTTON");
        form.children[1].type.should.equal("submit");
    })
})
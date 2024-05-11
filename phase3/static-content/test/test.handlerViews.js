import handlerViews from "../views/handlerViews/handlerViews.js";

describe('Test handlerViews', function() {
    it('should create a header', function() {
        const header = handlerViews.createHeader("Home page");
        header.tagName.should.equal("H1");
        header.textContent.should.equal("Home page");
    })
    it('should create a LabelledInput', function() {
      const labelledInput = handlerViews.createLabeledInput("label", "text", "id");

      labelledInput.length.should.equal(3);
      labelledInput[0].tagName.should.equal("LABEL");
      labelledInput[0].textContent.should.equal("label");
      labelledInput[1].tagName.should.equal("INPUT");
      labelledInput[1].type.should.equal("text");
      labelledInput[1].id.should.equal("id");
      labelledInput[2].tagName.should.equal("P");
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
})
import views from "./viewsCreators.js";

function changeHash(hash) {
    window.location.hash = hash;
}

const updateButtonState = (searchButton, inputDev, inputGenres) => {
    searchButton.disabled = !inputDev.value.trim() && !inputGenres.value.trim()
}

function updateGameSearchButton(searchButton, inputDev, inputGenres) {
    const update = () => updateButtonState(searchButton, inputDev, inputGenres)
    inputDev.addEventListener("input", update)
    inputGenres.addEventListener("input", update)
}

function createEventHandlerButton(attributes, textContent, handler) {
    const button = views.button(attributes, textContent)
    button.addEventListener("click", handler)
    return button
}

function isResponseOK(response) {
    return response.status >= 200 && response.status < 399
}

function createHeader(text) {
    return views.h1({}, text);
}

function createStateInputs() {
    return [
        views.label({qualifiedName: "for", value: "textbox"}, "Enter State: "),
        views.radioButton({name: "state", value: "open", checked: true}),
        views.radioButtonLabel("open", "Open"),
        views.radioButton({name: "state", value: "close"}),
        views.radioButtonLabel("close", "Close"),
        views.radioButton({name: "state", value: "both"}),
        views.radioButtonLabel("both", "Both")
    ];
}

function createLabeledInput(labelText, inputType, inputId) {
    return [
        views.label({ qualifiedName: "for", value: inputId }, labelText),
        views.input({ type: inputType, id: inputId }),
        views.p()
    ];
}

function createFormContent() {
    const gidLabelInput = createLabeledInput("Enter Game Id: ", "text", "gameId");
    const pidLabelInput = createLabeledInput("Enter Player Id: ", "text", "playerId");
    const dateLabelInput = createLabeledInput("Enter Date: ", "datetime-local", "date");
    const stateLabelInputs = createStateInputs();

    return [
        ...gidLabelInput,
        ...pidLabelInput,
        ...dateLabelInput,
        ...stateLabelInputs,
        views.p(),
        views.button({ type: "submit" }, "Search")
    ];
}

function createLabeledInput2(labelText, inputType, inputId, attributes = {}) {
    return [
        views.label({ qualifiedName: "for", value: inputId }, labelText),
        views.input({ type: inputType, id: inputId, ...attributes }),
        views.p()
    ];
}

function createFormContent2(inputs, buttonAttributes = { type: "submit" }) {
    const formContent = inputs.map(input => createLabeledInput(input.labelText, input.inputType, input.inputId, input.attributes));
    formContent.push(views.p());
    formContent.push(views.button(buttonAttributes, "Search"));
    return formContent;
}

function sessionHrefConstructor(session) {
    return [
        views.a({href: `#sessionDetails/${session.sid}`}, "Session" + session.sid),
        views.p()
        ]
}

function executeCommandWithResponse(url, responseHandler) {
    fetch(url)
        .then(response => {
            if(isResponseOK(response)) {
                responseHandler(response);
            } else {
                response.text().then(text => alert("Error fetching data: " + text));
            }
        })
}

function makeQueryString(query) {
    const queryString = new URLSearchParams();
    for (const [key, value] of query) {
        queryString.set(key, value);
    }
    return queryString;
}

const handlerUtils = {
    createLabeledInput2,
    createFormContent2,
    sessionHrefConstructor,
    createEventHandlerButton,
    changeHash,
    updateGameSearchButton,
    createStateInputs,
    createHeader,
    createLabeledInput,
    createFormContent,
    executeCommandWithResponse,
    makeQueryString
}

export default handlerUtils
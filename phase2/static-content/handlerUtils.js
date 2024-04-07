import views from "./viewsCreators.js";

function changeHash(hash) {
    window.location.hash = hash;
}

const updateButtonState = (searchButton, inputDev, inputGenres) => {
    searchButton.disabled = !inputDev.value.trim() && !inputGenres.value.trim()
}

function updateSearchButton(searchButton, inputDev, inputGenres) {
    const update = () => updateButtonState(searchButton, inputDev, inputGenres)
    inputDev.addEventListener("input", update)
    inputGenres.addEventListener("input", update)
}

function isOkResponse(response) {
    return response.status >= 200 && response.status < 399
}

function createHeader() {
    return views.h1({}, "Search Sessions: ");
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

function sessionHrefConstructor(session) {
    return [
        views.a({href: `#sessionDetails/${session.sid}`}, "Session" + session.sid),
        views.p()
        ]
}

const handlerUtils = {
    changeHash,
    updateSearchButton,
    isOkResponse,
    createStateInputs,
    createHeader,
    createLabeledInput,
    createFormContent,
    sessionHrefConstructor
}

export default handlerUtils
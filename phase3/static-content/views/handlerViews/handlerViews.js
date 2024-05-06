import views from "../viewsCreators.js";
import handlerUtils from "../../handlers/handlerUtils/handlerUtils.js";
import constants from "../../constants/constants.js";

function createHeader(text) {
    return views.h1({}, text);
}

function createSearchPlayerView() {
    const h1 = views.h1({}, "Search Player:");
    const form =
        views.form({action: "#playerDetails", method: "get"},
        views.input({type: "text", id: "pid", maxLength: 10}),
        views.button({type: "submit", class: "submit-button",}, "Player Details")
    );
    return [h1, form];
}

function createLabeledInput(labelText, inputType, inputId) {
    return [
        views.label({qualifiedName: "for", value: inputId}, labelText),
        views.input({type: inputType, id: inputId}),
        views.p()
    ];
}

function hrefConstructor(hrefBase, id, textBase) {
    return [
        views.a({href: `${hrefBase}/${id}`}, `${textBase} ${id}`),
        views.p()
    ]
}

function hrefButtonView(textContent, query) {
    const backButton = views.button({type: "button", class: "general-button"}, textContent);
    backButton.addEventListener('click', () => {
        window.location.hash = query;
    });
    return backButton;
}

function createBackButtonView() {
    const backButton = views.button({type: "button", class: "general-button"}, "Back");
    backButton.addEventListener('click', () => {
        window.history.back();
    });
    return backButton;
}

function createPagination(query, hash, hasNext) {
    const container = views.div({class: "pagination-container"});

    const prevButton = views.button({id: "prev", type: "button"}, "<");
    if (query.get("offset") === 0) {
        prevButton.disabled = true;
    }
    prevButton.addEventListener('click', () => {
        if (query.get("offset") > 0) {
            query.set("offset", query.get("offset") - constants.ELEMENTS_PER_PAGE);
            handlerUtils.changeHash(`${hash}?${handlerUtils.makeQueryString(query)}`);
        }
    });

    const nextButton = views.button({id: "next", type: "button"}, ">");
    if (!hasNext) {
        nextButton.disabled = true;
    }
    nextButton.addEventListener('click', () => {
        if (hasNext) {
            query.set("offset", query.get("offset") + constants.ELEMENTS_PER_PAGE);
            handlerUtils.changeHash(`${hash}?${handlerUtils.makeQueryString(query)}`);
        }
    });

    container.appendChild(prevButton);
    container.appendChild(nextButton);

    return container;
}

const handlerViews = {
    hrefButtonView,
    createHeader,
    createLabeledInput,
    hrefConstructor,
    createBackButtonView,
    createPagination,
    createHomeView: createSearchPlayerView,
}

export default handlerViews;

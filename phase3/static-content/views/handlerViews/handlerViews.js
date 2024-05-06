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
        views.input({type: "text", id: "pid", maxLength: 10, placeholder: "Player userName"}),
        views.button({type: "submit"}, "Player Details")
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
        views.a({href: `${hrefBase}/${id}`}, `${textBase}`),
        views.p()
    ]
}

function sessionsButtonView(textContent, query) {
    const backButton = views.button({type: "button"}, textContent);
    backButton.addEventListener('click', () => {
        window.location.hash = query;
    });
    return backButton;
}

function createBackButtonView() {
    const backButton = views.button({type: "button"}, "Back");
    backButton.addEventListener('click', () => {
        window.history.back();
    });
    return backButton;
}

function createPagination(query, hash, hasNext) {
    const prevButton = views.button({id: "prev", type: "button"}, "Previous")
    if(query.get("offset") === 0) {
        prevButton.disabled = true;
    }
    prevButton.addEventListener('click', () => {
        if (query.get("offset") > 0) {
            query.set("offset", query.get("offset") - constants.LIMIT)
            handlerUtils.changeHash(`${hash}?${handlerUtils.makeQueryString(query)}`)
        }
    })
    const nextButton = views.button({id: "next", type: "button"}, "Next")
    if(!hasNext) {
        nextButton.disabled = true;
    }
    nextButton.addEventListener('click', () => {
        if (hasNext) {
            query.set("offset", query.get("offset") + constants.LIMIT)
            handlerUtils.changeHash(`${hash}?${handlerUtils.makeQueryString(query)}`)
        }
    })

    return views.div(
        {},
        prevButton,
        nextButton,
    )
}

const handlerViews = {
    sessionsButtonView,
    createHeader,
    createLabeledInput,
    hrefConstructor,
    createBackButtonView,
    createPagination,
    createSearchPlayerView,
}

export default handlerViews;

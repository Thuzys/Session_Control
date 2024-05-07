import views from "../viewsCreators.js";
import handlerUtils from "../../handlers/handlerUtils/handlerUtils.js";
import constants from "../../constants/constants.js";
import {fetcher} from "../../utils/fetchUtils.js";

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

function hrefConstructor(hrefBase, id, textBase, offset = undefined, limit = undefined) {
    if (offset !== undefined) {
        return [
            views.a({href: `${hrefBase}/${id}?offset=${offset}`}, `${textBase}`),
            views.p()
        ]
    } else {
        return [
            views.a({href: `${hrefBase}/${id}`}, `${textBase}`),
            views.p()
        ]
    }
}

function hrefButtonView(textContent, query) {
    const backButton = views.button({type: "button", class: "general-button"}, textContent);
    backButton.addEventListener('click', () => {
        window.location.hash = query;
    });
    return backButton;
}

function createBackButtonView(hasHistory = undefined) {
    const backButton = views.button({type: "button", class: "general-button"}, "Back");
    backButton.addEventListener('click', () => {
        if (hasHistory) {
            handlerUtils.changeHash(hasHistory)
        } else {
            window.history.back();
        }
    });
    return backButton;
}

function createDeleteSessionButtonView(session) {
    const deleteSessionButton = views.button({type: "submit", class: "general-button"}, "Delete Session");
    deleteSessionButton.addEventListener('click', (e) => {
        e.preventDefault();
        fetcher.del(constants.API_BASE_URL + constants.SESSION_ID_ROUTE + session.sid, constants.TOKEN )
            .then(() => {
                handlerUtils.changeHash("#sessions?pid=" + constants.TEMPORARY_USER_ID + "&offset=0");
            })
    });
    return deleteSessionButton;
}

function createLeaveSessionButtonView(session) {
    const leaveSessionButton = views.button({type: "submit", class: "general-button"}, "Leave Session");
    leaveSessionButton.addEventListener('click', (e) => {
        e.preventDefault();
        fetcher.del(constants.API_BASE_URL + constants.SESSION_ID_ROUTE + session.sid + "/" + constants.TEMPORARY_USER_ID, constants.TOKEN)
            .then(() => {
                handlerUtils.changeHash("#sessions?pid=" + constants.TEMPORARY_USER_ID + "&offset=0");
            })
    });
    return leaveSessionButton;
}

function createPagination(query, hash, hasNext, elementsPerPage = constants.ELEMENTS_PER_PAGE) {
    const container = views.div({class: "pagination-container"});

    const prevButton = views.button({id: "prev", type: "button"}, "<");
    if (query.get("offset") === 0) {
        prevButton.disabled = true;
    }
    prevButton.addEventListener('click', () => {
        if (query.get("offset") > 0) {
            query.set("offset", query.get("offset") - elementsPerPage);
            handlerUtils.changeHash(`${hash}?${handlerUtils.makeQueryString(query)}`);
        }
    });

    const nextButton = views.button({id: "next", type: "button"}, ">");
    if (!hasNext) {
        nextButton.disabled = true;
    }
    nextButton.addEventListener('click', () => {
        if (hasNext) {
            query.set("offset", query.get("offset") + elementsPerPage);
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
    createSearchPlayerView,
    createDeleteSessionButtonView,
    createLeaveSessionButtonView
}

export default handlerViews;

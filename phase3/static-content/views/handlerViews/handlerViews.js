import views from "../viewsCreators.js";
import handlerUtils from "../../handlers/handlerUtils/handlerUtils.js";
import constants from "../../constants/constants.js";
import {fetcher} from "../../utils/fetchUtils.js";

/**
 * Create h1 view with text
 * @param text
 * @returns {*}
 */
function createHeader(text) {
    return views.h2({class:"w3-wide centered"}, text);
}

/**
 * Create labeled input
 * @param labelText label text content
 * @param inputType input type (text, password, etc.)
 * @param inputId input id attribute value
 * @returns {*[]} labeled input view
 */
function createLabeledInput(labelText, inputType, inputId) {
    return [
        views.label({qualifiedName: "for", value: inputId}, labelText),
        views.input({type: inputType, id: inputId}),
        views.p()
    ];
}

/**
 * Create href view with text
 * @param hrefBase base of the href
 * @param id id of the href
 * @param textBase text of the href
 * @param offset offset parameter of the href (optional)
 * @param limit limit parameter of the href (optional)
 * @returns {*[]} href view
 */
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

/**
 * Create href button view with text content and query
 * @param textContent text content of the button
 * @param query query change on click
 * @returns {*} href button view
 */
function hrefButtonView(textContent, query) {
    const backButton = views.button({type: "button", class: "general-button"}, textContent);
    backButton.addEventListener('click', () => {
        window.location.hash = query;
    });
    return backButton;
}

/**
 * Create back button view
 * @returns {*} back button view
 */
function createBackButtonView() {
    const backButton = views.button({type: "button", class: "general-button"}, "Back");
    backButton.addEventListener('click', () => {
            window.history.back();
    });
    return backButton;
}

/**
 * Create delete or leave session button view
 * @param session session to delete or leave
 * @param isLeaveButton if true create leave button, else create delete button
 * @returns {*} delete session button view
 */
function createDeleteOrLeaveSessionButtonView(session, isLeaveButton = false) {
    const buttonText = isLeaveButton ? "Leave Session" : "Delete Session";
    const button = views.button({type: "submit", class: "general-button"}, buttonText);
    button.addEventListener('click', (e) => {
        e.preventDefault();
        let url = constants.API_BASE_URL + constants.SESSION_ID_ROUTE + session.sid;
        if (isLeaveButton) {
            url += "/" + constants.TEMPORARY_USER_ID;
        }
        fetcher.del(url, constants.TOKEN)
            .then(() => {
                handlerUtils.changeHash("#sessions?pid=" + constants.TEMPORARY_USER_ID + "&offset=0");
            })
    });
    return button;
}

/**
 * Create pagination view
 * @param query query
 * @param hash hash to change
 * @param hasNext if there is next page
 * @param elementsPerPage elements per page
 * @returns {*} pagination view
 */
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
    createDeleteOrLeaveSessionButtonView,
}

export default handlerViews;

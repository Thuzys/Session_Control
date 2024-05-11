import views from "../viewsCreators.js";
import handlerUtils from "../../handlers/handlerUtils/handlerUtils.js";
import constants from "../../constants/constants.js";
import {fetcher} from "../../utils/fetchUtils.js";

/**
 * Create labeled radio button
 *
 * @param name
 * @param value
 * @param id
 * @returns {{radioButton, label}}
 */

function createLabeledRadioButton(name, value, id) {
    const label = views.label(
        { qualifiedName: "for", value: id },
        `${value.charAt(0).toUpperCase() + value.slice(1)}: `
    );
    const radioButton = views.radioButton({ name, value, id });
    return { label, radioButton };
}

/**
 * Create labeled input
 *
 * @param id
 * @param placeholder
 * @returns {*}
 */
function createLabeledInput(id, placeholder) {
    return views.input({ type: "text", id, placeholder });
}

/**
 * Add toggle event listeners
 *
 * @param toggleFn
 * @param elements
 */
function addToggleEventListeners(toggleFn, ...elements) {
    elements.forEach(el => el.addEventListener("input", toggleFn));
}

/**
 * Create header
 *
 * @param text
 * @returns {*}
 */
function createHeader(text) {
    return views.h1({}, text);
}

/**
 * Create a href
 *
 * @param hrefBase
 * @param id
 * @param textBase
 * @param offset
 * @param limit
 * @returns {*[]}
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
 * Href button view
 *
 * @param textContent
 * @param query
 * @returns {any}
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
 *
 * @returns {*} back button view
 */
function createBackButtonView() {
    const backButton = views.button({type: "button", class: "general-button"}, "Back");
    backButton.addEventListener('click', () => {
        window.history.back();
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
        handlerUtils.changeHash("#removePlayer?sid=" + session.sid)
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

function createUpdateSessionButtonView(session) {
    const updateSessionButton = views.button({type: "submit", class: "general-button"}, "Update Session");
    updateSessionButton.addEventListener('click', (e) => {
        e.preventDefault();
        handlerUtils.changeHash("#updateSession?sid=" + session.sid)
    });
    return updateSessionButton;
}

/**
 * Show alert
 *
 * @param message
 */
function showAlert(message) {
    let modal = document.getElementById("alertModal");
    if (!modal) {
        modal = document.createElement("div");
        modal.id = "alertModal";
        modal.className = "alert-modal";
        document.body.appendChild(modal);

        const alertContent = document.createElement("div");
        alertContent.className = "alert-content";
        modal.appendChild(alertContent);

        const messageText = document.createElement("div");
        messageText.id = "alertMessage";
        alertContent.appendChild(messageText);

        const buttonContainer = document.createElement("div");
        buttonContainer.className = "alert-buttons";
        alertContent.appendChild(buttonContainer);

        const closeButton = document.createElement("button");
        closeButton.innerText = "OK";
        closeButton.onclick = () => {
            modal.style.display = "none";
        };

        buttonContainer.appendChild(closeButton);
    }

    document.getElementById("alertMessage").innerText = message;
    modal.style.display = "flex";
}

/**
 * Check if ul has item
 *
 * @param item
 * @param children
 * @returns {boolean}
 */
function ulHasItem(item, children) {
    return Array.from(children).some(child => {
        if(!child.childNodes || child.childNodes.length === 0) return false
        return Array.from(child.childNodes).some(node => node.nodeType === Node.TEXT_NODE && node.data === item)
    })
}

function toggleButtonState(button, condition) {
    button.disabled = condition
}

const handlerViews = {
    hrefButtonView,
    createHeader,
    createLabeledInput,
    hrefConstructor,
    createBackButtonView,
    createPagination,
    showAlert,
    toggleButtonState,
    createDeleteSessionButtonView,
    createLeaveSessionButtonView,
    createUpdateSessionButtonView,
    createLabeledRadioButton,
    addToggleEventListeners,
    ulHasItem,
}

export default handlerViews

import views from "../viewsCreators.js";
import handlerUtils from "../../handlers/handlerUtils/handlerUtils.js";
import constants from "../../constants/constants.js";
import sessionHandlers from "../../handlers/sessionHandlers.js";

/**
 * Create a radio button with a label.
 * @param labelText text for the label
 * @param name name for the input element
 * @returns {HTMLElement} label element that contains the input and span elements
 */
function createRadioButton(labelText, name) {
    const input = views.input({type: "radio", class: "particles-checkbox", name: "state", id: labelText, value: labelText});
    const span = views.span({},name);
    return views.label({class: "particles-checkbox-container"}, input, span);
}

/**
 * Create labeled input
 * @param id
 * @param placeholder
 * @returns {*}
 */
function createLabeledInput(id, placeholder) {
    return views.input({type: "text", id, placeholder });
}

/**
 * Add toggle event listeners
 * @param toggleFn
 * @param elements
 */
function addToggleEventListeners(toggleFn, ...elements) {
    elements.forEach(el => el.addEventListener("input", toggleFn));
}

/**
 * Create header
 * @param text
 * @returns {*}
 */
function createHeader(text) {
    return views.h2({class:"w3-wide centered"}, text);
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

/**
 * Show alert
 * @param message
 */
function showAlert(message) {
    let modal = document.getElementById("alertModal");
    if (!modal) {
        modal = views.div({class: "alert-modal", id: "alertModal"});
        document.body.appendChild(modal);

        const alertContent = views.div({class: "alert-content"});
        modal.appendChild(alertContent);

        const messageText = views.div({id: "alertMessage"});
        alertContent.appendChild(messageText);

        const buttonContainer = views.div({class: "alert-buttons"});
        alertContent.appendChild(buttonContainer);

        const closeButton = views.button({type: "button"}, "OK");
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

/**
 * Toggle button state
 * @param button
 * @param condition
 */
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
    addToggleEventListeners,
    ulHasItem,
    createRadioButton
}

export default handlerViews

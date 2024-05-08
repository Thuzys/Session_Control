import views from "../viewsCreators.js";
import handlerUtils from "../../handlers/handlerUtils/handlerUtils.js";
import constants from "../../constants/constants.js";

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
 * Create search player view
 *
 * @returns {*[]}
 */
function createSearchPlayerView() {
    const h1 = views.h1({}, "Search Player:");
    const form =
        views.form({action: "#playerDetails", method: "get"},
        views.input({type: "text", id: "pid", maxLength: 10}),
        views.button({type: "submit"}, "Player Details")
    );
    return [h1, form];
}

/**
 * Create labeled input
 *
 * @param labelText
 * @param inputType
 * @param inputId
 * @returns {*[]}
 */
function createLabeledInput(labelText, inputType, inputId) {
    return [
        views.label({qualifiedName: "for", value: inputId}, labelText),
        views.input({type: inputType, id: inputId}),
        views.p()
    ]
}

/**
 * Href constructor
 *
 * @param hrefBase
 * @param id
 * @param textBase
 * @returns {*[]}
 */
function hrefConstructor(hrefBase, id, textBase) {
    return [
        views.a({href: `${hrefBase}/${id}`}, `${textBase} ${id}`),
        views.p()
    ]
}

/**
 * Sessions button view
 *
 * @param textContent
 * @param query
 * @returns {*}
 */
function sessionsButtonView(textContent, query) {
    const backButton = views.button({type: "button"}, textContent);
    backButton.addEventListener('click', () => {
        window.location.hash = query;
    });
    return backButton;
}

/**
 * Create back button view
 *
 * @returns {*}
 */
function createBackButtonView() {
    const backButton = views.button({type: "button"}, "Back");
    backButton.addEventListener('click', () => {
        window.history.back();
    });
    return backButton;
}

/**
 * Create pagination
 *
 * @param query
 * @param hash
 * @param hasNext
 * @returns {*}
 */
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

function toggleButtonState(button, condition) {
    button.disabled = condition
}


const handlerViews = {
    sessionsButtonView,
    createHeader,
    createLabeledInput,
    hrefConstructor,
    createBackButtonView,
    createPagination,
    createHomeView: createSearchPlayerView,
    showAlert,
    toggleButtonState,
}

export default handlerViews;

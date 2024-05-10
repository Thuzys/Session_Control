/**
 * Create an element with the given tag name, attributes, and children.
 * @param tagName tag name of the element
 * @param attributes attributes of the element
 * @param children children of the element
 * @returns { HTMLElement } element
 */
function createElement(tagName, attributes = {}, ...children) {
    const element = document.createElement(tagName);
    for (const [key, value] of Object.entries(attributes)) {
        element.setAttribute(key, value);
    }
    for (const child of children) {
        if (typeof child === "string") {
            element.appendChild(document.createTextNode(child));
        } else if (child instanceof Node) {
            element.appendChild(child)
        }
    }
    return element;
}

/**
 * Create a datalist element with the given attributes and options.
 * @param attributes
 * @param options
 * @returns {HTMLDataListElement}
 */
function datalist(attributes = {}, ...options) {
    return createElement("datalist", attributes, ...options);
}

/**
 * Create an option element with the given attributes.
 * @param attributes
 * @returns {HTMLOptionElement}
 */
function option(attributes = {}) {
    return createElement("option", attributes);
}

/**
 * Create ul element with the given items.
 * @param items
 * @returns {HTMLUListElement}
 */
function ul(...items) {
    return createElement("ul", {id: "ul"}, ...items.map(li))
}

/**
 * Create li element with the given content.
 * @param content
 * @returns {HTMLLIElement}
 */
function li(content) {
    return createElement("li", {}, content);
}

/**
 * Create div element with the given attributes and children.
 * @param attributes
 * @param children
 * @returns {HTMLDivElement}
 */
function div(attributes = {}, ...children) {
    return createElement("div", attributes, ...children);
}

/**
 * Create p element with the given attributes and children.
 * @param attributes
 * @param children
 * @returns {HTMLParagraphElement}
 */
function p(attributes = {}, ...children) {
    return createElement("p", attributes, ...children);
}

/**
 * Create h1 element with the given attributes and children.
 * @param attributes
 * @param children
 * @returns {HTMLHeadingElement}
 */
function h1(attributes = {}, ...children) {
    return createElement("h1", attributes, ...children);
}

/**
 * Create h2 element with the given attributes and children.
 * @param attributes
 * @param children
 * @returns {HTMLHeadingElement}
 */
function h2(attributes = {}, ...children) {
    return createElement("h2", attributes, ...children);
}

/**
 * Create h3 element with the given attributes and children.
 * @param attributes
 * @param children
 * @returns {HTMLHeadingElement}
 */
function h3(attributes = {}, ...children) {
    return createElement("h3", attributes, ...children);
}

/**
 * Create a element with the given attributes and children.
 * @param attributes
 * @param children
 * @returns {HTMLAnchorElement}
 */
function a(attributes = {}, ...children) {
    return createElement("a", attributes, ...children);
}

/**
 * Create an input element with the given attributes.
 * @param attributes
 * @returns {HTMLInputElement}
 */
function input(attributes = {}) {
    return createElement("input", attributes);
}

/**
 * Create a label element with the given attributes and text content.
 * @param attributes
 * @param textContent
 * @returns {HTMLLabelElement}
 */
function label(attributes = {}, textContent) {
    return createElement("label", attributes, textContent);
}

/**
 * Create a form element with the given attributes and children.
 * @param attributes
 * @param children
 * @returns {HTMLFormElement}
 */
function form(attributes = {}, ...children) {
    return createElement("form", attributes, ...children);
}

/**
 * Create a radio button element with the given attributes.
 * @param attributes
 * @returns {HTMLInputElement}
 */
function radioButton(attributes = {}) {
    return createElement("input", {type: "radio", class: "state-button", ...attributes});
}

/**
 * Create a radio button label element with the given forId, text content, and attributes.
 * @param forId
 * @param textContent
 * @param attributes
 * @returns {HTMLLabelElement}
 */
function radioButtonLabel(forId, textContent, attributes = {}) {
    return createElement("label", {for: forId, ...attributes}, textContent);
}

/**
 * Create a button element with the given attributes and text content.
 * @param attributes
 * @param textContent
 * @returns {HTMLButtonElement}
 */
function button(attributes = {}, textContent) {
    return createElement("button", attributes, textContent);
}

/**
 * Create an image element with the given attributes.
 * @param attributes
 * @returns {HTMLImageElement}
 */
function image(attributes) {
    return createElement('img', attributes);
}

const views = {
    datalist,
    option,
    ul,
    li,
    form,
    input,
    label,
    radioButton,
    radioButtonLabel,
    p,
    div,
    h1,
    button,
    a,
    h2,
    h3,
    image
}

export default views

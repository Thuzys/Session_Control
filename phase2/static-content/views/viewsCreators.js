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

function datalist(attributes = {}, ...options) {
    return createElement("datalist", attributes, ...options);
}

function option(attributes = {}) {
    return createElement("option", attributes);
}

function ul(...items) {
    return createElement("ul", {id: "ul"}, ...items.map(li))
}

function li(content) {
    return createElement("li", {}, content);
}

function div(attributes = {}, ...children) {
    return createElement("div", attributes, ...children);
}

function p(attributes = {}, ...children) {
    return createElement("p", attributes, ...children);
}

function h1(attributes = {}, ...children) {
    return createElement("h1", attributes, ...children);
}

function h2(attributes = {}, ...children) {
    return createElement("h2", attributes, ...children);
}

function h3(attributes = {}, ...children) {
    return createElement("h3", attributes, ...children);
}

function a(attributes = {}, ...children) {
    return createElement("a", attributes, ...children);
}

function input(attributes = {}) {
    return createElement("input", attributes);
}

function label(attributes = {}, textContent) {
    return createElement("label", attributes, textContent);
}

function form(attributes = {}, ...children) {
    return createElement("form", attributes, ...children);
}

function radioButton(attributes = {}) {
    return createElement("input", {type: "radio", class: "state-button", ...attributes});
}

function radioButtonLabel(forId, textContent, attributes = {}) {
    return createElement("label", {for: forId, ...attributes}, textContent);
}

function button(attributes = {}, textContent) {
    return createElement("button", attributes, textContent);
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
    h3
}

export default views

import views from "../viewsCreators.js";
import handlerViews from "./handlerViews.js";

/**
 * Create log in view
 * @returns {HTMLDivElement} log in view
 */
function createLoginView() {
    const container = views.div({class: "player-details-container"});
    const header = handlerViews.createHeader("Log In");
    const username = handlerViews.createLabeledInput("username", "Username");
    const password = handlerViews.createLabeledPasswordInput("password", "Password");
    const div = views.div({},
        username,
        views.p(),
        password,
        views.p(),
    )
    const submitButton = views.button({type: "submit", class: "general-button", disabled: true}, "Log In");

    const toggleLoginButton = () => {
        handlerViews.toggleButtonState(
            submitButton,
            !canLogin(username.value.trim(), password.value.trim())
        )
    }

    handlerViews.addToggleEventListeners(
        toggleLoginButton,
        username,
        password
    )

    const span = views.span({class: "w3-opacity centered"}, "Don't have an account? ", views.a({href: "#register"}, "Create one!"));

    container.replaceChildren(header, views.hr({class:"w3-opacity"}), div, submitButton, views.p(), span);
    return container;
}

/**
 * Check if username and password are not empty
 * @param username username
 * @param password password
 * @returns {*} true if username and password are not empty
 */
function canLogin(username, password) {
    return username && password;
}

/**
 * Create register view
 * @returns {HTMLDivElement} register view
 */
function createRegisterView() {
    const container = views.div({class: "player-details-container"});
    const header = handlerViews.createHeader("Create Account");
    const name = handlerViews.createLabeledInput("name", "Name");
    const username = handlerViews.createLabeledInput("username", "Username (optional)");
    const email = handlerViews.createLabeledEmailInput("email", "Email");
    const password = handlerViews.createLabeledPasswordInput("password", "Password");
    const confirmPassword = handlerViews.createLabeledPasswordInput("confirm-password", "Confirm Password");

    const div = views.div({},
        name,
        views.p(),
        username,
        views.p(),
        email,
        views.p(),
        password,
        views.p(),
        confirmPassword,
        views.p(),
    )

    const submitButton = views.button({type: "submit", class: "general-button", disabled: true}, "Create Account");

    const toggleCreateAccountButton = () => {
        handlerViews.toggleButtonState(
            submitButton,
            !canCreateAccount(
                name.value.trim(),
                email.value.trim(),
                password.value.trim(),
                confirmPassword.value.trim()
            )
        )
    }

    handlerViews.addToggleEventListeners(
        toggleCreateAccountButton,
        name,
        email,
        password,
        confirmPassword
    )

    container.replaceChildren(header, views.hr({class:"w3-opacity"}), div, submitButton);
    return container;

}

/**
 * Check if name, username, email, password and confirm password are not empty
 * @param name name of the user
 * @param email email of the user
 * @param password password of the user
 * @param confirmPassword confirm password of the user
 * @returns {*} true if name, username, email, password and confirm password are not empty
 */
function canCreateAccount(name, email, password, confirmPassword) {
    return name && email && password && confirmPassword;
}

export default {
    createLoginView,
    createRegisterView
}
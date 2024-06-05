import views from "../viewsCreators.js";
import handlerViews from "./handlerViews.js";

/**
 * Create log in view
 * @returns {HTMLDivElement} log in view
 */
function createLoginView() {
    const container = views.div({class: "player-details-container"});
    const header = handlerViews.createHeader("Log In");
    const username = handlerViews.createLabeledInput("text", "username", "Username");
    const password = handlerViews.createLabeledInput("password", "password", "Password");
    const div = views.div({},
        username,
        views.p(),
        password,
        views.p(),
    )
    const submitButton =
        views
            .button(
                {
                    id: "login-button",
                    type: "submit",
                    class: "general-button",
                    disabled: true
                },
                "Log In",
            );

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

    const span = views.span({class: "w3-opacity centered"},
        "Don't have an account? ", views.a({href: "#register",  style: "padding-left: 5px;"}, "Create one!"));

    const form = views.form(
        {},
        views.hr({class:"w3-opacity"}),
        div,
        submitButton,
        views.p(),
        span,
    )
    container.replaceChildren(header, form);
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
    const name = handlerViews.createLabeledInput("text", "name", "Name");
    const username = handlerViews.createLabeledInput("text", "username", "Username (Optional)");
    const email = handlerViews.createLabeledInput("email", "email", "Email");
    const password = handlerViews.createLabeledInput("password", "password", "Password");
    const confirmPassword = handlerViews.createLabeledInput("password", "confirm-password", "Confirm Password");

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
        confirmPassword,

    )

    const span = views.span({class: "w3-opacity centered"},
        "Already have an account?", views.a({href: "#logIn",  style: "padding-left: 5px;"}, "Log in"));

    const form = views.form(
        {},
        views.hr({class:"w3-opacity"}),
        div,
        submitButton,
        views.p(),
        span
    );

    container.replaceChildren(header, form);
    return container;

}

/**
 * Check if the name, username, email, password and confirm password are not empty
 * @param name name of the user
 * @param email email of the user
 * @param password password of the user
 * @param confirmPassword confirm password of the user
 * @returns {*} true if the name, username, email, password and confirm password are not empty
 */
function canCreateAccount(name, email, password, confirmPassword) {
    return name && email && password && confirmPassword;
}

export default {
    createLoginView,
    createRegisterView
}
import constants from "../constants/constants.js";
import {fetcher} from "../utils/fetchUtils.js";
import playerHandlers from "./playerHandlers.js";
import homeHandlerViews from "../views/handlerViews/homeHandlerViews.js";
import handlerViews from "../views/handlerViews/handlerViews.js";
import handlerUtils from "./handlerUtils/handlerUtils.js";

/**
 * Get home page
 * @param mainContent main content of the page
 */
function getHome(mainContent) {
    const pid = sessionStorage.getItem('pid');
    const token = sessionStorage.getItem('token');
    const route = `${constants.PLAYER_ID_ROUTE}${pid}`;
    const url = handlerUtils.createURL(route);

    fetcher
        .get(url, token)
        .then(
            response =>
                playerHandlers.handleGetPlayerDetailsResponse(response, mainContent)
        );
}

/**
 * LogIn page
 * @param mainContent main content of the page
 */
function logIn(mainContent) {
    const container = homeHandlerViews.createLoginView()
    container.onsubmit = (e) => handleLoginSubmit(e);
    mainContent.replaceChildren(container);
}

/**
 * Handle logIn submit event
 * @param e event that triggered submit
 */
function handleLoginSubmit(e) {
    e.preventDefault();
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const url = handlerUtils.createURL(constants.LOGIN_ROUTE);
    const body = {username: username, password: password};

    fetcher
        .post(url, body, undefined, false)
        .then(response => handleLogInRegisterResponse(response))
}

/**
 * Create account page
 * @param mainContent main content of the page
 */
function register(mainContent) {
    const container = homeHandlerViews.createRegisterView()
    container.onsubmit = (e) => handleCreateAccountSubmit(e);
    mainContent.replaceChildren(container);
}

/**
 * Handle create account submit event
 * @param e event that triggered submit
 */
function handleCreateAccountSubmit(e) {
    e.preventDefault();
    const name = document.getElementById('name').value;
    const email = document.getElementById('email').value;
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirm-password').value;

    if (password !== confirmPassword) {
        handlerViews.showAlert('Passwords do not match!');
        return;
    }

    const url = handlerUtils.createURL(constants.PLAYER_ROUTE);
    const body = {
        name: name,
        username: username,
        email: email,
        password: password,
    };

    fetcher
        .post(url, body)
        .then(response => handleLogInRegisterResponse(response))
}

/**
 * Handle login and sign up response
 * @param response response from the server
 */
function handleLogInRegisterResponse(response) {
    sessionStorage.setItem('pid', response.pid);
    sessionStorage.setItem('isAuthenticated', 'true');
    sessionStorage.setItem('token', response.token);
    handlerUtils.changeHash('#players/home');
}

/**
 * Log out the user
 */
function logOut() {
    if (sessionStorage.getItem('isAuthenticated') === 'false') {
        return;
    }
    sessionStorage.setItem('isAuthenticated', 'false');

    const route = `${constants.PLAYER_ID_ROUTE}${sessionStorage.getItem("pid")}`
    const url = handlerUtils.createURL(route);

    fetcher
        .put(url, sessionStorage.getItem('token'))
        .then(_ => {})

    sessionStorage.removeItem('token');
    sessionStorage.removeItem('pid');
    handlerUtils.changeHash('#logIn');
    window.location.reload();
}

export default {
    getHome,
    logIn,
    register,
    logOut
};

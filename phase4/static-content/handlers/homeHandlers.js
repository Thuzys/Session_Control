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
    const url = `${constants.API_BASE_URL}${constants.PLAYER_ID_ROUTE}1`;
    fetcher
        .get(url, constants.TOKEN)
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
    const url = `${constants.API_BASE_URL}${constants.LOGIN_ROUTE}`;
    const body = {username: username, password: password};
    fetcher
        .post(url, body)
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
    const confirmPassword = document.getElementById('confirmPassword').value;

    if (password !== confirmPassword) {
        handlerViews.showAlert('Passwords do not match!');
        return;
    }

    const url = `${constants.API_BASE_URL}${constants.PLAYER_ROUTE}`;
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
        handlerUtils.changeHash('#home');
}

/**
 * Log out the user
 */
function logOut() {
    sessionStorage.removeItem('pid');
    sessionStorage.setItem('isAuthenticated', 'false');
    //TODO(ERASE TOKEN FROM DATABASE)
    handlerUtils.changeHash('#logIn');
    window.location.reload();
}

export default {
    getHome,
    logIn,
    register,
    logOut
};

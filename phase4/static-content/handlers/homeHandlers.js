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
    const route = handlerUtils.createRoute(constants.API_PLAYER_ROUTE, pid);
    const url = handlerUtils.createURL(route);
    const player = sessionStorage.getItem('player');
    if (player == null) {
        fetcher
            .get(url, token)
            .then(
                response => {
                    const userPlayer = {
                        name: response.name,
                        username: response.username,
                        email: response.email,
                        pid: response.pid,
                    }
                    sessionStorage.setItem('player', JSON.stringify(userPlayer));
                    playerHandlers.handleGetPlayerDetailsResponse(userPlayer, mainContent)
                }
    );
        mainContent.replaceChildren(handlerViews.createLoaderView());
    } else {
        playerHandlers.handleGetPlayerDetailsResponse(JSON.parse(player), mainContent);
    }
}

/**
 * LogIn page
 * @param mainContent main content of the page
 */
function logIn(mainContent) {
    const container = homeHandlerViews.createLoginView()
    container.onsubmit = (e) => handleLoginSubmit(e, mainContent);
    mainContent.replaceChildren(container);
}

/**
 * Handle logIn submit event
 * @param e event that triggered submit
 * @param mainContent main content of the page
 */
function handleLoginSubmit(e, mainContent) {
    e.preventDefault();
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const url = handlerUtils.createURL(constants.API_LOGIN_ROUTE);
    const body = {username: username, password: password};
    sessionStorage.setItem('loginParams', JSON.stringify(body));
    fetcher
        .post(url, body, undefined, false, true)
        .then(response => {
            sessionStorage.removeItem('loginParams')
            handleLogInRegisterResponse(response)
        })
    mainContent.replaceChildren(handlerViews.createLoaderView());
}

/**
 * Create account page
 * @param mainContent main content of the page
 */
function register(mainContent) {
    const container = homeHandlerViews.createRegisterView()
    container.onsubmit = (e) => handleCreateAccountSubmit(e, mainContent);
    mainContent.replaceChildren(container);
}

/**
 * Handle create account submit event
 * @param e event that triggered submit
 * @param mainContent main content of the page
 */
function handleCreateAccountSubmit(e, mainContent) {
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

    const url = handlerUtils.createURL(constants.API_PLAYER_ROUTE);
    const body = {
        name: name,
        email: email,
        password: password,
    };

    if (username !== '') {
        body.username = username;
    }
    fetcher
        .post(url, body, undefined, false, true)
        .then(response => {
            handleLogInRegisterResponse(response)
        })
    mainContent.replaceChildren(handlerViews.createLoaderView());
}

/**
 * Handle login and sign up response
 * @param response response from the server
 */
function handleLogInRegisterResponse(response) {
    sessionStorage.setItem('pid', response.pid);
    sessionStorage.setItem('isAuthenticated', 'true');
    sessionStorage.setItem('token', response.token);
    handlerUtils.changeHash(constants.PLAYERS_HOME_ROUTE);
}

/**
 * Log out the user
 */
function logOut() {
    if (sessionStorage.getItem('isAuthenticated') === 'false') {
        return;
    }
    sessionStorage.setItem('isAuthenticated', 'false');
    sessionStorage.removeItem('player');

    const pid = sessionStorage.getItem("pid")
    const route = handlerUtils.createRoute(constants.API_PLAYER_ROUTE, pid);
    const url = handlerUtils.createURL(route);

    fetcher
        .del(url, sessionStorage.getItem('token')).then(_ => {})
    sessionStorage.removeItem('token');
    sessionStorage.removeItem('pid');
    handlerUtils.changeHash(constants.LOGIN_ROUTE);
    window.location.reload();
}

export default {
    getHome,
    logIn,
    register,
    logOut
};

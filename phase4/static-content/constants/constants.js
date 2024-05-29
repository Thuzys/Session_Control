/**
 * The base url for the api
 * @type {string}
 */
const API_BASE_URL = "http://localhost:8080/"

/**
 * The limit of elements to search for
 * @type {number}
 */
const LIMIT = 11

/**
 * The limit of players to search for
 * @type {number}
 */
const LIMIT_PLAYERS = 4

/**
 * The number of elements to display per page
 * @type {number}
 */
const ELEMENTS_PER_PAGE = 10

/**
 * The number of elements to display per page for players
 * @type {number}
 */
const ELEMENTS_PER_PAGE_PLAYERS = 3

/**
 * The route for players related resources
 * @type {string}
 */
const API_PLAYER_ROUTE = "players"

/**
 * The route for sessions related resources
 * @type {string}
 */
const API_SESSION_ROUTE = "sessions"

/**
 * The route for games related resources
 * @type {string}
 */
const API_GAME_ROUTE = "games"

/**
 * The login route
 * @type {string}
 */
const API_LOGIN_ROUTE = API_PLAYER_ROUTE + "/login"

/**
 * The genres route
 * @type {string}
 */
const API_GENRES_ROUTE = "genres"

const PLAYER_ROUTE = "players"

const GAME_ROUTE = "games"

const SESSION_ROUTE = "sessions"

const LOGIN_ROUTE = "logIn"

const REGISTER_ROUTE = "register"

const LOGOUT_ROUTE = "logOut"

const PLAYERS_HOME_ROUTE = `${PLAYER_ROUTE}/home`

const PLAYER_SEARCH_ROUTE = "playerSearch"

const CREATE_GAME_ROUTE = "createGame"

const GAME_SEARCH_ROUTE = "gameSearch"

const GAMES_ID_ROUTE = `${GAME_ROUTE}/:gid`

const PLAYERS_ID_ROUTE = `${PLAYER_ROUTE}/:pid`

const SESSION_SEARCH_ROUTE = "sessionSearch"

const SESSIONS_ID_ROUTE = `${SESSION_ROUTE}/:sid`

const UPDATE_SESSION_ROUTE = "updateSession/:sid"

const CONTACTS_ROUTE = "contacts"

/**
 * general constants used for the application
 */
const constants = {
    API_BASE_URL,
    LIMIT,
    API_PLAYER_ROUTE,
    API_SESSION_ROUTE,
    API_GAME_ROUTE,
    ELEMENTS_PER_PAGE,
    LIMIT_PLAYERS,
    ELEMENTS_PER_PAGE_PLAYERS,
    API_LOGIN_ROUTE,
    API_GENRES_ROUTE,
    LOGIN_ROUTE,
    REGISTER_ROUTE,
    LOGOUT_ROUTE,
    PLAYERS_HOME_ROUTE,
    PLAYER_ROUTE,
    GAME_ROUTE,
    SESSION_ROUTE,
    PLAYER_SEARCH_ROUTE,
    CREATE_GAME_ROUTE,
    GAME_SEARCH_ROUTE,
    GAMES_ID_ROUTE,
    PLAYERS_ID_ROUTE,
    SESSION_SEARCH_ROUTE,
    SESSIONS_ID_ROUTE,
    UPDATE_SESSION_ROUTE,
    CONTACTS_ROUTE
}

export default constants;
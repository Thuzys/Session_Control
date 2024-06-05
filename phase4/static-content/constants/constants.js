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

/**
 * The route for players
 * @type {string}
 */
const PLAYER_ROUTE = "players"

/**
 * The route for games
 * @type {string}
 */
const GAME_ROUTE = "games"

/**
 * The route for sessions
 * @type {string}
 */
const SESSION_ROUTE = "sessions"

/**
 * The login route
 * @type {string}
 */
const LOGIN_ROUTE = "logIn"

/**
 * The register route
 * @type {string}
 */
const REGISTER_ROUTE = "register"

/**
 * The logout route
 * @type {string}
 */
const LOGOUT_ROUTE = "logOut"

/**
 * The players home route
 * @type {string}
 */
const PLAYERS_HOME_ROUTE = `${PLAYER_ROUTE}/home`

/**
 * The player search route
 * @type {string}
 */
const PLAYER_SEARCH_ROUTE = "playerSearch"

/**
 * The create game route
 * @type {string}
 */
const CREATE_GAME_ROUTE = "createGame"

/**
 * The game search route
 * @type {string}
 */
const GAME_SEARCH_ROUTE = "gameSearch"

/**
 * The game id route
 * @type {string}
 */
const GAMES_ID_ROUTE = `${GAME_ROUTE}/:gid`

/**
 * The players id route
 * @type {string}
 */
const PLAYERS_ID_ROUTE = `${PLAYER_ROUTE}/:pid`

/**
 * The session search route
 * @type {string}
 */
const SESSION_SEARCH_ROUTE = "sessionSearch"

/**
 * The session id route
 * @type {string}
 */
const SESSIONS_ID_ROUTE = `${SESSION_ROUTE}/:sid`

/**
 * The update session route
 * @type {string}
 */
const UPDATE_SESSION_ROUTE = "updateSession/:sid"

/**
 * The contacts route
 * @type {string}
 */
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
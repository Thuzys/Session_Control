/**
 * The base url for the api
 * @type {string}
 */
const API_BASE_URL = "http://localhost:8080/"

/**
 * The token used for authentication
 * @type {string}
 */
const TOKEN = "e247758f-02b6-4037-bd85-fc245b84d5f2"

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
const PLAYER_ROUTE = "players"

/**
 * The route for player details related resources by player id
 * @type {string}
 */
const PLAYER_ID_ROUTE = PLAYER_ROUTE + "/"

/**
 * The route for sessions related resources
 * @type {string}
 */
const SESSION_ROUTE = "sessions"

/**
 * The route for session details related resources by session id
 * @type {string}
 */
const SESSION_ID_ROUTE = SESSION_ROUTE + "/"

/**
 * The route for games related resources
 * @type {string}
 */
const GAME_ROUTE = "games"

/**
 * The route for game details related resources by game id
 * @type {string}
 */
const GAME_ID_ROUTE = GAME_ROUTE + "/"

/**
 * The temporary user id used for the application
 * @type {number}
 */
const TEMPORARY_USER_ID = 1

/**
 * The login route
 * @type {string}
 */
const LOGIN_ROUTE = PLAYER_ROUTE + "/login"

/**
 * The genres route
 * @type {string}
 */
const GENRES_ROUTE = "genres"

/**
 * general constants used for the application
 */
const constants = {
    API_BASE_URL,
    TOKEN,
    LIMIT,
    PLAYER_ROUTE,
    PLAYER_ID_ROUTE,
    SESSION_ROUTE,
    SESSION_ID_ROUTE,
    GAME_ROUTE,
    GAME_ID_ROUTE,
    ELEMENTS_PER_PAGE,
    LIMIT_PLAYERS,
    ELEMENTS_PER_PAGE_PLAYERS,
    TEMPORARY_USER_ID,
    LOGIN_ROUTE,
    GENRES_ROUTE,
}

export default constants;
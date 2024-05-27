import constants from "../../constants/constants.js";

/**
 * Change the hash of the current URL
 * @param hash the new hash
 */
function changeHash(hash) {
    window.location.hash = hash;
}

/**
 * Convert children to string
 * @param children the children to convert
 * @returns {string} the children as a string
 */
function childrenToString(children) {
    return Array.from(children)
        .map(child => {
            return Array.from(child.childNodes)
                .filter(node => node.nodeType === Node.TEXT_NODE)
                .map(node => node.data)
                .join('');
        })
        .join(',');
}

/**
 * Make a query string from a query
 * @param query the query to convert
 * @returns {URLSearchParams} the query as a URLSearchParams
 */
function makeQueryString(query) {
    const queryString = new URLSearchParams();
    for (const [key, value] of query) {
        queryString.set(key, value);
    }
    return queryString;
}

/**
 * Create a URL from a route and query
 *
 * @param route the route
 * @param query the query
 * @returns {string} the URL
 */
function createURL(route, query = undefined) {
    return `${constants.API_BASE_URL}${route}${query ? `?${handlerUtils.makeQueryString(query)}` : ""}`
}

const handlerUtils = {
    childrenToString,
    changeHash,
    makeQueryString,
    createURL
}

export default handlerUtils
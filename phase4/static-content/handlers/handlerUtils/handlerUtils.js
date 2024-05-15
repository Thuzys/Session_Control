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

const handlerUtils = {
    childrenToString,
    changeHash,
    makeQueryString,
}

export default handlerUtils
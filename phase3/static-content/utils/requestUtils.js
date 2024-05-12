/**
 * Get the query parameters from the URL
 * @returns the query parameters
 */
function getParams() {
    const list = window.location.hash.replace("#", "").split("/")
    return list.length > 1 ? list[1] : ""
}

/**
 * Get the query parameters from the URL
 * @returns {Map<any, any>} the query parameters
 */
function getQuery() {
    if (window.location.hash.length === 0) {
        return new Map()
    }
    const retMap = new Map();
    const list = window.location.hash
        .replace("#", "")
        .replace("+", " ")
        .split("?")[1]
        .split("&")
    list.forEach(pair => {
        const [key, value] = pair.split("=")
        if (key === 'offset') {
            retMap.set(key, parseInt(value))
        }
        else {
            retMap.set(key, value)
        }
    })
    return retMap
}

/**
 * Get the path from the URL
 * @returns {string} the path
 */
function getPath(){
    return window.location.hash.replace("#", "").split("?")[0]
}

const RequestUtils = {
    getQuery,
    getPath,
    getParams
}

export default RequestUtils

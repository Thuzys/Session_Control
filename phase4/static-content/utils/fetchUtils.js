import handlerViews from "../views/handlerViews/handlerViews.js";

/**
 * Fetches data from the server
 *
 * @param uri the uri to fetch data from
 * @param token the token to use
 * @param isToGoBack if the page should go back if the response is not OK
 * @returns {Promise<*>} the data fetched
 */
async function get(
    uri,
    token = undefined,
    isToGoBack = true,
) {
    return fetchInternal(uri, {}, undefined, token, isToGoBack)
}

/**
 * Deletes data from the server
 *
 * @param uri the uri to delete data from
 * @param token the token to use
 * @param isToGoBack if the page should go back if the response is not OK
 * @returns {Promise<*>}
 */
async function del(
    uri,
    token = undefined,
    isToGoBack = true,
) {
    return fetchInternal(uri, {method: "DELETE"}, undefined, token, isToGoBack)
}

/**
 * Updates data on the server
 *
 * @param uri the uri to update data on
 * @param token the token to use
 * @param body the data to update
 * @param isToGoBack if the page should go back if the response is not OK
 * @returns {Promise<*>}
 */
async function put(
    uri,
    token = undefined,
    body = undefined,
    isToGoBack = true,
) {
    return fetchInternal(uri, {method: "PUT"}, body, token, isToGoBack)
}

/**
 * Posts data to the server
 *
 * @param uri the uri to post data to
 * @param body the data to post
 * @param token the token to use
 * @param isToGoBack if the page should go back if the response is not OK
 * @returns {Promise<*>}
 */
async function post(
    uri,
    body,
    token = undefined,
    isToGoBack = true,
) {
    return fetchInternal(uri, {method: "POST"}, body, token, isToGoBack)
}

/**
 * Internal fetch function to fetch data from the server
 *
 * @param uri the uri to fetch data from
 * @param options the options to use
 * @param body the data to fetch
 * @param token the token to use
 * @param isToGoBack if the page should go back if the response is not OK
 * @returns {Promise<any>} the data fetched
 */
async function fetchInternal(
    uri, options = {},
    body = undefined,
    token= undefined,
    isToGoBack = true,
) {
    if(body || token){
        if(token){
            options.headers = {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        }
        else {
            options.headers = {
                'Content-Type': 'application/json'
            }
        }
        if (body) options.body = JSON.stringify(body)
    }
    return fetch(uri, options)
        .then(response => {
            if (!isResponseOK(response)) {
                response.json().then(json => {
                    const text = json.error
                    if (isToGoBack) {
                        window.history.back()
                    }
                    handlerViews.showAlert(text)
                })
            } else {
                return response.json()
            }
        })
}

/**
 * Checks if the response is OK
 *
 * @param response the response to check
 * @returns {boolean} true if the response is OK
 */
function isResponseOK(response) {
    return response.status >= 200 && response.status < 399
}

export const fetcher = {
    get,
    del,
    put,
    post,
}

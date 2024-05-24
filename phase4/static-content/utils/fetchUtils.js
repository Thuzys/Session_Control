import handlerViews from "../views/handlerViews/handlerViews.js";

/**
 * Fetches data from the server
 *
 * @param uri the uri to fetch data from
 * @param token the token to use
 * @param isToGoBack if the page should go back if the response is not OK
 * @param onError the function to call if an error occurs
 * @returns {Promise<*>} the data fetched
 */
async function get(
    uri,
    token = undefined,
    isToGoBack = true,
    onError = undefined,
) {
    return fetchInternal(uri, {}, undefined, token, isToGoBack, onError)
}

/**
 * Deletes data from the server
 *
 * @param uri the uri to delete data from
 * @param token the token to use
 * @param isToGoBack if the page should go back if the response is not OK
 * @param onError the function to call if an error occurs
 * @returns {Promise<*>}
 */
async function del(
    uri,
    token = undefined,
    isToGoBack = true,
    onError = undefined,
) {
    return fetchInternal(uri, {method: "DELETE"}, undefined, token, isToGoBack, onError)
}

/**
 * Updates data on the server
 *
 * @param uri the uri to update data on
 * @param token the token to use
 * @param body the data to update
 * @param isToGoBack if the page should go back if the response is not OK
 * @param onError the function to call if an error occurs
 * @returns {Promise<*>}
 */
async function put(
    uri,
    token = undefined,
    body = undefined,
    isToGoBack = true,
    onError = undefined,
) {
    return fetchInternal(uri, {method: "PUT"}, body, token, isToGoBack, onError)
}

/**
 * Posts data to the server
 *
 * @param uri the uri to post data to
 * @param body the data to post
 * @param token the token to use
 * @param isToGoBack if the page should go back if the response is not OK
 * @param onError the function to call if an error occurs
 * @returns {Promise<*>}
 */
async function post(
    uri,
    body,
    token = undefined,
    isToGoBack = true,
    onError = undefined,
) {
    return fetchInternal(uri, {method: "POST"}, body, token, isToGoBack, onError)
}

/**
 * Internal fetch function to fetch data from the server
 *
 * @param uri the uri to fetch data from
 * @param options the options to use
 * @param body the data to fetch
 * @param token the token to use
 * @param isToGoBack if the page should go back if the response is not OK
 * @param onError the function to call if an error occurs
 * @returns {Promise<any>} the data fetched
 */
async function fetchInternal(
    uri, options = {},
    body = undefined,
    token= undefined,
    isToGoBack = true,
    onError = undefined,
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
    console.log(uri, options)
    return fetch(uri, options)
        .then(response => {
            if (!isResponseOK(response)) {
                response.json().then(json => {
                    const text = json.error
                    if (isToGoBack) {
                        window.history.back()
                    }
                    if (!onError) {
                        handlerViews.showAlert(text)
                    } else {
                        onError(text)
                    }
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

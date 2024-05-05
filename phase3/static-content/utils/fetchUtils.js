/**
 * Fetches data from the server
 *
 * @param uri
 * @param token
 * @returns {Promise<*>}
 */
async function get(uri, token = undefined) {
    return fetchInternal(uri, {}, undefined, token)
}

/**
 * Deletes data from the server
 *
 * @param uri
 * @returns {Promise<*>}
 */
async function del(uri) {
    return fetchInternal(uri, {method: "DELETE"})
}

/**
 * Updates data on the server
 *
 * @param uri
 * @param body
 * @returns {Promise<*>}
 */
async function put(uri, body) {
    return fetchInternal(uri, {method: "PUT"}, body)
}

/**
 * Posts data to the server
 *
 * @param uri
 * @param body
 * @returns {Promise<*>}
 */
async function post(uri, body) {
    return fetchInternal(uri, {method: "POST"}, body)
}

/**
 * Internal fetch function
 *
 * @param uri
 * @param options
 * @param body
 * @param token
 * @returns {Promise<any>}
 */
async function fetchInternal(uri, options = {}, body = undefined, token= undefined) {
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
                response.text().then(text => {
                    window.history.back()
                    alert(`Error: ${response.status} ${response.statusText}\n${text}`)
                })
            } else {
                return response.json()
            }
        })
}

/**
 * Checks if the response is OK
 *
 * @param response
 * @returns {boolean}
 */
function isResponseOK(response) {
    return response.status >= 200 && response.status < 399
}

export const fetcher = {
    get,
    del,
    put,
    post
}

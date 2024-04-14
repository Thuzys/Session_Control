function changeHash(hash) {
    window.location.hash = hash;
}

function isResponseOK(response) {
    return response.status >= 200 && response.status < 399
}

function executeCommandWithResponse(url, responseHandler) {
    fetch(url)
        .then(response => {
            if(isResponseOK(response)) {
                responseHandler(response);
            } else {
                window.history.back()
                response.text().then(text => alert("Error fetching data: " + text));
            }
        })
}

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
    executeCommandWithResponse,
    makeQueryString,
}

export default handlerUtils
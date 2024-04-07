function getQuery() {
    return window.location.hash.replace("#", "").split("?")[1]
}

function getPath(){
    return window.location.hash.replace("#", "").split("?")[0]
}

function getPathParams(){
    return getPath().split("/").slice(1)
}

const RequestUtils = {
    getPathParams,
    getQuery,
    getPath,
}

export default RequestUtils

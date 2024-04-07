function getQuery() {
    const list = window.location.hash.replace("#", "").split("/?")
    return list.length > 1 ? list[1] : ""
}

function getParams() {
    const list = window.location.hash.replace("#", "").split("?")
    return list.length > 1 ? list[1] : ""
}

function getPath(){
    return window.location.hash.replace("#", "").split("?")[0]
}

const RequestUtils = {
    getQuery,
    getPath,
    getParams
}

export default RequestUtils

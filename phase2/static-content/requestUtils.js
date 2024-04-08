function getQuery() {
    const retMap = new Map();
    const list = window.location.hash
        .replace("#", "")
        .split("?")[1]
        .split("&")
    list.forEach(pair => {
        const [key, value] = pair.split("=")
        retMap.set(key, value)
    })
    return retMap
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

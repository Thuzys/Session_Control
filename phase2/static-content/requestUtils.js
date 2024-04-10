function getParams() {
    const list = window.location.hash.replace("#", "").split("/")
    return list.length > 1 ? list[1] : ""
}

function getQuery() {
    const retMap = new Map();
    const list = window.location.hash
        .replace("#", "")
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

function getPath(){
    return window.location.hash.replace("#", "").split("?")[0]
}

const RequestUtils = {
    getParams,
    getPath,
    getQuery
}

export default RequestUtils

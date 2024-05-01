function changeHash(hash) {
    window.location.hash = hash;
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
    makeQueryString,
}

export default handlerUtils
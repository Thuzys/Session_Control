import views from "../../views/viewsCreators.js";
import handlerViews from "../../views/handlerViews/handlerViews.js";
function changeHash(hash) {
    window.location.hash = hash;
}

const updateButtonState = (searchButton, inputDev, inputGenres) => {
    searchButton.disabled = !inputDev.value.trim() && !inputGenres.value.trim()
}

function updateGameSearchButton(searchButton, inputDev, inputGenres) {
    const update = () => updateButtonState(searchButton, inputDev, inputGenres)
    inputDev.addEventListener("input", update)
    inputGenres.addEventListener("input", update)
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

                response.text().then(text => alert("Error fetching data: " + text));
            }
        })
}

function makeQueryString(query) {
    const queryString = new URLSearchParams();
    for (const [key, value] of query) {
        queryString.set(key, value);
    }
    return queryString;
}

const handlerUtils = {
    changeHash,
    updateGameSearchButton,
    executeCommandWithResponse,
    makeQueryString,
}

export default handlerUtils
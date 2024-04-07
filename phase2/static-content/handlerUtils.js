import views from "./viewsCreators.js";

function isOkResponse(response) {
  return response.status >= 200 && response.status < 399
}

const handlerUtils = {
    isOkResponse,
}

export default handlerUtils
import views from "../viewsCreators.js";

/**
 * Create a toggle switch for light mode and dark mode
 * @returns {HTMLLabelElement}
 */
function createBB8Toggle() {
    const input = views.input({type: "checkbox", class: "bb8-toggle__checkbox"});
    const container = views.div({class: "bb8-toggle__container"});
    const scenery = views.div({class: "bb8-toggle__scenery"});
    const bb8 = views.div({class: "bb8"});
    const headContainer = views.div({class: "bb8__head-container"});
    const body = views.div({class: "bb8__body"});
    const artificialHidden = views.div({class: "artificial__hidden"});
    const shadow = views.div({class: "bb8__shadow"});

    for(let i = 0; i < 7; i++) {
        scenery.appendChild(views.div({class: "bb8-toggle__star"}));
    }
    ["tatto-1", "tatto-2", "gomrassen", "hermes", "chenini"].forEach(classname => {
        scenery.appendChild(views.div({class: classname}));
    });
    for(let i = 0; i < 3; i++) {
        scenery.appendChild(views.div({class: "bb8-toggle__cloud"}));
    }
    for(let i = 0; i < 2; i++) {
        headContainer.appendChild(views.div({class: "bb8__antenna"}));
    }
    headContainer.appendChild(views.div({class: "bb8__head"}));
    bb8.appendChild(headContainer);
    bb8.appendChild(body);
    artificialHidden.appendChild(shadow);

    container.appendChild(scenery);
    container.appendChild(bb8);
    container.appendChild(artificialHidden);

    input.addEventListener('change', function() {
        setTimeout(function() {
            toggleLightMode();
            location.reload();
        }, 275);
    });

    return views.label({class: "bb8-toggle centered"}, input, container);
}

/** Enable light mode **/
function enableLightMode() {
    const mainContent = document.getElementById('mainContent');
    mainContent.classList.add('light-mode');
    document.body.classList.add('light-mode');
    localStorage.setItem('lightMode', 'enabled');
}

/** Disable light mode **/
function disableLightMode() {
    const mainContent = document.getElementById('mainContent');
    mainContent.classList.remove('light-mode');
    document.body.classList.remove('light-mode');
    localStorage.setItem('lightMode', 'disabled');
}

/**
 * Toggle light mode and dark mode
 */
function toggleLightMode() {
    if (localStorage.getItem('lightMode') === 'enabled') {
        disableLightMode();
    } else {
        enableLightMode();
    }
}

if (localStorage.getItem('lightMode') === 'disabled') {
    disableLightMode();
} else {
    enableLightMode();
}

export default createBB8Toggle;
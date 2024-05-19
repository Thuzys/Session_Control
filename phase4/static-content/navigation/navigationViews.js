import views from "../views/viewsCreators.js";

function createNavigationIconView(href, iconCssClass, textContent) {
    return views.a({href: href, class: "w3-bar-item w3-button w3-padding-large w3-hover-black"},
        views.i({class: iconCssClass}),
        views.p({}, textContent)
    );
}

function createNavigationBarView() {
    return views.nav({class: "sidebar w3-bar-block w3-small w3-hide-small w3-center"},
            createNavigationIconView("#players/home", "fa fa-home w3-xxlarge", "Home"),
            createNavigationIconView("#gameSearch", "fa fa-gamepad w3-xxlarge", "Search Games"),
            createNavigationIconView("#sessionSearch", "fa fa-search w3-xxlarge", "Search Sessions"),
            createNavigationIconView("#playerSearch", "fa fa-users w3-xxlarge", "Search Player"),
            createNavigationIconView("#contacts", "fa fa-envelope w3-xxlarge", "Contacts"),
            createNavigationIconView("#logOut", "fa fa-sign-out w3-xxlarge", "Log Out")
    );
}

const navigationViews = {
    createNavigationBarView
}

export default navigationViews
import views from "../views/viewsCreators.js";

function createNavigationIconView(href, iconCssClass, textContent) {
    return views.a({href: href, class: "navbar-icon w3-bar-item w3-button w3-padding-large w3-hover-black"},
        views.i({class: iconCssClass}),
        views.p({class: "nav-bar-icon"}, textContent)
    );
}

function createNavigationBarView() {
    return views.nav({class: "sidebar w3-bar-block w3-small w3-hide-small w3-center", id: "navBar"},
            createNavigationIconView("#players/home", "fa fa-home w3-xxlarge nav-bar-icon", "Home"),
            createNavigationIconView("#gameSearch", "fa fa-gamepad w3-xxlarge nav-bar-icon", "Search Games"),
            createNavigationIconView("#sessionSearch", "fa fa-search w3-xxlarge nav-bar-icon", "Search Sessions"),
            createNavigationIconView("#playerSearch", "fa fa-users w3-xxlarge nav-bar-icon", "Search Player"),
            createNavigationIconView("#contacts", "fa fa-envelope w3-xxlarge nav-bar-icon", "Contacts"),
            createNavigationIconView("#logOut", "fa fa-sign-out w3-xxlarge nav-bar-icon", "Log Out")
    );
}

const navigationViews = {
    createNavigationBarView
}

export default navigationViews
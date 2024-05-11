import views from "../views/viewsCreators.js";
import contactHandlerViews from "../views/handlerViews/contactHandlerViews.js";
import menu from "../navigation/menuLinks.js";

const cardData = [
    {
        name: 'Arthur Oliveira',
        title: 'CEO & Founder',
        university: 'ISEL university',
        image: '/resources/Thuzys.jpg',
        email: 'A50543@alunos.isel.pt',
        socials: [
            {name: 'gitHub', image: '/resources/gitHub.png', link: `https://github.com/Thuzys`},
            {
                name: 'LinkedIn',
                image: '/resources/linkedIn.png',
                link: 'https://www.linkedin.com/in/arthur-cesar-oliveira-681643184'
            },
        ]
    },
    {
        name: 'Guilherme Belo',
        title: 'CEO & Founder',
        university: 'ISEL university',
        image: '/resources/Guilherme.jpg',
        email: 'A50978@alunos.isel.pt',
        socials: [
            {name: 'gitHub', image: '/resources/gitHub.png', link: `https://github.com/GuilhermeBelo2904`},
            {name: 'LinkedIn', image: '/resources/linkedIn.png'},
        ]
    },
    {
        name: 'Brian Melhorado',
        title: 'CEO & Founder',
        university: 'ISEL university',
        image: '/resources/Brian.jpg',
        email: 'brgm37@gmail.com',
        socials: [
            {name: 'gitHub', image: '/resources/gitHub.png', link: `https://github.com/Brgm37`},
            {name: 'LinkedIn', image: '/resources/linkedIn.png', link: `https://www.linkedin.com/in/brian-melhorado-449794307`},
        ]
    },
    ];

/**
 * Get contacts
 * @param mainContent
 * @param mainHeader
 */
function getContacts(mainContent, mainHeader) {
    const container = views.div({class: "contacts-container"});
    const contactsView = contactHandlerViews.createGetContactsView(cardData);
    container.replaceChildren(contactsView);
    mainContent.replaceChildren(container);
mainHeader.replaceChildren(menu.get("playerSearch"), menu.get("home"), menu.get("gameSearch"), menu.get("sessionSearch"));
}

export default {
    getContacts
}
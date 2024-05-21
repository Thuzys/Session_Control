import contactHandlerViews from "../views/handlerViews/contactHandlerViews.js";

/**
 * Container of the contact data
 */
const cardData = [
    {
        name: 'Arthur Oliveira',
        title: 'CEO & Founder',
        university: 'ISEL University',
        image: '/resources/Thuzys.jpg',
        email: 'A50543@alunos.isel.pt',
        socials: [
            {name: 'github', image: '/resources/github.png', link: `https://github.com/Thuzys`},
            {
                name: 'linkedin',
                image: '/resources/linkedin.png',
                link: 'https://www.linkedin.com/in/arthur-cesar-oliveira-681643184'
            },
        ]
    },
    {
        name: 'Guilherme Belo',
        title: 'CEO & Founder',
        university: 'ISEL University',
        image: '/resources/Guilherme.jpg',
        email: 'A50978@alunos.isel.pt',
        socials: [
            {name: 'github', image: '/resources/gitHub.png', link: `https://github.com/GuilhermeBelo2904`},
            {name: 'linkedin', image: '/resources/linkedIn.png'},
        ]
    },
    {
        name: 'Brian Melhorado',
        title: 'CEO & Founder',
        university: 'ISEL University',
        image: '/resources/Brian.jpg',
        email: 'brgm37@gmail.com',
        socials: [
            {name: 'github', image: '/resources/gitHub.png', link: `https://github.com/Brgm37`},
            {name: 'linkedin', image: '/resources/linkedIn.png', link: `https://www.linkedin.com/in/brian-melhorado-449794307`},
        ]
    },
    ];

/**
 * Handles the get contacts operation
 * @param mainContent main content of the page
 */
function getContacts(mainContent) {
    const contactsView = contactHandlerViews.createGetContactsView(cardData);
    mainContent.replaceChildren(contactsView);
}

export default {
    getContacts
}
import views from '../viewsCreators.js';

/**
 * Create card view with contact data
 * @param cardData contact data
 * @returns {*} card view
 */
function createCard(cardData) {
    const divCard = views.div({class: 'card'});

    const img = views.image({src: cardData.image, alt: cardData.name, style: 'width:100%'});
    const h2 = views.h2({}, cardData.name);
    const pTitle = views.p({class: 'title'}, cardData.title);
    const pUniversity = views.p({}, cardData.university);

    const divIcons = views.div({style: 'margin: 24px 0'});
    cardData.socials.forEach(social => {
        const a = views.a({href:social.link}, views.i({href: social.link, class: "w3-xlarge card-a fa fa-" + social.name}));
        divIcons.appendChild(a);
    });

    const pButton = views.p({});
    const button = views.button({class: "contact-button"}, 'Contact');
    button.addEventListener('click', () => {
        window.location.href = `mailto:${cardData.email}`
    });
    pButton.appendChild(button);
    divCard.replaceChildren(img, h2, pTitle, pUniversity, divIcons, pButton);

    return divCard;
}

/**
 * Create view with contacts
 * @param contacts contacts data
 * @returns {*} view with contacts
 */
function createGetContactsView(contacts) {
    const div = views.div({class: 'contacts-container'})
    contacts.forEach(contact => {
        const card = createCard(contact);
        div.appendChild(card);
    });
    return div;
}

export default {
    createGetContactsView
}
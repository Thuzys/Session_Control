import views from '../viewsCreators.js';

function createCard(cardData) {
    const divCard = views.div({class: 'card'});

    const img = views.image({src: cardData.image, alt: cardData.name, style: 'width:100%'});
    divCard.appendChild(img);

    const h1 = views.h1({}, cardData.name);
    divCard.appendChild(h1);

    const pTitle = views.p({class: 'title'}, cardData.title);
    divCard.appendChild(pTitle);

    const pUniversity = views.p({}, cardData.university);
    divCard.appendChild(pUniversity);

    const divIcons = views.div({style: 'margin: 24px 0'});
    cardData.socials.forEach(social => {
        const a = views.a({href: social.link});
        const i = views.image({class: "icon-image", src: social.image, alt: social.name, style: 'width:100%'});
        a.appendChild(i);
        divIcons.appendChild(a);
    });
    divCard.appendChild(divIcons);

    const pButton = views.p({});
    const button = views.button({class: "contact-button"}, 'Contact');
    button.addEventListener('click', () => {
        window.location.href = `mailto:${cardData.email}`
    });
    pButton.appendChild(button);
    divCard.appendChild(pButton);

    return divCard;
}


function createGetContactsView(contacts) {
    const div = views.div({class: 'contacts-container'})
    contacts.forEach(contact => {
        const card = createCard(contact);
        div.appendChild(card);
    });
    return div;
}

export default {
    createCard,
    createGetContactsView
}
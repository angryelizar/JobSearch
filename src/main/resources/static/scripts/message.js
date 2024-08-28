let messagesBlock = document.querySelector('.messages-block');
let locale = document.getElementById('locale').getAttribute('content');

function getNoMessagesText(){
    let language = locale.substring(0, 2);
    console.log(language)
    if (language === 'ky') {
        return 'Бул чатта азырынча эч кандай билдирүүлөр жок - биринчи кадам таштаңыз!'
    } else {
        return 'В этом чате пока нет сообщений - сделайте первый шаг!'
    }
}

function fetchMessages() {
    let respondedApplicantId = parseInt(document.location.pathname.split("/").pop());
    let url = window.location.origin + '/api/messages/response/' + respondedApplicantId;

    fetch(url)
        .then(response => response.json())
        .then(data => {
            messagesBlock.innerHTML = '';
            if (data.length > 0) {
                data.forEach(message => {
                    let messageCard = document.createElement('div');
                    messageCard.classList.add('card', 'w-100', 'text-bg-light', 'mb-3');
                    let cardHeader = document.createElement('div');
                    cardHeader.classList.add('card-header');
                    cardHeader.textContent = formatDate(message.dateTime);
                    messageCard.appendChild(cardHeader);
                    let cardBody = document.createElement('div');
                    cardBody.classList.add('card-body');
                    let title = document.createElement('h5');
                    title.classList.add('card-title');
                    title.textContent = message.author;
                    cardBody.appendChild(title);
                    let content = document.createElement('p');
                    content.classList.add('card-text');
                    content.textContent = message.content;
                    cardBody.appendChild(content);
                    messageCard.appendChild(cardBody);
                    messagesBlock.appendChild(messageCard);
                });
            } else {
                let noMessagesText = document.createElement('p');
                noMessagesText.classList.add('text-center', 'mb-5');
                noMessagesText.textContent = `${getNoMessagesText()}`
                messagesBlock.appendChild(noMessagesText);
            }
        })
        .catch(error => {
            console.error('Ошибка при получении сообщений:', error);
        });
}

function onLoadWindow() {
    fetchMessages();
    setInterval(fetchMessages, 30000);
}

window.addEventListener('load', onLoadWindow);

function formatDate(dateStr) {
    let date = new Date(dateStr);
    const months = ['января', 'февраля', 'марта', 'апреля', 'мая', 'июня', 'июля', 'августа', 'сентября', 'октября', 'ноября', 'декабря'];

    const hours = ('0' + date.getHours()).slice(-2);
    const minutes = ('0' + date.getMinutes()).slice(-2);
    const day = date.getDate();
    const month = months[date.getMonth()];
    const year = date.getFullYear();

    return `${hours}:${minutes}, ${day} ${month}, ${year}`;
}
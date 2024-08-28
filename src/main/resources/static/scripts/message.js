let messagesBlock = document.querySelector('.messages-block');
let stompClient = null;


function addMessage(message) {
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
}

function connectToChat() {
    // Подключаемся к нашему эндпоинту, создаем сокет
    var socket = new SockJS("/ws");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        console.log('Connected!');
        console.log('Frame:' + frame);
    });
}

window.addEventListener('load', connectToChat);

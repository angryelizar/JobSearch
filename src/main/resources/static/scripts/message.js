const SOCKET_URL = '/ws';
const TOPIC_URL = '/chat/response/';
const SEND_MESSAGE_URL = '/chat/send/response/';
let messagesBlock = document.querySelector('.messages-block');
let respondApplicantId = document.querySelector("[name='respondApplicant']").value;
let submitButton = document.querySelector('#submitButton');
let stompClient = null;

function addMessage(message) {
    let messageCard = document.createElement('div');
    messageCard.classList.add('card', 'w-100', 'text-bg-light', 'mb-3');
    let cardHeader = document.createElement('div');
    cardHeader.classList.add('card-header');
    cardHeader.textContent = message.dateTime;
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
    let messageText = document.querySelector('#messageText');
    messageText.value = '';
}

function connectToChat() {
    // Подключаемся к нашему эндпоинту, создаем сокет
    let socket = new SockJS(SOCKET_URL);
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        console.log('Connected!');
        // Подписываемся на необходимый нам "топик" (в нашем случае, это топик для определенного отклика)
        stompClient.subscribe(TOPIC_URL + respondApplicantId, function (messageOutput) {
            addMessage(JSON.parse(messageOutput.body));
        });
    });
}

function sendMessage() {
    let messageText = document.querySelector('#messageText').value;
    let fromTo = parseInt(document.querySelector("[name='messageAuthor']").value, 10);
    let toFrom = parseInt(document.querySelector("[name='messageRecipient']").value, 10);
    let id = parseInt(document.querySelector("[name='respondApplicant']").value, 10);

    const message = {
        'messageAuthor': fromTo,
        'messageRecipient': toFrom,
        'respondApplicant': id,
        'messageText': messageText
    };

    console.log(message);
    stompClient.send(SEND_MESSAGE_URL + id, {}, JSON.stringify(message));
}


window.addEventListener('load', connectToChat);


submitButton.addEventListener('click', sendMessage)

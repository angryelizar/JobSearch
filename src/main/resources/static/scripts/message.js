let messageTextInput = document.getElementById('messageText');
let submitButton = document.getElementById('submitButton');

function onChangeMessage() {
    let messageValue = messageTextInput.value;
    if (messageValue.trim().length !== 0){
        submitButton.disabled = false;
    }
}

function onLoadWindow() {
    submitButton.disabled = true;
    messageTextInput.addEventListener('input', onChangeMessage);
}

window.addEventListener('load', onLoadWindow);
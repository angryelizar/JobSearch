let modalBody = document.querySelector(".modal-body");
window.addEventListener('load', onLoad);

function onSendResponse(select, vacancy_id) {
    let selectedItem = select.value; // Получаем значение выбранной опции
    const data = {
        resumeId: selectedItem,
        vacancyId: vacancy_id
    };
    const options = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    };
    console.log(data);
    fetch('/api/vacancies/responded-applicant', options)
        .then(response => response.json())
        .then(data => {
            console.log("Успех!");
            console.log(data);
        })
        .catch(error => {
            console.error('Ошибка:', error);
        });
}

function printDataOnModal(data) {
    let header = document.createElement('h6');
    header.innerText = "Выберите резюме для отклика:";
    modalBody.append(header);
    let objectSize = Object.keys(data).length;
    let formSelect = document.createElement('select');
    formSelect.setAttribute('class', 'form-select');
    modalBody.append(formSelect);
    for (let i = 0; i < objectSize; i++) {
        let option = document.createElement('option');
        option.setAttribute('value', `${data[i].resumeId}`);
        option.innerText = data[i].resumeName;
        formSelect.append(option);
    }
    let vacancy_id = parseInt(document.location.pathname.split("/").pop());
    let button = document.getElementById('respondButton');
    button.addEventListener("click", function() { // Используем функцию обратного вызова
        onSendResponse(formSelect, vacancy_id);
    });
}

function onGetData(data) {
    let objectSize = Object.keys(data).length;
    if (objectSize > 0){
        console.log("Мы получили " + objectSize + " резюме");
        printDataOnModal(data);
    } else {
        modalBody.innerText = "У вас нет подходящего резюме для этой вакансии!";
        console.log("Мало данных");
    }
}

function onLoad() {
    let vacancy_id = parseInt(document.location.pathname.split("/").pop());
    let user_email = document.getElementById('userEmail').getAttribute('value');
    const port = document.location.port;

    const data = {
        vacancyId: vacancy_id,
        userEmail: user_email
    };
    const options = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    };

    fetch('/api/vacancies/resume', options)
        .then(response => response.json())
        .then(data => {
            console.log(data);
            onGetData(data);
        })
        .catch(error => {
            console.error('Ошибка:', error);
        });

}

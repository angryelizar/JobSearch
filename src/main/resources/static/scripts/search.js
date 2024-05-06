const searchInput = document.getElementById('search-block');
const cardWrap = document.getElementById('card-wrap');
searchInput.addEventListener('input', searchOnChange)


async function getData(response) {
    let json = await response.json();
    let objectSize = Object.keys(json).length;
    if (objectSize === 0){
        makeWarning('Таких вакансий у нас нет.....')
    } else {
        for (let i = 0; i < objectSize; i++) {
            makeCard(json[i].id, json[i].name, json[i].salary);
        }
    }
}

async function searchOnChange() {
    cardWrap.innerHTML = '';
    let query = searchInput.value;
    let response = await fetch('/api/vacancies/search?query=' + query);
    if (response.ok) {
        getData(response);
    } else {
        console.log("Ошибка HTTP: " + response.status);
    }
}

function makeCard(id, name, salary) {
    let card = document.createElement('div');
    card.classList.add('card');
    card.innerHTML = `<div class="card-body">
                            <h5 class="card-title"><a class="text-decoration-none text-dark"
                                                      href="/vacancies/${id}">${name}</a></h5>
                            <h3 class="card-title">${salary}</h3>
                            <a href="/vacancies/${id}" class="btn btn-dark">Подробнее</a>
                        </div>`
    cardWrap.append(card);
}

function makeWarning(message){
    cardWrap.innerHTML = '';
    let textElement = document.createElement('h4');
    textElement.classList.add('text-danger', 'text-center')
    textElement.innerText = message;
    cardWrap.append(textElement);
}
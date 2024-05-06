let selectInput = document.getElementById('accountType');
const EMPLOYER_VALUE = "Работодатель";
const APPLICANT_VALUE = "Соискатель";
const element = document.createElement('div')
element.classList.add('mb-3')
element.id = 'surname-input'
element.innerHTML = '<label for="surname" class="form-label">Ваша фамилия</label>\n' +
    '<input type="text" id="surname" name="surname" placeholder="Ваша фамилия" ' +
    'class="form-control">'

selectInput.addEventListener('change', onChange)
window.addEventListener('load', onLoadPage);

function onChange() {
    let surnameBlock = document.getElementById('surname-input');
    let nameLabel = document.getElementById('name-label');
    let nameBlock = document.getElementById('name-input');
    if (selectInput.value === (EMPLOYER_VALUE)){
        deleteSurname(surnameBlock, nameLabel)
    } else if (!surnameBlock && selectInput.value === APPLICANT_VALUE){
        addSurname(nameBlock, nameLabel)
    }

}

function onLoadPage(){
    let surnameBlock = document.getElementById('surname-input');
    let nameLabel = document.getElementById('name-label');
    deleteSurname(surnameBlock, nameLabel)
}

function addSurname(nameBlock, nameLabel){
    nameBlock.after(element);
    nameLabel.innerText = 'Ваше имя'
    document.getElementById('name').placeholder = 'Ваше имя'
}

function deleteSurname(surnameBlock, nameLabel){
    surnameBlock.remove();
    nameLabel.innerText = 'Название компании'
    document.getElementById('name').placeholder = 'Название компании'
}


let selectInput = document.getElementById('accountType');
let locale = document.getElementById('locale').getAttribute('content');
const EMPLOYER_VALUE = "Работодатель";
const APPLICANT_VALUE = "Соискатель";
const element = document.createElement('div')
element.classList.add('mb-3')
element.id = 'surname-input'
element.innerHTML = `<label for="surname" class="form-label">${getSurnameText()}</label> <input type="text" id="surname" name="surname" class="form-control">`

selectInput.addEventListener('change', onChange)
window.addEventListener('load', onLoadPage);


function getCompanyNameText(){
    let language = locale.substring(0, 2);
    console.log(language)
    if (language === 'ky') {
        return 'Компаниянын аталышы'
    } else {
        return 'Название компании'
    }
}

function getNameText(){
    let language = locale.substring(0, 2);
    console.log(language)
    if (language === 'ky') {
        return 'Сиздин атыңыз'
    } else {
        return 'Ваше имя'
    }
}


function getSurnameText(){
    let language = locale.substring(0, 2);
    console.log(language)
    if (language === 'ky') {
        return 'Сиздин фамилияңыз'
    } else {
        return 'Ваша фамилия'
    }
}

function deleteAge(ageBlock) {
    ageBlock.remove();
}

function onChange() {
    let surnameBlock = document.getElementById('surname-input');
    let nameLabel = document.getElementById('name-label');
    let nameBlock = document.getElementById('name-input');
    let ageBlock = document.getElementById('age-input');
    if (selectInput.value === (EMPLOYER_VALUE)){
        deleteSurname(surnameBlock, nameLabel)
        deleteAge(ageBlock)
    } else if (!surnameBlock && selectInput.value === APPLICANT_VALUE){
        addSurname(nameBlock, nameLabel)
        addAge(ageBlock, nameBlock)
    }

}

function onLoadPage(){
    let surnameBlock = document.getElementById('surname-input');
    let nameLabel = document.getElementById('name-label');
    deleteSurname(surnameBlock, nameLabel)
}

function addSurname(nameBlock, nameLabel){
    nameBlock.after(element);
    nameLabel.innerText = `${getNameText()}`
    document.getElementById('surname').value = ''
}

function deleteSurname(surnameBlock, nameLabel){
    surnameBlock.remove();
    nameLabel.innerText = `${getCompanyNameText()}`
}


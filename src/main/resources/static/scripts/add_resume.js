let workExperienceCounter = 0;
let educationInfoCounter = 0;
let locale = document.getElementById('locale').getAttribute('content');

function deleteEducationOnClick() {
    event.preventDefault();
    let module = event.target.parentNode.parentNode;
    module.remove();
}

function deleteEducationOnClickElement(element) {
    event.preventDefault();
    let module = element.target.parentNode.parentNode;
    module.remove();
}

function addEducationOnClick() {
    event.preventDefault();
    educationInfoCounter = educationInfoCounter + 1;
    let index = educationInfoCounter;
    let educationInfoItem = `<div class="education-module-buttons d-flex justify-content-center gap-3">
        <button class="education-delete-button mt-2 fs-6 rounded-4 btn btn-danger text-decoration-none">${getDeleteText()}</button>
    </div>
    <label for="EducationInfos[${index}].degree" class="form-label">${getEducationText()}</label>
    <input type="text" class="form-control" id="EducationInfos[${index}].degree"
           name="EducationInfos[${index}].degree">
        <label for="EducationInfos[${index}].program" class="form-label">${getProgramText()}</label>
        <input type="text" class="form-control" id="EducationInfos[${index}].program"
               name="EducationInfos[${index}].program">
            <label for="EducationInfos[${index}].institution" class="form-label">${getInstitutionText()}</label>
            <input type="text" class="form-control" id="EducationInfos[${index}].institution"
                   name="EducationInfos[${index}].institution">
                <div class="d-flex flex-column justify-content-start mt-3">
                    <div class="experience-item d-flex align-items-center">
                        <label for="EducationInfos[${index}].startDate" class="form-text form-label me-2">${getStartDateText()}</label>
                        <input type="date" class="form-control" id="EducationInfos[${index}].startDate"
                               name="EducationInfos[${index}].startDate">
                    </div>
                    <div class="experience-item d-flex justify-content-between align-items-center">
                        <label for="EducationInfos[${index}].endDate" class="form-text form-label me-2">${getEndDateText()}</label>
                        <input type="date" class="form-control" id="EducationInfos[${index}].endDate"
                               name="EducationInfos[${index}].endDate">
                    </div>
                </div>`;
    let module = event.target.parentNode;
    let element = document.createElement('div');
    element.classList.add('education-module-item', 'pb-3', 'mt-2', 'border-top', 'border-bottom');
    element.innerHTML = educationInfoItem;
    module.append(element);
    let deleteItemButton = document.querySelector('.education-delete-button');
    deleteItemButton.addEventListener('click', deleteEducationOnClickElement)
}

function deleteWorkOnClick() {
    event.preventDefault();
    let module = event.target.parentNode.parentNode;
    module.remove();
}

function deleteWorkOnClickElement(element) {
    event.preventDefault();
    let module = element.target.parentNode.parentNode;
    module.remove();
}

function addWorkOnClick() {
    event.preventDefault();
    workExperienceCounter = workExperienceCounter + 1;
    let index = workExperienceCounter;
    let workInfoItem = `<div class="work-module-buttons d-flex justify-content-center gap-3">
    <button  class="work-delete-button mt-2 fs-6 rounded-4 btn btn-danger text-decoration-none">${getDeleteText()}</button>
</div>
<label for="WorkExperienceInfos[${index}].years" class="form-label">${getWorkExperienceInfoText()}</label>
<input type="number" class="form-control" id="WorkExperienceInfos[${index}].years"
    name="WorkExperienceInfos[${index}].years">
<label for="WorkExperienceInfos[${index}].companyName" class="form-label">${getCompanyNameText()}</label>
<input type="text" class="form-control" id="WorkExperienceInfos[${index}].companyName"
    name="WorkExperienceInfos[${index}].companyName">
<label for="WorkExperienceInfos[${index}].position" class="form-label">${getPositionNameText()}</label>
<input type="text" class="form-control" id="WorkExperienceInfos[${index}].position"
    name="WorkExperienceInfos[${index}].position">
<label for="WorkExperienceInfos[${index}]responsibilities" class="form-label">${getResponsibilitiesText()}</label>
<input type="text" class="form-control" id="WorkExperienceInfos[${index}].responsibilities"
    name="WorkExperienceInfos[${index}].responsibilities">`;
    let module = event.target.parentNode;
    let element = document.createElement('div');
    element.classList.add('work-module-item', 'pb-3', 'mt-2', 'border-top', 'border-bottom');
    element.innerHTML = workInfoItem;
    module.append(element);
    let deleteItemButton = document.querySelector('.work-delete-button');
    deleteItemButton.addEventListener('click', deleteWorkOnClickElement)
}

function onLoad() {
    let workInfoDeleteButton = document.getElementById('work-delete-button');
    workInfoDeleteButton.onclick = deleteWorkOnClick;
    let workInfoAddButton = document.getElementById('work-add-button');
    workInfoAddButton.onclick = addWorkOnClick;
    let educationInfoDeleteButton = document.getElementById('education-delete-button');
    educationInfoDeleteButton.onclick = deleteEducationOnClick;
    let educationInfoAddButton = document.getElementById('education-add-button');
    educationInfoAddButton.onclick = addEducationOnClick;
}

function getDeleteText() {
    let language = locale.substring(0, 2);
    if (language === 'ky') {
        return 'Өчүрүү'
    } else {
        return 'Удалить'
    }
}

function getWorkExperienceInfoText() {
    let language = locale.substring(0, 2);
    if (language === 'ky') {
        return 'Мурунку иш жериндеги иш тажрыйбасы:'
    } else {
        return 'Опыт работы на предыдущем месте:'
    }
}

function getCompanyNameText() {
    let language = locale.substring(0, 2);
    if (language === 'ky') {
        return 'Компаниянын аты:'
    } else {
        return 'Название компании:'
    }
}

function getPositionNameText(){
    let language = locale.substring(0, 2);
    if (language === 'ky') {
        return 'Позиция:'
    } else {
        return 'Должность:'
    }
}

function getResponsibilitiesText(){
    let language = locale.substring(0, 2);
    console.log(language)
    if (language === 'ky') {
        return 'Милдеттер:'
    } else {
        return 'Обязанности'
    }
}

function getEducationText(){
    let language = locale.substring(0, 2);
    console.log(language)
    if (language === 'ky') {
        return 'Билим:'
    } else {
        return 'Образование'
    }
}

function getProgramText(){
    let language = locale.substring(0, 2);
    console.log(language)
    if (language === 'ky') {
        return 'Программа:'
    } else {
        return 'Специальность:'
    }
}

function getInstitutionText(){
    let language = locale.substring(0, 2);
    console.log(language)
    if (language === 'ky') {
        return 'Окуган жериңиз:'
    } else {
        return 'Где учились:'
    }
}

function getStartDateText(){
    let language = locale.substring(0, 2);
    console.log(language)
    if (language === 'ky') {
        return 'Окууну баштаган дата:'
    } else {
        return 'Начало обучения:'
    }
}

function getEndDateText(){
    let language = locale.substring(0, 2);
    console.log(language)
    if (language === 'ky') {
        return 'Окууну аяктаган дата:'
    } else {
        return 'Конец обучения:'
    }
}

window.addEventListener('load', onLoad);
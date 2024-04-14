let workExperienceCounter = 0;
let educationInfoCounter = 0;

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

function addEducationOnClick(){
    event.preventDefault();
    educationInfoCounter = educationInfoCounter + 1;
    let index = educationInfoCounter;
    let educationInfoItem = `<div class="education-module-buttons d-flex justify-content-center gap-3">
        <button class="education-delete-button mt-2 fs-6 rounded-4 btn btn-danger text-decoration-none">Удалить</button>
    </div>
    <label for="EducationInfos[${index}].degree" class="form-label">Образование:</label>
    <input type="text" class="form-control" id="EducationInfos[${index}].degree"
           name="EducationInfos[${index}].degree">
        <label for="EducationInfos[${index}].program" class="form-label">Специальность:</label>
        <input type="text" class="form-control" id="EducationInfos[${index}].program"
               name="EducationInfos[${index}].program">
            <label for="EducationInfos[${index}].institution" class="form-label">Где учились:</label>
            <input type="text" class="form-control" id="EducationInfos[${index}].institution"
                   name="EducationInfos[${index}].institution">
                <div class="d-flex flex-column justify-content-start mt-3">
                    <div class="experience-item d-flex align-items-center">
                        <label for="EducationInfos[${index}].startDate" class="form-text form-label me-2">Начало
                            обучения</label>
                        <input type="date" class="form-control" id="EducationInfos[${index}].startDate"
                               name="EducationInfos[${index}].startDate">
                    </div>
                    <div class="experience-item d-flex justify-content-between align-items-center">
                        <label for="EducationInfos[${index}].endDate" class="form-text form-label me-2">Конец
                            обучения</label>
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
    <button  class="work-delete-button mt-2 fs-6 rounded-4 btn btn-danger text-decoration-none">Удалить</button>
</div>
<label for="WorkExperienceInfos[${index}].years" class="form-label">Опыт работы на предыдущем
    месте:</label>
<input type="number" class="form-control" id="WorkExperienceInfos[${index}].years"
    name="WorkExperienceInfos[${index}].years">
<label for="WorkExperienceInfos[${index}].companyName" class="form-label">Название компании:</label>
<input type="text" class="form-control" id="WorkExperienceInfos[${index}].companyName"
    name="WorkExperienceInfos[${index}].companyName">
<label for="WorkExperienceInfos[${index}].position" class="form-label">Должность:</label>
<input type="text" class="form-control" id="WorkExperienceInfos[${index}].position"
    name="WorkExperienceInfos[${index}].position">
<label for="WorkExperienceInfos[${index}]responsibilities" class="form-label">Обязанности:</label>
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


window.addEventListener('load', onLoad);
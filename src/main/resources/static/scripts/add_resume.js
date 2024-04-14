let workExperienceCounter = 0;
let educationInfoCounter = 0;


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
<label for="WorkExperienceInfo.responsibilities" class="form-label">Обязанности:</label>
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

function deleteEducationOnClick() {
    event.preventDefault();
    let module = event.target.parentNode.parentNode;
    module.remove();
}

function onLoad() {
    let workInfoDeleteButton = document.getElementById('work-delete-button');
    workInfoDeleteButton.onclick = deleteWorkOnClick;
    let workInfoAddButton = document.getElementById('work-add-button');
    workInfoAddButton.onclick = addWorkOnClick;
    let educationInfoDeleteButton = document.getElementById('education-delete-button');
    educationInfoDeleteButton.onclick = deleteEducationOnClick;
}


window.addEventListener('load', onLoad);
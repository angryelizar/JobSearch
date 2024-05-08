<#import "../layout.ftlh" as main>
<#import "../csrf_template.ftlh" as csrf>
<@main.layout>
    <h1>Создание резюме</h1>
    <div class="row">
        <div class="col-12 col-xl-10">
            <form action="/resumes/add" method="post">
                <div class="mb-3">
                    <label for="name" class="form-label">Название резюме</label>
                    <input type="text" class="form-control" id="name" name="name" aria-describedby="nameHelp" required>
                </div>
                <div class="mb-3">
                    <label for="categoryId" class="form-label">Категория</label>
                    <select id="categoryId" class="form-select" name="categoryId">
                        <#list categories as category>
                            <option value="${category.id}">${category.name}</option>
                        </#list>
                    </select>
                </div>
                <div class="mb-3">
                    <label for="salary" class="form-label">Ожидаемый уровень оклада:</label>
                    <input type="number" class="form-control" id="salary" name="salary" required>
                </div>
                <h3 class="mb-3">О себе</h3>
                <h4 class="mb-3 mt-2">Опыт работы</h4>
                <div id="work-module" class="text-center work-module mb-3">
                    <div id="work-module-items" class="work-module-items w-100">
                        <button type="button" id="work-add-button" class="fs-6 mb-2 rounded-4 btn btn-success text-decoration-none">Добавить еще</button>
                        <div class="work-module-item pb-3 mt-2 border-top border-bottom">
                            <div class="work-module-buttons d-flex justify-content-center gap-3">
                                <button type="button" id="work-delete-button" class="mt-2 fs-6 rounded-4 btn btn-danger text-decoration-none">Удалить</button>
                            </div>
                            <label for="WorkExperienceInfo.years" class="form-label">Опыт работы на предыдущем
                                месте:</label>
                            <input type="number" class="form-control" id="WorkExperienceInfo.years"
                                   name="WorkExperienceInfos[0].years">
                            <label for="WorkExperienceInfo.companyName" class="form-label">Название компании:</label>
                            <input type="text" class="form-control" id="WorkExperienceInfo.companyName"
                                   name="WorkExperienceInfos[0].companyName">
                            <label for="WorkExperienceInfo.position" class="form-label">Должность:</label>
                            <input type="text" class="form-control" id="WorkExperienceInfo.position"
                                   name="WorkExperienceInfos[0].position">
                            <label for="WorkExperienceInfo.responsibilities" class="form-label">Обязанности:</label>
                            <input type="text" class="form-control" id="WorkExperienceInfos[0].responsibilities"
                                   name="WorkExperienceInfo.responsibilities">
                        </div>
                    </div>
                </div>
                <hr class="mt-2 mb-2">
                <h4 class="mb-3 mt-2">Образование</h4>
                <div class="education-module mb-3">
                    <div class="education-module-items text-center">
                        <button type="button" id="education-add-button" class="fs-6 rounded-4 btn btn-success text-decoration-none">Добавить еще</button>
                        <div class="education-module-item pb-3 mt-2 border-top border-bottom">
                            <div class="education-module-buttons d-flex justify-content-center gap-3">
                                <button type="button" id="education-delete-button" class="mt-2 fs-6 rounded-4 btn btn-danger text-decoration-none">Удалить</button>
                            </div>
                            <label for="EducationInfos[0].degree" class="form-label">Образование:</label>
                            <input type="text" class="form-control" id="EducationInfos[0].degree"
                                   name="EducationInfos[0].degree">
                            <label for="EducationInfos[0].program" class="form-label">Специальность:</label>
                            <input type="text" class="form-control" id="EducationInfos[0].program"
                                   name="EducationInfos[0].program">
                            <label for="EducationInfos[0].institution" class="form-label">Где учились:</label>
                            <input type="text" class="form-control" id="EducationInfos[0].institution"
                                   name="EducationInfos[0].institution">
                            <div class="d-flex flex-column justify-content-start mt-3">
                                <div class="experience-item d-flex align-items-center">
                                    <label for="EducationInfos[0].startDate" class="form-text form-label me-2">Начало
                                        обучения</label>
                                    <input type="date" class="form-control" id="EducationInfos[0].startDate"
                                           name="EducationInfos[0].startDate">
                                </div>
                                <div class="experience-item d-flex justify-content-between align-items-center">
                                    <label for="EducationInfos[0].endDate" class="form-text form-label me-2">Конец
                                        обучения</label>
                                    <input type="date" class="form-control" id="EducationInfos[0].endDate"
                                           name="EducationInfos[0].endDate">
                                </div>
                            </div>
                        </div>
                    </div>
                    <hr class="mt-2 mb-2">
                </div>
                <h4 class="mb-3">Ссылки на соц. сети</h4>
                <div class="module w-100">
                    <div class="item w-50 d-inline p-3">
                        <label for="telegram" class="m-2">
                            <input class="rounded-1"  type="text" id="telegram" name="telegram" placeholder="t.me/.....">
                            <i class="bi bi-telegram fs-5"></i>
                        </label>
                    </div>
                    <div class="item w-50 d-inline p-3">
                        <label for="whatsapp" class="m-2">
                            <input class="rounded-1"  type="text" id="whatsapp" name="whatsapp" placeholder="wa.me/.....">
                            <i class="bi bi-whatsapp fs-5"></i>
                        </label>
                    </div>
                    <div class="item w-50 d-inline p-3">
                        <label for="telephone" class="m-2">
                            <input class="rounded-1"  type="text" id="telephone" name="telephone" placeholder="+996XXXXXXXXX">
                            <i class="bi bi-telephone fs-5"></i>
                        </label>
                    </div>
                    <div class="item w-50 d-inline p-3">
                        <label for="linkedin" class="m-2">
                            <input class="rounded-1"  type="text" id="linkedin" name="linkedin" placeholder="linkedin.com/...">
                            <i class="bi bi-linkedin fs-5"></i>
                        </label>
                    </div>
                    <div class="item w-50 d-inline p-3">
                        <label for="email" class="m-2">
                            <input class="rounded-1" type="text" id="email" name="email" placeholder="sobaka@sobaka.kg">
                            <i class="bi bi-envelope fs-5"></i>
                        </label>
                    </div>
                </div>
                <div class="mt-3 mb-3 form-check">
                    <input type="checkbox" class="form-check-input" id="isActive" name="isActive">
                    <label class="form-check-label" for="isActive">Активно</label>
                </div>
                <@csrf.csrf></@csrf.csrf>
                <button type="submit" class="btn btn-secondary">Отправить</button>
            </form>
        </div>
    </div>
    <script src="../scripts/add_resume.js"></script>
</@main.layout>
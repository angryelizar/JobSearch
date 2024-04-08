<#import "../layout.ftlh" as main>
<@main.layout>
    <h1>Создание резюме</h1>
    <div class="row">
        <div class="col-12 col-xl-10">
            <form action="/resumes/add" method="post">
                <div class="mb-3">
                    <label for="name" class="form-label">Название резюме</label>
                    <input type="text" class="form-control" id="name" name="name" aria-describedby="nameHelp">
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
                <h4 class="mb-3">Опыт работы</h4>
                <div class="module mb-3 d-flex align-items-center">
                    <div class="module-item w-100 me-3">
                        <label for="WorkExperienceInfo.years" class="form-label">Опыт работы на предыдущем
                            месте:</label>
                        <input type="number" class="form-control" id="WorkExperienceInfo.years"
                               name="WorkExperienceInfo.years" required>
                        <label for="WorkExperienceInfo.companyName" class="form-label">Название компании:</label>
                        <input type="text" class="form-control" id="WorkExperienceInfo.companyName"
                               name="WorkExperienceInfo.companyName" required>
                        <label for="WorkExperienceInfo.position" class="form-label">Должность:</label>
                        <input type="text" class="form-control" id="WorkExperienceInfo.position"
                               name="WorkExperienceInfo.position" required>
                        <label for="WorkExperienceInfo.responsibilities" class="form-label">Обязанности:</label>
                        <input type="text" class="form-control" id="WorkExperienceInfo.responsibilities"
                               name="WorkExperienceInfo.responsibilities" required>
                    </div>
                    <div class="module-item">
                        <a href="#" class="rounded-4 btn btn-success text-decoration-none" style="font-size: 80px">+</a>
                    </div>
                </div>
                <h4 class="mb-3">Образование</h4>
                <div class="module mb-3 d-flex align-items-center">
                    <div class="module-item w-100 me-3">
                        <label for="EducationInfo.degree" class="form-label">Образование:</label>
                        <input type="text" class="form-control" id="EducationInfo.degree"
                               name="EducationInfo.degree" required>
                        <label for="EducationInfo.program" class="form-label">Специальность:</label>
                        <input type="text" class="form-control" id="EducationInfo.program"
                               name="EducationInfo.program" required>
                        <label for="EducationInfo.institution" class="form-label">Где учились:</label>
                        <input type="text" class="form-control" id="EducationInfo.institution"
                               name="EducationInfo.institution" required>
                        <div class="d-flex flex-column justify-content-start mt-3">
                            <div class="experience-item d-flex align-items-center me-3">
                                <label for="EducationInfo.startDate" class="form-text form-label me-2">Начало
                                    обучения</label>
                                <input type="date" class="form-control" id="EducationInfo.startDate"
                                       name="EducationInfo.startDate" required>
                            </div>
                            <div class="experience-item d-flex justify-content-between align-items-center">
                                <label for="EducationInfo.endDate" class="form-text form-label me-2">Конец
                                    обучения</label>
                                <input type="date" class="form-control" id="EducationInfo.endDate"
                                       name="EducationInfo.endDate">
                            </div>
                        </div>
                    </div>
                    <div class="module-item">
                        <a href="#" class="rounded-4 btn btn-success text-decoration-none" style="font-size: 80px">+</a>
                    </div>
                </div>
                <h4 class="mb-3">Ссылки на соц. сети</h4>
                <div class="module w-100">
                    <div class="item w-50 d-inline p-3">
                        <label for="telegram">
                            <input class="rounded-1"  type="text" id="telegram" name="telegram" placeholder="t.me/.....">
                            <i class="bi bi-telegram fs-5"></i>
                        </label>
                    </div>
                    <div class="item w-50 d-inline p-3">
                        <label for="whatsapp">
                            <input class="rounded-1"  type="text" id="whatsapp" name="whatsapp" placeholder="wa.me/.....">
                            <i class="bi bi-whatsapp fs-5"></i>
                        </label>
                    </div>
                    <div class="item w-50 d-inline p-3">
                        <label for="telephone">
                            <input class="rounded-1"  type="text" id="telephone" name="telephone" placeholder="+996XXXXXXXXX">
                            <i class="bi bi-telephone fs-5"></i>
                        </label>
                    </div>
                    <div class="item w-50 d-inline p-3">
                        <label for="linkedin">
                            <input class="rounded-1"  type="text" id="linkedin" name="linkedin" placeholder="linkedin.com/...">
                            <i class="bi bi-linkedin fs-5"></i>
                        </label>
                    </div>
                    <div class="item w-50 d-inline p-3">
                        <label for="email">
                            <input class="rounded-1" type="text" id="email" name="email" placeholder="sobaka@sobaka.kg">
                            <i class="bi bi-envelope fs-5"></i>
                        </label>
                    </div>
                </div>
                <div class="mt-3 mb-3 form-check">
                    <input type="checkbox" class="form-check-input" id="isActive" name="isActive">
                    <label class="form-check-label" for="isActive">Активно</label>
                </div>
                <button type="submit" class="btn btn-secondary">Отправить</button>
            </form>
        </div>
    </div>
</@main.layout>
<#import "../layout.ftlh" as main>
<#import "../csrf_template.ftlh" as csrf>
<@main.layout>
    <h1>Редактирование вакансии</h1>
    <div class="row">
        <div class="col-12 col-xl-10">
            <form action="/vacancies/edit" method="post">
                <div class="d-none">
                    <label for="id" class="form-label">ID</label>
                    <input type="text" class="form-control" id="id" name="id" aria-describedby="idHelp"
                           value="${vacancy.id}">
                </div>
                <div class="mb-3">
                    <label for="name" class="form-label">Название вакансии</label>
                    <input type="text" class="form-control" id="name" name="name" aria-describedby="nameHelp"
                           value="${vacancy.name}">
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
                    <label for="salary" class="form-label">Заработная плата</label>
                    <input type="number" class="form-control" id="salary" name="salary" placeholder="${vacancy.salary}"
                           value="${vacancy.salary}" required>
                </div>
                <div class="mb-3">
                    <label for="description mb-3">Описание вакансии</label>
                    <textarea class="form-control" id="description" name="description" rows="10"
                              required>${vacancy.description}</textarea>
                    <div id="descriptionHelp" class="form-text">Описание вакансии не может быть пустым.</div>
                    <div id="descriptionHelp" class="form-text">Размер описания вакансии - от 100 до 2000 символов.
                    </div>
                </div>
                <div class="mb-3">
                    <label for="experience d-block mb-3">Требуемый опыт:</label>
                    <div class="d-flex justify-content-start">
                        <div class="experience-item d-flex align-items-center me-3">
                            <label for="expFrom" class="form-label me-3">От</label>
                            <input type="number" class="form-control" id="expFrom" name="expFrom"
                                   value="${vacancy.expFrom}" required>
                        </div>
                        <div class="experience-item d-flex justify-content-between align-items-center">
                            <label for="expTo" class="form-label me-3">До</label>
                            <input type="number" class="form-control" id="expTo" name="expTo" value="${vacancy.expTo}"
                                   required>
                        </div>
                    </div>
                </div>
                <div class="mb-3 form-check">
                    <input type="checkbox" class="form-check-input" id="isActive" name="isActive">
                    <label class="form-check-label" for="isActive">Активно</label>
                </div>
                <@csrf.csrf></@csrf.csrf>
                <button type="submit" class="btn btn-secondary me-5">Отправить</button>
                <a href="/vacancies/delete?id=${vacancy.id}" class=" btn btn-danger">Удалить вакансию</a>
            </form>
        </div>
    </div>
</@main.layout>
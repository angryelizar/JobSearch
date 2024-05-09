<#import "../layout.ftlh" as main>
<#import "../csrf_template.ftlh" as csrf>
<@main.layout>
    <h1>Создание вакансии</h1>
    <div class="row">
        <div class="col-12 col-xl-10">
            <form action="/vacancies/add" method="post">
                <div class="mb-3">
                    <label for="name" class="form-label">Название вакансии</label>
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
                    <label for="salary" class="form-label">Заработная плата</label>
                    <input type="number" class="form-control" id="salary" name="salary" required>
                </div>
                <div class="mb-3">
                    <label for="description mb-3">Описание вакансии</label>
                    <textarea class="form-control" id="description" name="description" rows="10" required></textarea>
                    <div id="descriptionHelp" class="form-text">Описание вакансии не может быть пустым.</div>
                    <div id="descriptionHelp" class="form-text">Размер описания вакансии - от 100 до 2000 символов.</div>
                </div>
                <div class="mb-3">
                    <label for="experience d-block mb-3">Требуемый опыт:</label>
                    <div class="d-flex justify-content-start">
                        <div class="experience-item d-flex align-items-center me-3">
                            <label for="expFrom" class="form-label me-3">От</label>
                            <input type="number" class="form-control" id="expFrom" name="expFrom" required>
                        </div>
                        <div class="experience-item d-flex justify-content-between align-items-center">
                            <label for="expTo" class="form-label me-3">До</label>
                            <input type="number" class="form-control" id="expTo" name="expTo" required>
                        </div>
                    </div>
                </div>
                <div class="mb-3 form-check">
                    <input type="checkbox" class="form-check-input" id="isActive" name="isActive">
                    <label class="form-check-label" for="isActive">Активно</label>
                </div>
                <@csrf.csrf></@csrf.csrf>
                <button type="submit" class="btn btn-secondary">Отправить</button>
            </form>
        </div>
    </div>
</@main.layout>
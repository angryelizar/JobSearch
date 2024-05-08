<#import "../layout.ftlh" as main>
<#import "../csrf_template.ftlh" as csrf>
<@main.layout>
    <h1>Редактирование резюме</h1>
    <div class="row">
        <form action="/resumes/edit" method="post">
            <div class="d-none">
                <label for="id" class="form-label">ID</label>
                <input type="text" class="form-control" id="id" name="id" aria-describedby="idHelp" value="${resume.id}">
            </div>
            <div class="mb-3">
                <label for="name" class="form-label">Название резюме</label>
                <input type="text" class="form-control" id="name" name="name" value="${resume.name}" aria-describedby="nameHelp">
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
                <input type="number" class="form-control" id="salary" name="salary" required placeholder="${resume.salary}">
            </div>
            <div class="mt-3 mb-3 form-check">
                <input type="checkbox" class="form-check-input" id="isActive" name="isActive">
                <label class="form-check-label" for="isActive">Активно</label>
            </div>
            <@csrf.csrf></@csrf.csrf>
            <button type="submit" class="btn btn-secondary">Отправить</button>
            <a href="/resumes/delete?id=${resume.id}" class=" btn btn-danger">Удалить резюме</a>
        </form>
    </div>
</@main.layout>
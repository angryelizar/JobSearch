<#import "../layout.ftlh" as main>
<@main.layout>
    <div class="row">
        <div class="col-12 col-md-10 d-flex flex-column gap-3">
            <#list vacancies as vacancy>
                <div class="card">
                    <div class="card-header">
                        ${vacancy.category}
                    </div>
                    <div class="card-body">
                        <h5 class="card-title">${vacancy.name}</h5>
                        <h3 class="card-title">${vacancy.salary}</h3>
                        <p class="card-text">${vacancy.author}</p>
                        <p>${vacancy.updateTime}</p>
                        <a href="/vacancies/${vacancy.id}" class="btn btn-dark">Подробнее</a>
                    </div>
                </div>
            </#list>
        </div>
        <div class="col-12 col-md-2">
            <form action="/vacancies/category" method="post">
                <h6>Показать вакансии по категории:</h6>
                <div class="mb-3">
                    <label for="categoryId" class="form-label">Категория</label>
                    <select id="categoryId" class="form-select" name="categoryId">
                        <#list categories as category>
                            <option value="${category.id}">${category.name}</option>
                        </#list>
                    </select>
                </div>
                <button type="submit" class="btn btn-secondary">Отправить</button>
            </form>
        </div>
    </div>
</@main.layout>
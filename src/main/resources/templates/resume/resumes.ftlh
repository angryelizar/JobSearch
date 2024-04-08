<#import "../layout.ftlh" as main>
<#import "../pagination.ftlh" as pagination>
<@main.layout>
    <div class="row">
        <@pagination.pagination></@pagination.pagination>
        <div class="col-12 col-md-10 d-flex flex-column gap-3">
            <#list resumes.content as resume>
                <div class="card">
                    <div class="card-header">
                        ${resume.category}
                    </div>
                    <div class="card-body">
                        <h5 class="card-title">${resume.name}</h5>
                        <h3 class="card-title">${resume.salary}</h3>
                        <p class="card-text">${resume.author}</p>
                        <p>${resume.updatedDate}</p>
                        <a href="/resumes/${resume.id}" class="btn btn-dark">Подробнее</a>
                    </div>
                </div>
            </#list>
        </div>
        <div class="col-12 col-md-2">
            <form action="/resumes/category" method="post">
                <h6>Показать резюме по категории:</h6>
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
    <@pagination.pagination></@pagination.pagination>
</@main.layout>
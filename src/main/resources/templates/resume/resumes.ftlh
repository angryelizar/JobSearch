<#import "../layout.ftlh" as main>
<#import "../pagination.ftlh" as pagination>
<@main.layout>
    <div class="row">
        <@pagination.pagination url page></@pagination.pagination>
        <div class="col-12 col-md-10 d-flex flex-column gap-3">
            <#if page.content?has_content>
                <#list page.content as resume>
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
                <#else>
                <p class="text-center mt-5">На сайте пока нет активных резюме</p>
            </#if>
        </div>
        <div class="col-12 col-md-2">
            <form action="/resumes/category" method="post">
                <h6>Показать резюме по категории:</h6>
                <div class="mb-3">
                    <label for="categoryId" class="form-label">Категория</label>
                    <select id="categoryId" class="form-select" name="categoryId">
                        <#if categories?has_content>
                            <#list categories as category>
                                <option value="${category.id}">${category.name}</option>
                            </#list>
                        </#if>
                    </select>
                </div>
                <button type="submit" class="btn btn-secondary">Отправить</button>
            </form>
        </div>
    </div>
    <@pagination.pagination url page></@pagination.pagination>
</@main.layout>
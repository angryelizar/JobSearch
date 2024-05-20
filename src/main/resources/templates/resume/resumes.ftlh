<#import "../layout.ftlh" as main>
<#import "../pagination.ftlh" as pagination>
<#import "../csrf_template.ftlh" as csrf>
<#import "/spring.ftl" as spring>
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
                            <a href="/resumes/${resume.id}" class="btn btn-dark"><@spring.message 'vacancies.more'/></a>
                        </div>
                    </div>
                </#list>
                <#else>
                <p class="text-center mt-5"><@spring.message 'resumes.noResumes'/></p>
            </#if>
        </div>
        <div class="col-12 col-md-2">
            <form action="/resumes/category" method="post">
                <h6><@spring.message 'resumes.category.title'/></h6>
                <div class="mb-3">
                    <label for="categoryId" class="form-label"><@spring.message 'resumes.add.category'/></label>
                    <select id="categoryId" class="form-select" name="categoryId">
                        <#if categories?has_content>
                            <#list categories as category>
                                <option value="${category.id}">${category.name}</option>
                            </#list>
                        </#if>
                    </select>
                </div>
                <@csrf.csrf></@csrf.csrf>
                <button type="submit" class="btn btn-secondary"><@spring.message 'vacancies.add.submit'/></button>
            </form>
        </div>
    </div>
    <@pagination.pagination url page></@pagination.pagination>
</@main.layout>
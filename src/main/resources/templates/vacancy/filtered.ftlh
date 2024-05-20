<#import "../layout.ftlh" as main>
<#import "../pagination_for_filter.ftlh" as pagination>
<#import "../csrf_template.ftlh" as csrf>
<#import '/spring.ftl' as spring>
<@main.layout>
    <div class="row">
        <@pagination.pagination url categoryId criterion order page></@pagination.pagination>
        <div class="col-12 col-md-10 d-flex flex-column gap-3">
            <#if page.content?has_content>
                <#list page.content as vacancy>
                    <div class="card">
                        <div class="card-header">
                            ${vacancy.category}
                        </div>
                        <div class="card-body">
                            <h5 class="card-title"><a class="text-decoration-none text-dark"
                                                      href="vacancies/${vacancy.id}">${vacancy.name}</a></h5>
                            <h3 class="card-title">${vacancy.salary}</h3>
                            <p class="card-text">${vacancy.author}</p>
                            <p>${vacancy.updateTime}</p>
                            <a href="/vacancies/${vacancy.id}" class="btn btn-dark"><@spring.message 'vacancies.more'/></a>
                        </div>
                    </div>
                </#list>
            <#else>
                <p class="text-center mt-5"><@spring.message 'vacancies.empty'/></p>
            </#if>
        </div>
        <div class="col-12 col-md-2">
            <form action="/vacancies/filter" method="POST">
                <h6><@spring.message 'vacancies.filtered.category'/></h6>
                <div class="mb-3">
                    <label for="categoryId" class="form-label"><@spring.message 'vacancies.add.category'/></label>
                    <select id="categoryId" class="form-select" name="categoryId">
                        <option value="0" selected><@spring.message 'vacancies.filtered.all'/></option>
                        <#if categories?has_content>
                            <#list categories as category>
                                <option value="${category.id}">${category.name}</option>
                            </#list>
                        </#if>
                    </select>
                </div>
                <h6><@spring.message 'vacancies.filtered.sort'/></h6>
                <div class="mb-3">
                    <label for="criterion" class="form-label"><@spring.message 'vacancies.filtered.sort.criterion'/></label>
                    <select id="criterion" class="form-select" name="criterion">
                        <option value="createdDate"><@spring.message 'vacancies.filtered.sort.criterion.createdDate'/></option>
                        <option value="responseCount"><@spring.message 'vacancies.filtered.sort.criterion.responseCount'/></option>
                        <option value="salary"><@spring.message 'vacancies.filtered.sort.criterion.salary'/></option>
                    </select>
                    <label for="order" class="form-label"><@spring.message 'vacancies.filtered.sort.order'/></label>
                    <select id="order" class="form-select" name="order">
                        <option value="increase"><@spring.message 'vacancies.filtered.sort.increase'/></option>
                        <option value="decrease"><@spring.message 'vacancies.filtered.sort.decrease'/></option>
                    </select>
                </div>
                <@csrf.csrf></@csrf.csrf>
                <button type="submit" class="btn btn-secondary"><@spring.message 'vacancies.add.submit'/></button>
            </form>
        </div>
    </div>
    <@pagination.pagination url categoryId criterion order page></@pagination.pagination>
</@main.layout>
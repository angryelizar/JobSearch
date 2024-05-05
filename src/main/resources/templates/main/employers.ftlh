<#import "../layout.ftlh" as main>
<#import "../pagination.ftlh" as pagination>
<@main.layout>
    <#if employers?has_content>
        <@pagination.pagination url employers></@pagination.pagination>
    </#if>
    <#if employers?has_content>
        <#list employers.content as employer>
            <div class="card mt-3">
                <div class="card-body">
                    <h5 class="card-title">${employer.name}</h5>
                    <a href="/employer/${employer.id}" class="btn btn-secondary">Открытые вакансии</a>
                </div>
            </div>
        </#list>
        <#else>
        <p class="text-center mt-5">На сайте пока нет компаний - со временем это пройдет!</p>
    </#if>
</@main.layout>
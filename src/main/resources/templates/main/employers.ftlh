<#import "../layout.ftlh" as main>
<@main.layout>
    <#list employers as employer>
        <div class="card mt-3">
            <div class="card-body">
                <h5 class="card-title">${employer.name}</h5>
                <a href="/employer/${employer.id}" class="btn btn-secondary">Открытые вакансии</a>
            </div>
        </div>
    </#list>
</@main.layout>
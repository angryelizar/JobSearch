<#import "../layout.ftlh" as main>
<@main.layout>
    <#list employers as employer>
        <div class="card">
            <div class="card-body">
                <h5 class="card-title">${employer.name}</h5>
                <a href="#" class="btn btn-secondary">Открытые вакансии</a>
            </div>
        </div>
    </#list>
</@main.layout>
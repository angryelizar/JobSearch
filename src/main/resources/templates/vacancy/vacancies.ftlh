<#import "../layout.ftlh" as main>
<@main.layout>
    <#list vacancies as vacancy>
        <div class="card mt-1 mb-1">
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
</@main.layout>
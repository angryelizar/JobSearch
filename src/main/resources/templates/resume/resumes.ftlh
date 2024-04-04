<#import "../layout.ftlh" as main>
<@main.layout>
    <#list resumes as resume>
        <div class="card mt-1 mb-1">
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
</@main.layout>
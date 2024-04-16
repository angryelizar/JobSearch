<#import "../layout.ftlh" as main>
<@main.layout>
    <#list contacts as contact>
        <div class="card d-block mb-3">
            <div class="card-body">
                <h5 class="card-title">${contact.name}</h5>
                <h4>${contact.vacancyName}</h4>
                <a href="message/response/${contact.respondedApplicantId}" class="btn btn-primary">Перейти в чат</a>
            </div>
        </div>
    </#list>
</@main.layout>
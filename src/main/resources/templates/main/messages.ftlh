<#import "../layout.ftlh" as main>
<@main.layout>
    <#if contacts?has_content>
        <#list contacts as contact>
            <div class="card d-block mb-3">
                <div class="card-body">
                    <h5 class="card-title">${contact.name}</h5>
                    <h4>${contact.vacancyName}</h4>
                    <a href="message/response/${contact.respondedApplicantId}" class="btn btn-primary">Перейти в чат</a>
                </div>
            </div>
        </#list>
        <#else>
        <p class="text-center mt-5">У вас пока нет диалогов :( </p>
    </#if>
</@main.layout>
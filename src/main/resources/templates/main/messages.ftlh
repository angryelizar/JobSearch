<#import "../layout.ftlh" as main>
<#import "/spring.ftl" as spring>
<@main.layout>
    <#if contacts?has_content>
        <#list contacts as contact>
            <div class="card d-block mb-3">
                <div class="card-body">
                    <h5 class="card-title">${contact.name}</h5>
                    <h4>${contact.vacancyName}</h4>
                    <a href="message/response/${contact.respondedApplicantId}" class="btn btn-primary"><@spring.message 'messages.goToChat'/></a>
                </div>
            </div>
        </#list>
        <#else>
        <p class="text-center mt-5"><@spring.message 'messages.noChats'/></p>
    </#if>
</@main.layout>
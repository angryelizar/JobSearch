<#import "../layout.ftlh" as main>
<#import "/spring.ftl" as spring>
<@main.layout>
    <h1><@spring.message 'applicant.responses.title'/></h1>
    <#if responses?has_content>
        <table class="table">
            <thead>
            <tr>
                <th scope="col">#</th>
                <th scope="col"><@spring.message 'layout.resumes'/></th>
                <th scope="col"><@spring.message 'applicant.responses.vacancy'/></th>
                <th scope="col"><@spring.message 'applicant.responses.answer'/></th>
            </tr>
            </thead>
            <tbody>
            <#list responses as response>
                <tr>
                    <th scope="row">${response?counter}</th>
                    <td><a class="text-decoration-none" href="/resumes/${response.resumedId}">${response.resumeName}</a></td>
                    <td><a class="text-decoration-none" href="/vacancies/${response.vacancyId}">${response.vacancyName}</a></td>
                    <td><@spring.message '${response.status}'/></td>
                </tr>
            </#list>
            </tbody>
        </table>
        <#else>
        <p class="text-center mt-5"><@spring.message 'applicant.responses.noContent'/></p>
    </#if>
</@main.layout>
<#import "../layout.ftlh" as main>
<#import "/spring.ftl" as spring>
<@main.layout>
    <#if isByVacancy??>
        <#if isByVacancy == true>
            <h1><a href="/vacancies/${vacancy.id}" class="text-decoration-none text-dark">${vacancy.name}</a></h1>
        </#if>
    <#else>
        <h1><@spring.message 'employer.responses.title'/></h1>
    </#if>
    <#if responses?has_content>
        <table class="table">
            <thead>
            <tr>
                <th scope="col">#</th>
                <th scope="col"><@spring.message 'applicant.responses.vacancy'/></th>
                <th scope="col"><@spring.message 'layout.resumes'/></th>
                <th scope="col"><@spring.message 'employer.responses.name'/></th>
                <th scope="col"><@spring.message 'employer.responses.status'/></th>
                <th scope="col">#</th>
                <th scope="col">#</th>
                <th scope="col">#</th>
            </tr>
            </thead>
            <tbody>
            <#list responses as response>
                <tr>
                    <th scope="row">${response?counter}</th>
                    <td><a class="text-decoration-none"
                           href="/vacancies/${response.vacancyId}">${response.vacancyName}</a></td>
                    <td><a class="text-decoration-none" href="/resumes/${response.resumeId}">${response.resumeName}</a>
                    </td>
                    <td><a class="text-decoration-none"
                           href="/applicant/${response.applicantId}">${response.applicantName}</a></td>
                    <td><@spring.message '${response.status}'/></td>
                    <td><a class="text-decoration-none"
                           href="/applicant/accept?resume=${response.resumeId}&vacancy=${response.vacancyId}"><i
                                    class="bi bi-check-lg"></i></a></td>
                    <td><a class="text-decoration-none"
                           href="/applicant/deny?resume=${response.resumeId}&vacancy=${response.vacancyId}"><i
                                    class="bi bi-x-lg"></i></a></td>
                    <td><a class="text-decoration-none" href="/messages"><i class="bi bi-envelope"></i></a></td>
                </tr>
            </#list>
            </tbody>
        </table>
    <#else>
        <p class="text-center mt-5"><@spring.message 'employer.responses.noContent'/></p>
    </#if>
</@main.layout>
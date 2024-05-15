<#import "../layout.ftlh" as main>
<@main.layout>
        <h1>Отклики на ваши вакансии</h1>
    </#if>
    <#if responses?has_content>
        <table class="table">
            <thead>
            <tr>
                <th scope="col">#</th>
                <th scope="col">Вакансия</th>
                <th scope="col">Резюме</th>
                <th scope="col">ФИО</th>
                <th scope="col">Статус</th>
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
                    <td>${response.status}</td>
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
        <p class="text-center mt-5">На ваши вакансии пока никто не откликался :(</p>
    </#if>
</@main.layout>
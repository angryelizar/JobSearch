<#import "../layout.ftlh" as main>
<@main.layout>
    <h1>Отклики на ваше резюме</h1>
    <table class="table">
        <thead>
        <tr>
            <th scope="col">#</th>
            <th scope="col">Резюме</th>
            <th scope="col">Вакансия</th>
            <th scope="col">Ответ</th>
        </tr>
        </thead>
        <tbody>
        <#list responses as response>
            <tr>
                <th scope="row">${response?counter}</th>
                <td><a class="text-decoration-none" href="/resumes/${response.resumedId}">${response.resumeName}</a></td>
                <td><a class="text-decoration-none" href="/vacancies/${response.vacancyId}">${response.vacancyName}</a></td>
                <td>${response.status}</td>
            </tr>
        </#list>
        </tbody>
    </table>
</@main.layout>
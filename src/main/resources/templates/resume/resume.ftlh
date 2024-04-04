<#import "../layout.ftlh" as main>
<@main.layout>
    <div class="resume-info mt-2 mb-2">
        <h1>${resume.name}</h1>
        <h2>${resume.category}</h2>
        <h3>${resume.salary}</h3>
        <h4>${resume.updatedDate}</h4>
    </div>
    <h5 class="mt-5 mb-2">Образование</h5>
    <#list educationInfo as education>
        <div class="education-info">
            <h6>${education.name}</h6>
            <p>${education.dates}</p>
            <p><b>${education.program}</b></p>
            <p>${education.degree}</p>
        </div>
    </#list>
    <h5 class="mt-2 mb-2">Карьера</h5>
    <#list workExperience as work>
        <div class="work-experience-info">
            <h6>${work.name}</h6>
            <p>Опыт работы в годах - ${work.years}</p>
            <p><b>${work.position}</b></p>
            <p>${work.responsibilites}</p>
        </div>
    </#list>
    <h5 class="mt-2 mb-2">Контакты</h5>
    <#list contactInfo as contact>
        <div class="work-experience-info">
            <h6>${contact.type}</h6>
            <p>${contact.content}</p>
        </div>
    </#list>
</@main.layout>
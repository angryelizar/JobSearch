<#import "../layout.ftlh" as main>
<#import "../pagination.ftlh" as pagination>
<#import "/spring.ftl" as spring>
<@main.layout>
    <div class="p-2">
        <h1 class="text-center"><@spring.message 'employer.title'/></h1>
        <div class="profile-block mt-2">
            <div class="row d-flex align-items-center">
                <div class="col-xxl-2 col-xl-2 col-lg-1 col-md-1">
                    <img src="/api/users/${employerId}/avatar" alt="${employer.name}" class="rounded-4 img-fluid">
                </div>
                <div class="col-xxl-6 col-xl-4 col-lg-5 col-md-11">
                    <h4 class="d-block">${employer.name}</h4>
                </div>
            </div>
        </div>
        <div class="posts-block container-fluid">
            <h3 class="text-start mt-3"><@spring.message 'layout.vacancies'/></h3>
            <#if vacancies?has_content>
                <@pagination.pagination url vacancies></@pagination.pagination>
            </#if>
            <#if vacancies?has_content>
                <div class="row row-cols-1 row-cols-md-2 row-cols-lg-3 mt-3 g-3">
                    <#list vacancies.content as vacancy>
                        <div class="col">
                            <div class="card" style="min-height: 255px;">
                                <div class="card-body">
                                    <h5 class="card-title"><a class="text-decoration-none"
                                                              href="/vacancies/${vacancy.id}">${vacancy.name}</a></h5>
                                    <h6 class="card-subtitle mb-2 text-body-secondary"><@spring.message 'applicant.salary'/> ${vacancy.salary}</h6>
                                    <p class="card-text"><@spring.message 'applicant.updated'/> ${vacancy.updateDate}</p>
                                </div>
                            </div>
                        </div>
                    </#list>
                </div>
            <#else>
                <p class="text-center">${employer.name} <@spring.message 'employer.noVacancies'/></p>
            </#if>
        </div>
    </div>
</@main.layout>
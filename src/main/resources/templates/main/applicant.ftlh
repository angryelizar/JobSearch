<#import "../layout.ftlh" as main>
<#import "../pagination.ftlh" as pagination>
<#import "/spring.ftl" as spring>
<@main.layout>
    <div class="p-2">
        <h1 class="text-center"><@spring.message 'resumes.resume.applicantProfile'/></h1>
        <div class="profile-block mt-2">
            <div class="row d-flex align-items-center">
                <div class="col-xxl-2 col-xl-2 col-lg-1 col-md-1">
                    <img src="/api/users/${applicantId}/avatar" alt="${applicant.name}" class="rounded-4 img-fluid">
                </div>
                <div class="col-xxl-6 col-xl-4 col-lg-5 col-md-11">
                    <h4 class="d-block">${applicant.name} <#if applicant.surname??>${applicant.surname}</#if></h4>
                    <#if applicant.surname??><p class="d-block"><@spring.message 'applicant.age'/> ${applicant.age}</p></#if>
                </div>
            </div>
        </div>
        <div class="posts-block container-fluid">
            <h3 class="text-start mt-3"><@spring.message 'layout.resumes'/></h3>
            <#if resumes?has_content>
                <@pagination.pagination url resumes></@pagination.pagination>
            </#if>
            <div class="row row-cols-1 row-cols-md-2 row-cols-lg-3 mt-3 g-3">
                <#if resumes?has_content>
                    <#list resumes.content as resume>
                        <div class="col">
                            <div class="card" style="min-height: 255px;">
                                <div class="card-body">
                                    <h5 class="card-title"><a class="text-decoration-none"
                                                              href="/resumes/${resume.id}">${resume.name}</a></h5>
                                    <h6 class="card-subtitle mb-2 text-body-secondary"><@spring.message 'applicant.updated'/> ${resume.salary}</h6>
                                    <p class="card-text"><@spring.message 'applicant.updated'/> ${resume.updateDate}</p>
                                </div>
                            </div>
                        </div>
                    </#list>
                <#else>
                    <p><@spring.message 'applicant.noResumes'/></p>
                </#if>
            </div>
        </div>
    </div>
</@main.layout>
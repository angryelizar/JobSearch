<#import "../layout.ftlh" as main>
<#import "/spring.ftl" as spring>
<@main.layout>
   <#if isPermitted = true>
       <div class="row">
           <div class="col-12 col-md-10">
               <div class="resume-info mt-2 mb-2">
                   <h1>${resume.name}</h1>
                   <h2>${resume.category}</h2>
                   <h3>${resume.salary}</h3>
                   <h4>${resume.updatedDate}</h4>
               </div>
               <h5 class="mt-5 mb-2"><@spring.message 'resumes.add.education'/></h5>
               <#if educationInfo?has_content>
                   <#list educationInfo as education>
                       <div class="education-info">
                           <h6>${education.name}</h6>
                           <p>${education.dates}</p>
                           <p><b>${education.program}</b></p>
                           <p>${education.degree}</p>
                       </div>
                   </#list>
               <#else>
                   <p class="text-center mt-5"><@spring.message 'resumes.resume.noEducation'/></p>
               </#if>
               <h5 class="mt-2 mb-2"><@spring.message 'resumes.resume.workExperience'/></h5>
               <#if workExperience?has_content>
                   <#list workExperience as work>
                       <div class="work-experience-info">
                           <h6>${work.name}</h6>
                           <p><@spring.message 'resumes.resume.workExperienceYears'/> ${work.years}</p>
                           <p><b>${work.position}</b></p>
                           <p>${work.responsibilites}</p>
                       </div>
                   </#list>
               <#else>
                   <p class="text-center mt-5"><@spring.message 'resumes.resume.noWorkExperienceYears'/></p>
               </#if>
               <h5 class="mt-2 mb-2"><@spring.message 'resumes.resume.contacts'/></h5>
               <#if contactInfo?has_content>
                   <#list contactInfo as contact>
                       <div class="work-experience-info">
                           <h6>${contact.type}</h6>
                           <p>${contact.content}</p>
                       </div>
                   </#list>
               <#else>
                   <p class="text-center mt-5"><@spring.message 'resumes.resume.noContacts'/></p>
               </#if>
           </div>
           <div class="col-12 col-md-2 me-auto ms-auto">
               <div class="applicant-info mt-2">
                   <h5><@spring.message 'resumes.resume.aboutApplicant'/></h5>
                   <div class="d-flex justify-content-between align-items-center">
                       <div><img class="rounded-3 p-1" src="/api/users/${applicant.id}/avatar" alt="${applicant.name} ${applicant.surname}"
                                 style="max-width: 80px"></div>
                       <div><h4 class="p-1">${applicant.name} ${applicant.surname}</h4></div>
                   </div>
                   <#if applicant.activeResumes gt 0>
                       <span class="d-block"><@spring.message 'resumes.resume.countOfResumes'/> ${applicant.activeResumes}</span>
                   <#else>
                       <span class="d-block"><@spring.message 'resumes.resume.noCountOfResumes'/></span>
                   </#if>
                   <a href="/applicant/${applicant.id}" class="text-decoration-none text-center"><@spring.message 'resumes.resume.applicantProfile'/></a>
               </div>
           </div>
       </div>
       <#else>
       <p class="text-center mt-5 text-danger"><@spring.message 'resumes.notPermitted'/></p>
   </#if>
</@main.layout>
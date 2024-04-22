<#import "../layout.ftlh" as main>
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
               <h5 class="mt-5 mb-2">Образование</h5>
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
                   <p class="text-center mt-5">Соискатель не оставил информации о своем образовании</p>
               </#if>
               <h5 class="mt-2 mb-2">Карьера</h5>
               <#if workExperience?has_content>
                   <#list workExperience as work>
                       <div class="work-experience-info">
                           <h6>${work.name}</h6>
                           <p>Опыт работы в годах - ${work.years}</p>
                           <p><b>${work.position}</b></p>
                           <p>${work.responsibilites}</p>
                       </div>
                   </#list>
               <#else>
                   <p class="text-center mt-5">Соискатель не оставил информации о своем предыдущем опыте работы</p>
               </#if>
               <h5 class="mt-2 mb-2">Контакты</h5>
               <#if contactInfo?has_content>
                   <#list contactInfo as contact>
                       <div class="work-experience-info">
                           <h6>${contact.type}</h6>
                           <p>${contact.content}</p>
                       </div>
                   </#list>
               <#else>
                   <p class="text-center mt-5">Соискатель не оставил контактных данных</p>
               </#if>
           </div>
           <div class="col-12 col-md-2 me-auto ms-auto">
               <div class="applicant-info mt-2">
                   <h5>О соискателе:</h5>
                   <div class="d-flex justify-content-between align-items-center">
                       <div><img class="rounded-3 p-1" src="/api/users/${applicant.id}/avatar" alt="${applicant.name} ${applicant.surname}"
                                 style="max-width: 80px"></div>
                       <div><h4 class="p-1">${applicant.name} ${applicant.surname}</h4></div>
                   </div>
                   <#if applicant.activeResumes gt 0>
                       <span class="d-block">У соискателя сейчас активно ${applicant.activeResumes} резюме</span>
                   <#else>
                       <span class="d-block">У соискателя сейчас нет открытых резюме</span>
                   </#if>
                   <a href="/applicant/${applicant.id}" class="text-decoration-none text-center">Профиль соискателя</a>
               </div>
           </div>
       </div>
       <#else>
       <p class="text-center mt-5 text-danger">Вы пытаетесь посмотреть чужое резюме не являясь работодателем - так нельзя!</p>
   </#if>
</@main.layout>
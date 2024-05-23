<#import "../layout.ftlh" as main>
<#import "../pagination.ftlh" as pagination>
<#import "../csrf_template.ftlh" as csrf>
<#import "/spring.ftl" as spring>
<@main.layout>
    <div class="p-2">
        <h1 class="text-center"><@spring.message 'profile.title' /></h1>
        <div class="profile-block mt-2">
            <div class="row d-flex align-items-center">
                <div class="col-xxl-2 col-xl-2 col-lg-1 col-md-1">
                    <img src="/api/users/${userId}/avatar" alt="${data.user.name}" class="rounded-4 img-fluid">
                </div>
                <div class="col-xxl-6 col-xl-4 col-lg-5 col-md-11">
                    <h4 class="d-block">${data.user.name} <#if data.user.surname??>${data.user.surname}</#if></h4>
                    <#if isEmployer == false><p class="d-block"><@spring.message 'profile.age' /> ${data.user.age}</p></#if>
                    <button type="button" class="p-2 btn btn-secondary d-block" data-bs-toggle="modal"
                            data-bs-target="#exampleModal">
                        <@spring.message 'profile.edit' />
                    </button>
                </div>
                <div class="col-xxl-2 col-xl-3 col-lg-3 col-md-12 mt-3">
                    <#if data.user.accountType = "Соискатель">
                        <a href="/resumes/add"
                           class="text-center d-block p-3 bg-danger rounded-4 text-decoration-none text-light"
                           style="height: 40%"><@spring.message 'profile.resume.add' /></a>
                    <#else>
                        <a href="/vacancies/add"
                           class="text-center d-block p-3 bg-danger rounded-4 text-decoration-none text-light"
                           style="height: 40%"><@spring.message 'profile.vacancy.add' /></a>
                    </#if>
                </div>
                <div class="col-xxl-2 col-xl-3 col-lg-3 col-md-12 mt-3">
                    <#if data.user.accountType = "Соискатель">
                        <a href="/applicant/responses"
                           class="text-center btn position-relative d-block p-3 border border-2 rounded-4 text-decoration-none text-black"
                           style="height: 40%"><#if approvedNumber gt 0><span
                                    class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
                                ${approvedNumber}
                                <span class="visually-hidden"><@spring.message 'profile.responsesCount' /></span>
                                </span><#else></#if> <@spring.message 'profile.responses' /></a>
                    <#else>
                        <a href="/employer/responses"
                           class="text-center d-block p-3 border border-2 rounded-4 text-decoration-none text-black"
                           style="height: 40%"><@spring.message 'profile.responses' /></a>
                    </#if>
                </div>
            </div>
        </div>
        <div class="posts-block container-fluid">
            <h3 class="text-start mt-3"><#if data.user.accountType = "Соискатель"><@spring.message 'layout.resumes' /><#else><@spring.message 'layout.vacancies' /></#if>:</h3>
            <#if data.user.accountType = "Соискатель">
                <#if data.resumes?has_content>
                    <@pagination.pagination url data.resumes></@pagination.pagination>
                </#if>
            <#else>
                <#if data.vacancies?has_content>
                    <@pagination.pagination url data.vacancies></@pagination.pagination>
                </#if>
            </#if>
            <div class="row row-cols-1 row-cols-md-2 row-cols-lg-3 mt-3 g-3">
                <#if data.user.accountType = "Соискатель">
                    <#if data.resumes?has_content>
                        <#list data.resumes.content as resume>
                            <div class="col">
                                <div class="card" style="min-height: 255px;">
                                    <div class="card-body">
                                        <h5 class="card-title"><a class="text-decoration-none"
                                                                  href="resumes/${resume.id}">${resume.name}</a></h5>
                                        <h6 class="card-subtitle mb-2 text-body-secondary"><@spring.message 'applicant.salary' /> ${resume.salary}</h6>
                                        <p class="card-text"><@spring.message 'applicant.updated' /> ${resume.updateDate}</p>
                                    </div>
                                    <div class="card-footer bg-transparent border-top-0">
                                        <a href="resumes/update?id=${resume.id}"
                                           class="text-light card-link btn btn-secondary"
                                           style="width: 40%; font-size: 10px"><@spring.message 'profile.post.update' /></a>
                                        <a href="/resumes/edit/${resume.id}" class="card-link btn btn-secondary"
                                           style="width: 40%; font-size: 10px"><@spring.message 'profile.post.edit' /></a>
                                    </div>
                                </div>
                            </div>
                        </#list>
                    <#else>
                        <p><@spring.message 'profile.noResumes' /></p>
                    </#if>
                <#else>
                    <#if data.vacancies?has_content>
                        <#list data.vacancies.content as vacancy>
                            <div class="col">
                                <div class="card" style="min-height: 255px;">
                                    <div class="card-body">
                                        <h5 class="card-title"><a class="text-decoration-none"
                                                                  href="vacancies/${vacancy.id}">${vacancy.name}</a>
                                        </h5>
                                        <h6 class="card-subtitle mb-2 text-body-secondary"><@spring.message 'applicant.salary' /> ${vacancy.salary}</h6>
                                        <p class="card-text"><@spring.message 'applicant.updated' /> ${vacancy.updateDate}</p>

                                    </div>
                                    <div class="card-footer bg-transparent border-top-0">
                                        <a href="/vacancies/update?id=${vacancy.id}"
                                           class="text-light card-link btn btn-secondary"
                                           style="width: 40%; font-size: 10px"><@spring.message 'profile.post.update' /></a>
                                        <a href="/vacancies/edit/${vacancy.id}" class="card-link btn btn-secondary"
                                           style="width: 40%; font-size: 10px"><@spring.message 'profile.edit' /></a>
                                    </div>
                                </div>
                            </div>
                        </#list>
                    <#else>
                        <p><@spring.message 'profile.noVacancies' /></p>
                    </#if>
                </#if>
            </div>
        </div>
    </div>
    <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h1 class="modal-title fs-5" id="exampleModalLabel"><@spring.message 'profile.edit.modal.title' /></h1>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-footer">
                    <div class="modal-body">
                        <form action="/profile" method="post" enctype="multipart/form-data">
                            <div class="mb-3">
                                <label for="avatarFile" class="form-label"><@spring.message 'profile.edit.modal.avatar' /></label>
                                <input type="file" class="form-control" id="avatarFile" name="avatarFile"
                                       aria-describedby="avatarFileHelp">
                            </div>
                            <div class="mb-3">
                                <label for="name" class="form-label"><@spring.message 'profile.edit.modal.name' /></label>
                                <input type="text" class="form-control" id="name" name="name"
                                       aria-describedby="nameHelp" value="${data.user.name}"
                                       required>
                            </div>
                            <#if isEmployer = true>
                                <#else>
                                    <div class="mb-3">
                                        <label for="surname" class="form-label"><@spring.message 'profile.edit.modal.surname' /></label>
                                        <input type="text" class="form-control" id="surname" name="surname"
                                               aria-describedby="surnameHelp" required>
                                    </div>
                                    <div class="mb-3">
                                        <label for="age" class="form-label"><@spring.message 'profile.edit.modal.age' /></label>
                                        <input type="number" class="form-control" id="age" name="age" aria-describedby="ageHelp"
                                               value="${data.user.age}" required>
                                    </div>
                            </#if>
                            <div class="mb-3">
                                <label for="email" class="form-label"><@spring.message 'profile.edit.modal.email' /></label>
                                <input type="email" class="form-control" id="email" name="email"
                                       aria-describedby="emailHelp" value="${data.user.email}" required>
                            </div>
                            <div class="mb-3">
                                <label for="password" class="form-label"><@spring.message 'login.password' /></label>
                                <input type="password" class="form-control" id="password" name="password" required>
                            </div>
                            <div class="mb-3">
                                <label for="phoneNumber" class="form-label"><@spring.message 'profile.edit.modal.phone' /></label>
                                <input type="tel" class="form-control" id="phoneNumber" name="phoneNumber"
                                       value="${data.user.phoneNumber}" required>
                            </div>
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal"><@spring.message 'profile.edit.modal.close' /></button>
                            <@csrf.csrf></@csrf.csrf>
                            <button type="submit" class="btn btn-success"><@spring.message 'profile.edit.modal.update' /></button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</@main.layout>
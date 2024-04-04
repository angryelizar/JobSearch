<#import "../layout.ftlh" as main>
<@main.layout>
    <div class="p-2">
        <h1 class="text-center">Личный профиль</h1>
        <div class="profile-block mt-2">
            <div class="row d-flex align-items-center">
                <div class="col-xxl-2 col-xl-2 col-lg-1 col-md-1">
                    <img src="#" alt="Аватарка профиля" class="rounded-4 img-fluid">
                </div>
                <div class="col-xxl-6 col-xl-4 col-lg-5 col-md-11">
                    <h4 class="d-block">${data.user.name} ${data.user.surname}</h4>
                    <p class="d-block">Возраст: ${data.user.age}</p>
                    <a href="#" class="p-2 btn btn-success d-block">Редактировать</a>
                </div>
                <div class="col-xxl-2 col-xl-3 col-lg-3 col-md-12 mt-3">
                    <#if data.user.accountType = "Соискатель">
                        <a href="#" class="text-center d-block p-3 bg-danger rounded-4 text-decoration-none text-light"
                           style="height: 40%">Создать резюме</a>
                    <#else>
                        <a href="#" class="text-center d-block p-3 bg-danger rounded-4 text-decoration-none text-light"
                           style="height: 40%">Создать вакансию</a>
                    </#if>
                </div>
                <div class="col-xxl-2 col-xl-3 col-lg-3 col-md-12 mt-3">
                    <#if data.user.accountType = "Соискатель">
                        <a href="#"
                           class="text-center d-block p-3 border border-2 rounded-4 text-decoration-none text-black"
                           style="height: 40%">Отклики</a>
                    <#else>
                    </#if>
                </div>
            </div>
        </div>
        <div class="posts-block container-fluid">
             <h3 class="text-start mt-3"><#if data.user.accountType = "Соискатель">Резюме<#else>Вакансии</#if>:</h3>
            <div class="row mt-3">
                <#if data.user.accountType = "Соискатель">
                    <#list data.resumes as resume>
                        <div class="col-12 col-md-6 col-lg-3 mb-1">
                            <div class="card" style="width: 18rem;">
                                <div class="card-body">
                                    <h5 class="card-title"><a class="text-decoration-none"
                                                              href="resumes/${resume.id}">${resume.name}</a></h5>
                                    <h6 class="card-subtitle mb-2 text-body-secondary">Оклад: ${resume.salary}</h6>
                                    <p class="card-text">Обновлено: ${resume.updateDate}</p>
                                    <a href="resumes/update?id=${resume.id}" class="text-light card-link btn btn-info"
                                       style="width: 40%; font-size: 10px">Обновить</a>
                                    <a href="#" class="card-link btn btn-success" style="width: 40%; font-size: 10px">Редактировать</a>
                                </div>
                            </div>
                        </div>
                    </#list>
                <#else>
                    <#list data.vacancies as vacancy>
                        <div class="col-12 col-md-6 col-lg-3 mb-1">
                            <div class="card" style="width: 18rem;">
                                <div class="card-body">
                                    <h5 class="card-title"><a class="text-decoration-none"
                                                              href="vacancies/${vacancy.id}">${vacancy.name}</a></h5>
                                    <h6 class="card-subtitle mb-2 text-body-secondary">Оклад: ${vacancy.salary}</h6>
                                    <p class="card-text">Обновлено: ${vacancy.updateDate}</p>
                                    <a href="/vacancies/update?id=${vacancy.id}" class="text-light card-link btn btn-info"
                                       style="width: 40%; font-size: 10px">Обновить</a>
                                    <a href="#" class="card-link btn btn-success" style="width: 40%; font-size: 10px">Редактировать</a>
                                </div>
                            </div>
                        </div>
                    </#list>
                </#if>
            </div>
        </div>
    </div>
</@main.layout>
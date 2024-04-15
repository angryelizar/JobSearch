<#import "../layout.ftlh" as main>
<@main.layout>
    <div class="p-2">
        <h1 class="text-center">Личный профиль</h1>
        <div class="profile-block mt-2">
            <div class="row d-flex align-items-center">
                <div class="col-xxl-2 col-xl-2 col-lg-1 col-md-1">
                    <img src="/api/users/${userId}/avatar" alt="Аватарка профиля" class="rounded-4 img-fluid">
                </div>
                <div class="col-xxl-6 col-xl-4 col-lg-5 col-md-11">
                    <h4 class="d-block">${data.user.name} ${data.user.surname}</h4>
                    <p class="d-block">Возраст: ${data.user.age}</p>
                    <button type="button" class="p-2 btn btn-secondary d-block" data-bs-toggle="modal"
                            data-bs-target="#exampleModal">
                        Редактировать
                    </button>
                </div>
                <div class="col-xxl-2 col-xl-3 col-lg-3 col-md-12 mt-3">
                    <#if data.user.accountType = "Соискатель">
                        <a href="/resumes/add"
                           class="text-center d-block p-3 bg-danger rounded-4 text-decoration-none text-light"
                           style="height: 40%">Создать резюме</a>
                    <#else>
                        <a href="/vacancies/add"
                           class="text-center d-block p-3 bg-danger rounded-4 text-decoration-none text-light"
                           style="height: 40%">Создать вакансию</a>
                    </#if>
                </div>
                <div class="col-xxl-2 col-xl-3 col-lg-3 col-md-12 mt-3">
                    <#if data.user.accountType = "Соискатель">
                        <a href="/applicant/responses"
                           class="text-center btn position-relative d-block p-3 border border-2 rounded-4 text-decoration-none text-black"
                           style="height: 40%"><#if approvedNumber gt 0><span
                                    class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
    ${approvedNumber}
    <span class="visually-hidden">Количество принятых откликов</span>
  </span><#else></#if> Отклики</a>
                    <#else>
                    </#if>
                </div>
            </div>
        </div>
        <div class="posts-block container-fluid">
            <h3 class="text-start mt-3"><#if data.user.accountType = "Соискатель">Резюме<#else>Вакансии</#if>:</h3>
            <div class="row row-cols-1 row-cols-md-2 row-cols-lg-3 mt-3 g-3">
                <#if data.user.accountType = "Соискатель">
                    <#list data.resumes as resume>
                        <div class="col">
                            <div class="card" style="min-height: 255px;">
                                <div class="card-body">
                                    <h5 class="card-title"><a class="text-decoration-none"
                                                              href="resumes/${resume.id}">${resume.name}</a></h5>
                                    <h6 class="card-subtitle mb-2 text-body-secondary">Оклад: ${resume.salary}</h6>
                                    <p class="card-text">Обновлено: ${resume.updateDate}</p>
                                </div>
                                <div class="card-footer bg-transparent border-top-0">
                                    <a href="resumes/update?id=${resume.id}"
                                       class="text-light card-link btn btn-secondary"
                                       style="width: 40%; font-size: 10px">Обновить</a>
                                    <a href="/resumes/edit/${resume.id}" class="card-link btn btn-secondary"
                                       style="width: 40%; font-size: 10px">Редактировать</a>
                                </div>
                            </div>
                        </div>
                    </#list>
                <#else>
                    <#list data.vacancies as vacancy>
                        <div class="col">
                            <div class="card" style="min-height: 255px;">
                                <div class="card-body">
                                    <h5 class="card-title"><a class="text-decoration-none"
                                                              href="vacancies/${vacancy.id}">${vacancy.name}</a></h5>
                                    <h6 class="card-subtitle mb-2 text-body-secondary">Оклад: ${vacancy.salary}</h6>
                                    <p class="card-text">Обновлено: ${vacancy.updateDate}</p>

                                </div>
                                <div class="card-footer bg-transparent border-top-0">
                                    <a href="/vacancies/update?id=${vacancy.id}"
                                       class="text-light card-link btn btn-secondary"
                                       style="width: 40%; font-size: 10px">Обновить</a>
                                    <a href="/vacancies/edit/${vacancy.id}" class="card-link btn btn-secondary"
                                       style="width: 40%; font-size: 10px">Редактировать</a>
                                </div>
                            </div>
                        </div>
                    </#list>
                </#if>
            </div>
        </div>
    </div>
    <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h1 class="modal-title fs-5" id="exampleModalLabel">Редактирование профиля</h1>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-footer">
                    <div class="modal-body">
                        <form action="/profile" method="post" enctype="multipart/form-data">
                            <div class="mb-3">
                                <label for="avatarFile" class="form-label">Аватарка</label>
                                <input type="file" class="form-control" id="avatarFile" name="avatarFile"
                                       aria-describedby="avatarFileHelp">
                            </div>
                            <div class="mb-3">
                                <label for="name" class="form-label">Ваше имя</label>
                                <input type="text" class="form-control" id="name" name="name"
                                       aria-describedby="nameHelp" value="${data.user.name}"
                                       required>
                            </div>
                            <div class="mb-3">
                                <label for="surname" class="form-label">Ваша фамилия</label>
                                <input type="text" class="form-control" id="surname" name="surname"
                                       aria-describedby="surnameHelp" value="${data.user.surname}"
                                       <#if data.user.accountType="Соискатель">required<#else></#if>>
                            </div>
                            <div class="mb-3">
                                <label for="age" class="form-label">Ваш возраст</label>
                                <input type="number" class="form-control" id="age" name="age" aria-describedby="ageHelp"
                                       value="${data.user.age}" required>
                            </div>
                            <div class="mb-3">
                                <label for="email" class="form-label">Ваша почта</label>
                                <input type="email" class="form-control" id="email" name="email"
                                       aria-describedby="emailHelp" value="${data.user.email}" required>
                            </div>
                            <div class="mb-3">
                                <label for="password" class="form-label">Пароль</label>
                                <input type="password" class="form-control" id="password" name="password" required>
                            </div>
                            <div class="mb-3">
                                <label for="phoneNumber" class="form-label">Ваш номер телефона</label>
                                <input type="tel" class="form-control" id="phoneNumber" name="phoneNumber"
                                       value="${data.user.phoneNumber}" required>
                            </div>
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Закрыть</button>
                            <button type="submit" class="btn btn-success">Обновить профиль</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</@main.layout>
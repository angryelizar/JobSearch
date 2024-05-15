<#import "../layout.ftlh" as main>
<#import "../csrf_template.ftlh" as csrf>
<@main.layout>
<section class="p-3 p-md-4 p-xl-5">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-12 col-xxl-11">
                <div class="card border-light-subtle shadow-sm">
                    <div class="row g-0">
                        <div class="col-12 col-md-6">
                            <img class="img-fluid rounded-start w-100 h-100 object-fit-cover" loading="lazy" src="./images/forgot_password.jpeg" alt="С возвращением, по вам скучали!">
                        </div>
                        <div class="col-12 col-md-6 d-flex align-items-center justify-content-center">
                            <div class="col-12 col-lg-11 col-xl-10">
                                <div class="card-body p-3 p-md-4 p-xl-5">
                                    <div class="row">
                                        <div class="col-12">
                                            <div class="mb-5">
                                                <div class="text-center mb-4">
                                                    <a class="navbar-brand fs-1" href="/">Job Search</a>
                                                </div>
                                                <h2 class="h4 text-center">Восстановление пароля</h2>
                                                <h3 class="fs-6 fw-normal text-secondary text-center m-0">Укажите адрес электронной почты, связанный с вашей учетной записью, чтобы восстановить пароль.</h3>
                                                <#if error??>
                                                    <b class="d-block text-warning fs-6 fw-normal text-secondary text-center m-0">${error}</b>
                                                </#if>
                                                <#if message??>
                                                    <b class="d-block text-warning fs-6 fw-normal text-secondary text-center m-0">${message}</b>
                                                </#if>
                                            </div>
                                        </div>
                                    </div>
                                    <form action="/forgot_password" method="post">
                                        <div class="row gy-3 overflow-hidden">
                                            <div class="col-12">
                                                <div class="form-floating mb-3">
                                                    <input type="email" class="form-control" name="email" id="email" placeholder="name@example.com" required>
                                                    <label for="email" class="form-label">Email</label>
                                                </div>
                                            </div>
                                            <div class="col-12">
                                                <div class="d-grid">
                                                    <@csrf.csrf></@csrf.csrf>
                                                    <button class="btn btn-dark btn-lg" type="submit">Сбросить пароль</button>
                                                </div>
                                            </div>
                                        </div>
                                    </form>
                                    <div class="row">
                                        <div class="col-12">
                                            <div class="d-flex gap-2 gap-md-4 flex-column flex-md-row justify-content-md-center mt-5">
                                                <a href="/login" class="link-secondary text-decoration-none">Вход</a>
                                                <a href="/registration" class="link-secondary text-decoration-none">Регистрация</a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
</@main.layout>
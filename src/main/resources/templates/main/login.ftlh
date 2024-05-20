<#import "../layout.ftlh" as main>
<#import "../csrf_template.ftlh" as csrf>
<#import "/spring.ftl" as spring>
<@main.layout>
    <section class="py-3 py-md-5">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-12 col-sm-10 col-md-8 col-lg-6 col-xl-5 col-xxl-4">
                    <div class="card border border-light-subtle rounded-3 shadow-sm">
                        <div class="card-body p-3 p-md-4 p-xl-5">
                            <div class="text-center mb-3">
                                <a class="navbar-brand fs-1" href="/">Job Search</a>
                            </div>
                            <h2 class="fs-6 fw-normal text-center text-secondary mb-4"><@spring.message 'login.title' /></h2>
                            <form action="/login" method="post">
                                <div class="row gy-2 overflow-hidden">
                                    <div class="col-12">
                                        <div class="form-floating mb-3">
                                            <input type="email" class="form-control" name="username" id="username" placeholder="name@example.com" required>
                                            <label for="username" class="form-label">Email</label>
                                        </div>
                                    </div>
                                    <div class="col-12">
                                        <div class="form-floating mb-3">
                                            <input type="password" class="form-control" name="password" id="password" placeholder="<@spring.message 'login.password' />" required>
                                            <label for="password" class="form-label"><@spring.message 'login.password' /></label>
                                        </div>
                                    </div>
                                    <div class="col-12">
                                        <div class="d-flex gap-2 justify-content-between">
                                            <div class="form-check">
                                                <input class="form-check-input" type="checkbox" value="" name="rememberMe" id="rememberMe">
                                                <label class="form-check-label text-secondary" for="rememberMe">
                                                    <@spring.message 'login.noExit' />
                                                </label>
                                            </div>
                                            <a href="/forgot_password" class="link-primary text-decoration-none"><@spring.message 'login.forgotPassword' /></a>
                                        </div>
                                    </div>
                                    <@csrf.csrf></@csrf.csrf>
                                    <div class="col-12">
                                        <div class="d-grid my-3">
                                            <button class="btn btn-dark btn-lg" type="submit"><@spring.message 'layout.login' /></button>
                                        </div>
                                    </div>
                                    <div class="col-12">
                                        <p class="m-0 text-secondary text-center"><@spring.message 'login.notRegistered' /> <a href="/registration" class="link-primary text-decoration-none"><@spring.message 'login.notRegistered.fix' /></a></p>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</@main.layout>
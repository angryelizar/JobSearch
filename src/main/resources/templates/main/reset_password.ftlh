<#import "../layout.ftlh" as main>
<#import "../csrf_template.ftlh" as csrf>
<@main.layout>
    <div class="text-center">
        <h2>Сбросить пароль</h2>
        <#if error??>
            <b class="text-danger">${error}</b>
        </#if>
    </div>
    <div class="py-5 h-100">
        <div class="row d-flex align-items-center justify-content-center h-100">
            <div class="col-md-7 col-lg-5 col-xl-5">
                <form action="/reset_password" method="post">
                    <@csrf.csrf></@csrf.csrf>
                    <input type="hidden" name="token" value="${token!}">
                    <input type="password" name="password" class="form-control"
                           placeholder="Введите новый пароль" required autofocus/>
                    <p class="text-center">
                        <button type="submit" class="btn btn-dark btn-lg mt-3">Изменить пароль</button>
                    </p>
                </form>
            </div>
        </div>
    </div>
</@main.layout>
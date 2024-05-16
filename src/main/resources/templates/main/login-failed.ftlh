<#import "../layout.ftlh" as main>
<@main.layout>
    <h2 class="text-center mt-5">Вход не удался</h2>
    <p class="text-center">E-mail или пароль не верны - попробуйте еще раз</p>
    <div class="text-center mt-5"><a href="/login" class="btn btn-dark btn-lg">Повторить</a></div>
    <div class="text-center col-12 mt-3">
        <a href="/forgot_password" class="text-center link-primary text-decoration-none">Забыли пароль?</a>
        <p class="m-0 text-secondary text-center">Не зарегистрированы? <a href="/registration" class="link-primary text-decoration-none">Исправим это!</a></p>
    </div>
</@main.layout>
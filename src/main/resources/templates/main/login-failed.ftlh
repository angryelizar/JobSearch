<#import "../layout.ftlh" as main>
<#import "/spring.ftl" as spring>
<@main.layout>
    <h2 class="text-center mt-5"><@spring.message 'login.failed'/></h2>
    <p class="text-center"><@spring.message 'login.failed.description'/></p>
    <div class="text-center mt-5"><a href="/login" class="btn btn-dark btn-lg"><@spring.message 'login.failed.repeat'/></a></div>
    <div class="text-center col-12 mt-3">
        <a href="/forgot_password" class="text-center link-primary text-decoration-none"><@spring.message 'login.forgotPassword'/></a>
        <p class="m-0 text-secondary text-center"><@spring.message 'login.notRegistered'/> <a href="/registration" class="link-primary text-decoration-none"><@spring.message 'login.notRegistered.fix'/></a></p>
    </div>
</@main.layout>
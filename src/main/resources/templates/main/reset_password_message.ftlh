<#import "../layout.ftlh" as main>
<#import "/spring.ftl" as spring>
<@main.layout>
<div class="text-center">
    <h2><@spring.message 'forgotPassword.reset'/></h2>
    <p class="text-danger mt-3"><@spring.message "${message}"/></p>
    <div class="text-center mt-3"><a href="/" class="btn btn-dark btn-lg"><@spring.message 'resetPassword.message.link'/></a></div>
</div>
</@main.layout>
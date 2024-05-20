<#import "../layout.ftlh" as main>
<#import "/spring.ftl" as spring>
<@main.layout>
    <h2 class="text-center mt-5"><@spring.message 'errors.403.title'/></h2>
    <h3 class="text-center mt-5">403</h3>
    <div class="text-center mt-5"><a href="/" class="btn btn-dark btn-lg"><@spring.message 'resetPassword.message.link'/></a></div>
</@main.layout>
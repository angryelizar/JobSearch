<#import "../layout.ftlh" as main>
<#import "/spring.ftl" as spring>
<@main.layout>
    <h2 class="text-center mt-5"><@spring.message 'errors.400.title'/></h2>
    <h3 class="text-center mt-5">400</h3>
    <h4 class="text-center mt-5"><@spring.message 'errors.400.subTitle'/></h4>
    <div class="text-center mt-5"><a href="/" class="btn btn-dark btn-lg"><@spring.message 'resetPassword.message.link'/></a></div>
</@main.layout>
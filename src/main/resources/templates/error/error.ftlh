<#import "../layout.ftlh" as main>
<@main.layout>
    <h2 class="text-center mt-5 text-danger">${exceptionText}</h2>
    <div class="text-center mt-5"><a href="${redirectLink}" class="btn btn-dark btn-lg">${redirectLinkTitle}</a></div>
</@main.layout>
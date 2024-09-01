<#import "../layout.ftlh" as main>
<#import "../csrf_template.ftlh" as csrf>
<#import "/spring.ftl" as spring>
<@main.layout>
    <div class="row">
        <div class="col-12 col-md-4">
            <h4>${employerName}</h4>
            <h5><@spring.message 'message.countOfVacancies'/> ${employerCountOfVacancies}</h5>
            <br>
            <hr>
            <br>
            <h4>${applicantName}</h4>
            <h5><@spring.message 'message.countOfResumes'/> ${applicantCountOfResumes}</h5>
        </div>
        <div class="col-12 col-md-8 messages-block" style="min-height: 400px; overflow: auto;">
            <#if messages?has_content>
                <#list messages as message>
                    <div class="card w-100 text-bg-light mb-3">
                        <div class="card-header">
                            ${message.dateTime}
                        </div>
                        <div class="card-body">
                            <h5>${message.author}</h5>
                            <p class="card-text">${message.content}</p>
                        </div>
                    </div>
                </#list>
            <#else>
                <p class="text-center"></p>
            </#if>
        </div>
        <div class="col-12">
            <div class="mb-3">
                <textarea class="form-control" name="messageText" id="messageText"></textarea>
                <input type="number" class="d-none" name="messageAuthor" value="${fromTo}">
                <input type="number" class="d-none" name="messageRecipient" value="${toFrom}">
                <input type="number" class="d-none" name="respondApplicant" value="${id}">
            </div>
            <@csrf.csrf></@csrf.csrf>
            <button id="submitButton" type="submit"
                    class="btn btn-dark"><@spring.message 'vacancies.add.submit'/></button>
        </div>
    </div>
    <script src="../../scripts/message.js"></script>
    <script src="../../scripts/stomp.js"></script>
    <script src="../../scripts/sockjs-0.3.4.js"></script>
</@main.layout>
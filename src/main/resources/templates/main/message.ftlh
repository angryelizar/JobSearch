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
        </div>
        <form  class="col-12" action="/message/response" method="post">
            <div class="mb-3">
                <textarea type="password" class="form-control" name="messageText" id="messageText"></textarea>
                <input type="text" class="d-none" name="messageAuthor" value="${fromTo}">
                <input type="text" class="d-none" name="messageRecipient" value="${toFrom}">
                <input type="text" class="d-none" name="respondApplicant" value="${id}">
            </div>
            <@csrf.csrf></@csrf.csrf>
            <button id = "submitButton" type="submit" class="btn btn-dark"><@spring.message 'vacancies.add.submit'/></button>
        </form>
    </div>
    <script src="../../scripts/message.js"></script>
</@main.layout>
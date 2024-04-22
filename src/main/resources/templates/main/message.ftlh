<#import "../layout.ftlh" as main>
<#assign formatter = "HH:mm, d MMMM, yyyy">
<@main.layout>
    <div class="row">
        <div class="col-12 col-md-4">
            <h4>${employerName}</h4>
            <h5>Количество вакансий: ${employerCountOfVacancies}</h5>
            <br>
            <hr>
            <br>
            <h4>${applicantName}</h4>
            <h5>Количество резюме: ${applicantCountOfResumes}</h5>
        </div>
        <div class="col-12 col-md-8" style="min-height: 400px; overflow: auto;">
            <#if messages?has_content>
                <#list messages as message>
                    <div class="card w-100 text-bg-light mb-3">
                        <div class="card-header">${message.dateTime}</div>
                        <div class="card-body">
                            <h5 class="card-title">${message.author}</h5>
                            <p class="card-text">${message.content}</p>
                        </div>
                    </div>
                </#list>
                <#else>
                <p class="text-center mb-5">В этом чате пока нет сообщений - сделайте первый шаг!</p>
            </#if>
            <form action="/message/response" method="post">
                <div class="mb-3">
                    <textarea type="password" class="form-control" name="messageText" id="messageText"></textarea>
                    <input type="text" class="d-none" name="messageAuthor" value="${fromTo}">
                    <input type="text" class="d-none" name="messageRecipient" value="${toFrom}">
                    <input type="text" class="d-none" name="respondApplicant" value="${id}">
                </div>
                <button id = "submitButton" type="submit" class="btn btn-dark">Отправить</button>
            </form>
        </div>
    </div>
    <script src="../../scripts/message.js"></script>
</@main.layout>
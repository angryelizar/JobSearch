<#import "../layout.ftlh" as main>
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
            <#list messages as message>
                <div class="card w-100 text-bg-light mb-3">
                    <div class="card-header">${message.dateTime}</div>
                    <div class="card-body">
                        <h5 class="card-title">${message.author}</h5>
                        <p class="card-text">${message.content}</p>
                    </div>
                </div>
            </#list>
            <form action="#">
                <div class="mb-3">
                    <textarea type="password" class="form-control" id="messageText"></textarea>
                </div>
                <button type="submit" class="btn btn-dark">Отправить</button>
            </form>
        </div>
    </div>
</@main.layout>
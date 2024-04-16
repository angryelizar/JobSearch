<#import "../layout.ftlh" as main>
<@main.layout>
    <div class="row">
        <div class="col-12 col-md-10">
            <h1>${vacancy.name}</h1>
            <b>${vacancy.updateTime}</b>
            <h2>${vacancy.salary}</h2>
            <h3>${vacancy.category}</h3>
            <h4>${vacancy.author}</h4>
            <h5>Необходимый опыт в годах: от ${vacancy.expFrom} до ${vacancy.expTo}</h5>
            <h6>Описание</h6>
            <p class="w-100 d-block">${vacancy.description}</p>
        </div>
        <div class="col-12 col-md-2 me-auto ms-auto">
            <#if isApplicant == true>
                <button type="button" class="btn btn-success" data-bs-toggle="modal" data-bs-target="#modal">
                    Откликнуться
                </button><#else></#if>
        </div>
        <div class="modal fade" id="modal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h1 class="modal-title fs-5" id="exampleModalLabel">Оставить отклик на вакансию</h1>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Закрыть"></button>
                    </div>
                        <div class="modal-body">
                            <input type="text" class="d-none" id="userEmail"
                                   value="<#if isApplicant == true>${userAuth.getName()}<#else></#if>">
                        </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Закрыть</button>
                        <button type="button" form="response" id="respondButton" class="btn btn-primary">Отправить отклик</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script src="../scripts/vacancy.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
            crossorigin="anonymous"></script>
</@main.layout>
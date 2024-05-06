<#import "../layout.ftlh" as main>
<@main.layout>
    <h3 class="text-center mt-3">Поиск вакансий на JobSearch</h3>
    <div class="input-group input-group-lg mt-3">
        <input placeholder="Введите свой запрос здесь...."  id="search-block" type="text" class="form-control" aria-label="Поиск"
               aria-describedby="inputGroup-sizing-lg">
    </div>
    <div class="row mt-3">
        <div class="col-12 col-md-12 d-flex flex-column gap-3" id="card-wrap">
        </div>
    </div>
    <script src="../scripts/search.js"></script>
</@main.layout>
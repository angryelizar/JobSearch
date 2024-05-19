<#import "../layout.ftlh" as main>
<#import '/spring.ftl' as spring>
<@main.layout>
    <h3 class="text-center mt-3"><@spring.message 'vacancies.search'/></h3>
    <div class="input-group input-group-lg mt-3">
        <input placeholder="<@spring.message 'vacancies.search.placeholder'/>"  id="search-block" type="text" class="form-control" aria-label="<@spring.message 'vacancies.search.placeholder'/>"
               aria-describedby="inputGroup-sizing-lg">
    </div>
    <div class="row mt-3">
        <div class="col-12 col-md-12 d-flex flex-column gap-3" id="card-wrap">
        </div>
    </div>
    <script src="../scripts/search.js"></script>
</@main.layout>
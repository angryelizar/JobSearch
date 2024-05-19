<#import "../layout.ftlh" as main>
<#import "../csrf_template.ftlh" as csrf>
<#import '/spring.ftl' as spring>
<@main.layout>
    <h1><@spring.message 'vacancies.edit.editing'/></h1>
    <div class="row">
        <div class="col-12 col-xl-10">
            <form action="/vacancies/edit" method="post">
                <div class="d-none">
                    <label for="id" class="form-label">ID</label>
                    <input type="text" class="form-control" id="id" name="id" aria-describedby="idHelp"
                           value="${vacancy.id}">
                </div>
                <div class="mb-3">
                    <label for="name" class="form-label"><@spring.message 'vacancies.add.title'/></label>
                    <input type="text" class="form-control" id="name" name="name" aria-describedby="nameHelp"
                           value="${vacancy.name}">
                </div>
                <div class="mb-3">
                    <label for="categoryId" class="form-label"><@spring.message 'vacancies.add.category'/></label>
                    <select id="categoryId" class="form-select" name="categoryId">
                        <#list categories as category>
                            <option value="${category.id}">${category.name}</option>
                        </#list>
                    </select>
                </div>
                <div class="mb-3">
                    <label for="salary" class="form-label"><@spring.message 'vacancies.add.salary'/></label>
                    <input type="number" class="form-control" id="salary" name="salary" placeholder="${vacancy.salary}"
                           value="${vacancy.salary}" required>
                </div>
                <div class="mb-3">
                    <label for="description mb-3"><@spring.message 'vacancies.add.description'/></label>
                    <textarea class="form-control" id="description" name="description" rows="10"
                              required>${vacancy.description}</textarea>
                    <div id="descriptionHelp" class="form-text"><@spring.message 'vacancies.add.empty'/></div>
                    <div id="descriptionHelp" class="form-text"><@spring.message 'vacancies.add.size'/>
                    </div>
                </div>
                <div class="mb-3">
                    <label for="experience d-block mb-3"><@spring.message 'vacancies.add.experience'/></label>
                    <div class="d-flex justify-content-start">
                        <div class="experience-item d-flex align-items-center me-3">
                            <label for="expFrom" class="form-label me-3"><@spring.message 'vacancies.add.from'/></label>
                            <input type="number" class="form-control" id="expFrom" name="expFrom"
                                   value="${vacancy.expFrom}" required>
                        </div>
                        <div class="experience-item d-flex justify-content-between align-items-center">
                            <label for="expTo" class="form-label me-3"><@spring.message 'vacancies.add.to'/></label>
                            <input type="number" class="form-control" id="expTo" name="expTo" value="${vacancy.expTo}"
                                   required>
                        </div>
                    </div>
                </div>
                <div class="mb-3 form-check">
                    <input type="checkbox" class="form-check-input" id="isActive" name="isActive">
                    <label class="form-check-label" for="isActive"><@spring.message 'vacancies.add.active'/></label>
                </div>
                <@csrf.csrf></@csrf.csrf>
                <button type="submit" class="btn btn-secondary me-5"><@spring.message 'vacancies.add.submit'/></button>
                <a href="/vacancies/delete?id=${vacancy.id}" class=" btn btn-danger"><@spring.message 'vacancies.edit.delete'/></a>
            </form>
        </div>
    </div>
</@main.layout>
<#import "../layout.ftlh" as main>
<#import "/spring.ftl" as spring>
<#import "../csrf_template.ftlh" as csrf>
<@main.layout>
    <div class="p-5">
        <h1 class="text-center"><@spring.message 'registration.title'/></h1>
        <form action="/registration" method="post">
            <div class="mb-3">
                <label for="accountType" class="form-label"><@spring.message 'registration.who'/></label>
                <@spring.formSingleSelect 'userDto.accountType' accountTypes 'name="accountType" id="accountType" class="form-control"'></@spring.formSingleSelect>
                <@spring.showErrors "<br>" 'text-danger'/>
            </div>
            <div class="mb-3" id="name-input">
                <label for="name" class="form-label" id="name-label"><@spring.message 'profile.edit.modal.name'/></label>
                <@spring.formInput "userDto.name" ' class="form-control" id="name" name= "name"'></@spring.formInput>
                <@spring.showErrors "<br>" 'text-danger'/>
            </div>
            <div class="mb-3" id="surname-input">
                <label for="surname" class="form-label"><@spring.message 'profile.edit.modal.surname'/></label>
                <@spring.formInput "userDto.surname" 'class="form-control" id="surname" name= "surname"'></@spring.formInput>
                <@spring.showErrors "<br>" 'text-danger'/>
            </div>
            <div class="mb-3" id="age-input">
                <label for="age" class="form-label"><@spring.message 'profile.edit.modal.age'/></label>
                <@spring.formInput "userDto.age" ' class="form-control" id="age" name="age"' "number"></@spring.formInput>
                <@spring.showErrors "<br>" 'text-danger'/>
            </div>
            <div class="mb-3">
                <label for="email" class="form-label"><@spring.message 'profile.edit.modal.email'/></label>
                <@spring.formInput "userDto.email" 'class="form-control" id="email"'></@spring.formInput>
                <@spring.showErrors "<br>" 'text-danger'/>
            </div>
            <div class="mb-3">
                <label for="password" class="form-label"><@spring.message 'login.password'/></label>
                <@spring.formPasswordInput "userDto.password" 'class="form-control"'></@spring.formPasswordInput>
                <@spring.showErrors "<br>" 'text-danger'/>
            </div>
            <div class="mb-3">
                <label for="phoneNumber" class="form-label"><@spring.message 'profile.edit.modal.phone'/></label>
                <@spring.formInput "userDto.phoneNumber" 'class="form-control" id="phoneNumber"'></@spring.formInput>
                <@spring.showErrors "<br>" 'text-danger'/>
            </div>
            <@csrf.csrf></@csrf.csrf>
            <button type="submit" class="btn btn-secondary"><@spring.message 'registration.submit'/></button>
        </form>
    </div>
    <script src="../scripts/registration.js">
</@main.layout>
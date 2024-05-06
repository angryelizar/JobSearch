<#import "../layout.ftlh" as main>
<#import "/spring.ftl" as spring>
<@main.layout>
    <div class="p-5">
        <h1 class="text-center">Регистрация</h1>
        <form action="/registration" method="post">
            <div class="mb-3">
                <label for="accountType" class="form-label">Кто вы?</label>
                <@spring.formSingleSelect 'userDto.accountType' accountTypes 'name="accountType" id="accountType" class="form-control"'></@spring.formSingleSelect>
                <@spring.showErrors "<br>" 'text-danger'/>
<#--                <select name="accountType" id="accountType" class="form-control" required>-->
<#--                    <option value="Работодатель">Работодатель</option>-->
<#--                    <option value="Соискатель">Соискатель</option>-->
<#--                </select>-->
            </div>
            <div class="mb-3">
                <label for="name" class="form-label">Ваше имя</label>
                <@spring.formInput "userDto.name" 'placeholder="Ваше имя" class="form-control" id="name" name= "name"'></@spring.formInput>
                <@spring.showErrors "<br>" 'text-danger'/>
            </div>
            <div class="mb-3">
                <label for="surname" class="form-label">Ваша фамилия</label>
                <@spring.formInput "userDto.surname" 'placeholder="Ваша фамилия" class="form-control" id="surname" name= "surname"'></@spring.formInput>
                <@spring.showErrors "<br>" 'text-danger'/>
            </div>
            <div class="mb-3">
                <label for="age" class="form-label">Ваш возраст</label>
                <@spring.formInput "userDto.age" 'placeholder="Ваш возраст" class="form-control" id="age" name="age"' "number"></@spring.formInput>
                <@spring.showErrors "<br>" 'text-danger'/>
            </div>
            <div class="mb-3">
                <label for="email" class="form-label">Ваша почта</label>
                <@spring.formInput "userDto.email" 'class="form-control" placeholder="Ваша почта" id="email"'></@spring.formInput>
                <@spring.showErrors "<br>" 'text-danger'/>
            </div>
            <div class="mb-3">
                <label for="password" class="form-label">Пароль</label>
                <@spring.formPasswordInput "userDto.password" 'class="form-control"'></@spring.formPasswordInput>
                <@spring.showErrors "<br>" 'text-danger'/>
            </div>
            <div class="mb-3">
                <label for="phoneNumber" class="form-label">Ваш номер телефона</label>
                <@spring.formInput "userDto.phoneNumber" 'class="form-control" id="phoneNumber"'></@spring.formInput>
                <@spring.showErrors "<br>" 'text-danger'/>
            </div>
            <button type="submit" class="btn btn-secondary">Зарегистрироваться</button>
        </form>
    </div>
</@main.layout>
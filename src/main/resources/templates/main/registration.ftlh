<#import "../layout.ftlh" as main>
<@main.layout>
<div class="p-5">
    <h1 class="text-center">Регистрация</h1>
    <form action="/registration" method="post">
        <div class="mb-3">
            <label for="name" class="form-label">Ваше имя</label>
            <input type="text" class="form-control" id="name" name="name" aria-describedby="nameHelp" required>
        </div>
        <div class="mb-3">
            <label for="surname" class="form-label">Ваша фамилия</label>
            <input type="text" class="form-control" id="surname" name="surname" aria-describedby="surnameHelp">
        </div>
        <div class="mb-3">
            <label for="age" class="form-label">Ваш возраст</label>
            <input type="number" class="form-control" id="age" name="age" aria-describedby="ageHelp" required>
        </div>
        <div class="mb-3">
            <label for="email" class="form-label">Ваша почта</label>
            <input type="email" class="form-control" id="email" name="email" aria-describedby="emailHelp" required>
        </div>
        <div class="mb-3">
            <label for="password" class="form-label">Пароль</label>
            <input type="password" class="form-control" id="password", name="password" required>
        </div>
        <div class="mb-3">
            <label for="phoneNumber" class="form-label">Ваш номер телефона</label>
            <input type="tel" class="form-control" id="phoneNumber" name="phoneNumber" required>
        </div>
        <div class="mb-3">
            <label for="accountType" class="form-label">Кто вы?</label>
            <select name="accountType" id="accountType" required>
                <option value="Работодатель">Работодатель</option>
                <option value="Соискатель">Соискатель</option>
            </select>
        </div>
        <button type="submit" class="btn btn-secondary">Зарегистрироваться</button>
    </form>
</div>
</@main.layout>
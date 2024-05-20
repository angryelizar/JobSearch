<#import "csrf_template.ftlh" as csrf>
<#import '/spring.ftl' as spring>
<#macro layout>
    <!doctype html>
    <html lang="ru">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="current-locale" content="${.locale}" id="locale">
        <meta name="csrf-header" content="${_csrf.headerName}" id="csrf-header">
        <meta name="csrf-parameter" content="${_csrf.parameterName}" id="csrf-parameter">
        <meta name="csrf-token" content="${_csrf.token}" id="csrf-token">
        <title><#if pageTitle??>${pageTitle}<#else>JobSearch</#if></title>
        <link rel="icon" href="images/icon.png" type="image/png">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
              crossorigin="anonymous">
        <link rel="stylesheet" href="../../css/style.css">
        <link rel="stylesheet" href="../../css/language.css">
    </head>
    <body>
    <nav class="navbar bg-primary navbar-expand-lg bg-body-tertiary" data-bs-theme="dark">
        <div class="container-fluid">
            <a class="navbar-brand" href="/">Job Search</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                    data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                    aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <a class="nav-link active" aria-current="page" href="/vacancies"><@spring.message 'layout.vacancies'/></a>
                    </li>
                    <#if isEmployer??>
                        <#if isEmployer == true>
                            <li class="nav-item">
                                <a class="nav-link" href="/resumes"><@spring.message 'layout.resumes'/></a>
                            </li>
                        </#if>
                    </#if>
                    <li class="nav-item">
                        <a class="nav-link" href="/employers"><@spring.message 'layout.employers'/></a>
                    </li>
                </ul>
                <div class="text-secondary-emphasis d-flex justify-content-center align-items-center" style="gap: 10px">
                    <div class="nav-item">
                        <div class="language" role="radiogroup" aria-labelledby="language-switcher1">
                            <div class="language__container--left language__container--kg" id="container-kg">
                                <input class="language__control" type="radio" id="language-kg" name="language-switch1">
                                <label class="language__label" for="language1-1">
                                    KG<span class="hidden"></span>
                                </label>
                            </div>
                            <div class="language__container--right language__container--ru" id="container-ru">
                                <input class="language__control" type="radio" id="language-ru" name="language-switch1">
                                <label class="language__label" for="language1-2">
                                    RU<span class="hidden"></span>
                                </label>
                            </div>
                        </div>
                    </div>
                    <div class="nav-item">
                        <a class="fs-2 nav-link active" aria-current="page" href="/vacancies/search"><i
                                    class="bi bi-search"></i></a>
                    </div>
                    <#if isAuthenticated??>
                        <#if isAuthenticated == true>
                            <div class="nav-item"><a class="fs-2 nav-link" href="/profile"><i
                                            class="bi bi-person-circle"></i></a></div>
                            <div class="nav-item"><a class="fs-2 nav-link" href="/messages"><i
                                            class="bi bi-envelope-open"></i></a></div>
                            <#if _csrf??>
                                <form action="/logout" method="post">
                                    <@csrf.csrf></@csrf.csrf>
                                    <div class="nav-item"><button type="submit" class="fs-2 nav-link"><i
                                                    class="bi bi-box-arrow-left"></i></button></div>
                                </form>
                            </#if>
                        <#else>
                            <div class="nav-item"><a class="nav-link" href="/login"><@spring.message 'layout.login'/></a></div>
                            <div class="nav-item"><a class="nav-link" href="/registration"><@spring.message 'layout.registration'/></a></div>
                        </#if>
                    </#if>
                </div>
            </div>
        </div>
    </nav>
    <div class="pb-5 container mt-3 mb-3">
        <#nested>
    </div>
    <footer class="position-fixed bottom-0 w-100 navbar bg-primary bg-body-tertiary" data-bs-theme="dark">
        <div class="container-fluid">
    <span class="navbar-text">
      Job Search, 2024
    </span>
        </div>
    </footer>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
            crossorigin="anonymous"></script>
    <script src="../scripts/navbar.js"></script>
    </body>
    </html>
</#macro>
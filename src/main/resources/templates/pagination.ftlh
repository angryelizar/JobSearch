<#macro pagination>
    <p class="mt-3 text-center">Всего записей ${size}, на странице отображается по 5 записей</p>
    <ul class="mt-2 mb-3 pagination justify-content-center">
        <li class="page-item"><a class="page-link" href="/vacancies?page=${page - 1}">Назад</a></li>
        <li class="page-item"><a class="page-link" href="/vacancies?page=${page + 1}">Вперед</a></li>
    </ul>
</#macro>
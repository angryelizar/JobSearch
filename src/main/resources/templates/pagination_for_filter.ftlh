<#import '/spring.ftl' as spring>
<#macro pagination url categoryId criterion order page>
    <#if page.getTotalPages() gt 7>
        <#assign
        totalPages = page.getTotalPages()
        pageNumber = page.getNumber() + 1
        head = (pageNumber > 4)?then([1, -1], [1, 2, 3])
        tail = (pageNumber < totalPages - 3)?then([-1, totalPages], [totalPages - 2, totalPages - 1, totalPages])
        bodyBefore = (pageNumber > 4 && pageNumber < totalPages - 1)?then([pageNumber - 2, pageNumber - 1], [])
        bodyAfter = (pageNumber > 2 && pageNumber < totalPages - 3)?then([pageNumber + 1, pageNumber + 2], [])
        body = head + bodyBefore + (pageNumber > 3 && pageNumber < totalPages - 2)?then([pageNumber], []) + bodyAfter + tail
        >
    <#else>
        <#assign body = 1..page.getTotalPages()>
    </#if>
    <div class="pagination-container d-flex justify-content-center flex-column align-items-center">
    <ul class="pagination mt-2">
        <li class="page-item disabled">
            <a class="page-link"><@spring.message 'pagination.pages'/></a>
        </li>
        <#list body as p>
            <#if (p - 1)  == page.getNumber()>
                <li class="page-item active">
                    <a href="#" class="page-link">${p}</a>
                </li>
                <#elseif p == -1>
                    <li class="page-item disabled">
                        <a href="#" class="page-link">...</a>
                    </li>
            <#else>
                <li class="page-item">
                    <a href="?categoryId=${categoryId}&criterion=${criterion}&order=${order}&page=${p-1}&size=${page.getSize()}" class="page-link">${p}</a>
                </li>
            </#if>
        </#list>
    </ul>

    <ul class="pagination mt-2">
        <li class="page-item disabled">
            <a class="page-link"><@spring.message 'pagination.elements'/></a>
        </li>
        <#list [5, 10, 25, 50] as c>
            <#if c  == page.getSize()>
                <li class="page-item active">
                    <a href="#" class="page-link">${c}</a>
                </li>
            <#else>
                <li class="page-item">
                    <a href="?categoryId=${categoryId}&criterion=${criterion}&order=${order}&page=${page.getNumber()}&size=${c}" class="page-link">${c}</a>
                </li>
            </#if>
        </#list>
    </ul>
    </div>
</#macro>
<#macro csrf>
    <#if _csrf??>
        <input type="hidden" name="${_csrf.parameterName!'scrf-parameter-name'}" value="${(_csrf.token)!'csrf-token'}">
    </#if>
</#macro>
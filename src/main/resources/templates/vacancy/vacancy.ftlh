<#import "../layout.ftlh" as main>
<@main.layout>
    <h1>${vacancy.name}</h1>
    <b>${vacancy.updateTime}</b>
    <h2>${vacancy.salary}</h2>
    <h3>${vacancy.category}</h3>
    <h4>${vacancy.author}</h4>
    <h5>Необходимый опыт в годах: от ${vacancy.expFrom} до ${vacancy.expTo}</h5>
    <h6>Описание</h6>
    <p class="w-100 d-block">${vacancy.description}</p>
</@main.layout>
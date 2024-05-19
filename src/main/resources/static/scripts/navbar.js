let kgRadio = document.getElementById('container-kg')
let ruRadio = document.getElementById('container-ru')
const currentUrl = `${window.location.protocol}//${window.location.host}${window.location.pathname}`;

function getToRussian() {
    window.location.href = currentUrl + '?lang=ru'
}

function getToKyrgyz() {
    window.location.href = currentUrl + '?lang=ky'
}

ruRadio.addEventListener('click', getToRussian);
kgRadio.addEventListener('click', getToKyrgyz);
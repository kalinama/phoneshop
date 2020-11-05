function redirect(url) {
    document.location.href = url;
}

function changeColor(tableRow, onmouseover) {
    if (onmouseover) {
        tableRow.style.backgroundColor = '#eaeaea';
    } else {
        tableRow.style.backgroundColor = 'white';
    }
}
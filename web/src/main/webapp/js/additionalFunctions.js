
function redirect(url) {
    document.location.href = url;
}

function popUpShow(){
    $("#popup").show();
    $(".fullscreen-container").fadeTo(200, 1);

}

function popUpHide(){
    $("#popup").hide();
    $(".fullscreen-container").fadeOut(200);
}

function formSubmit() {
    document.getElementById("logoutForm").submit();
}
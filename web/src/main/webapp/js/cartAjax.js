$(document).ready(get());

function add(phoneId) {
    let quantity = $("#quantityAddToCart-" + phoneId).val();
    $.ajax({
        contentType: "application/json;charset=UTF-8",
        url: "ajaxCart",
        type: "POST",
        dataType: "json",
        data: JSON.stringify({phoneId: phoneId, quantity: quantity}),
        success: function (data) {
            console.log(data);
            if (data.added) {
                setMiniCart(data.miniCart);
                $("#message-" + phoneId).text(data.message).css({'color': 'green'});
            } else {
                $("#message-" + phoneId).text(data.message).css({'color': 'red'});
            }
        }
    });
}

function get() {
    $.getJSON("ajaxCart",
        function (returnedData) {
            setMiniCart(returnedData)
        });
}

function setMiniCart(cart) {
    $("#miniCart").text("My cart: " + cart.totalQuantity + " items " + cart.totalCost + " $");
}

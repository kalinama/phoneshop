$(document).ready(get());

function add(phoneId) {
    let quantity = $("#quantityAddToCart-" + phoneId).val();
    $.post(myContextPath + "/ajaxCart", {phoneId: phoneId, quantity: quantity},
        function (data) {
        const message = $(`#message-${phoneId}`);

            if (data.added) {
                setMiniCart(data.miniCart);
                message.text(data.message).css({'color': 'green'});

                setTimeout(function(){
                    message.text('');
                    }, 3000);

            } else {
                message.text(data.message).css({'color': 'red'});
            }
    });
}

function get() {
    $.getJSON(myContextPath + "/ajaxCart",
        function (returnedData) {
            setMiniCart(returnedData)
        });
}

function setMiniCart(cart) {
    $(`#miniCart`).text("My cart: " + cart.totalQuantity + " items " + cart.totalCost + " $");
}

function changeStatus(orderId, status) {
    $.ajax({
        url: myContextPath + "/admin/orders/" + orderId,
        type: 'POST',
        data: JSON.stringify({orderStatus: status}),
        contentType: 'application/json',
        success: function () {
            $(`#button-delivered`).remove();
            $(`#button-rejected`).remove();
            $(`#status`).text(status.toUpperCase())
        }
    });
}
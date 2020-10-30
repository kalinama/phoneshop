$(document).ready(function () {
    if ($(window).width() > 992) {
        $(window).scroll(function () {
            if ($(this).scrollTop() > 40) {
                $('#myHeader').addClass("fixed-top");
                $('body').css('padding-top', $('.navbar').outerHeight() + 'px');
            } else {
                $('#myHeader').removeClass("fixed-top");
                $('body').css('padding-top', '0');
            }
        });
    }
});

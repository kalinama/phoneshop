
$(document).ready($(document).ajaxSend(function(e, xhr, options) {
        const csrfParameter = $("meta[name='_csrf_parameter']").attr("content");
        const csrfHeader = $("meta[name='_csrf_header']").attr("content");
        const csrfToken = $("meta[name='_csrf']").attr("content");
    xhr.setRequestHeader(csrfHeader, csrfToken);
}));


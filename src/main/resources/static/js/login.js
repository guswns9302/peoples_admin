$(document).ready(function() {
    if (HttpOnlyCookieExist("remember-me-appliance")) {
        $("#rememberMe").attr("checked", true);
    }
});

function HttpOnlyCookieExist(cookiename) {
    var d = new Date();
    d.setTime(d.getTime() + (1000));
    var expires = "expires=" + d.toUTCString();
    document.cookie = cookiename + "=new_value;path=/;" + expires;
    return document.cookie.indexOf(cookiename + '=') == -1;
}

function goLogin() {
    var input = $('.validate-input .check-input');
    var check = true;
    for (var i = 0; i < input.length; i++) {
        if (validate(input[i]) == false) {
            showValidate(input[i]);
            check = false;
        }
    }
    if (check) {
        var param = {"userId": $("input[name=userId]").val(), "password": $("input[name=password]").val()};
        if ($("#rememberMe").is(":checked")) {
            param["rememberMeParam"] = true;
        }
        $.ajax({
            url: "/login",
            method: "POST",
            contentType: "application/x-www-form-urlencoded; charset=utf-8;",
            data: param,
            headers: getCsrfHeader(),
            cache: false,
            success: function(data) {
                    location.replace("/dashboard");
            },
            error: function(req, status, error) {
                console.log("실패");
            }
        });
    }
}

function validate(input) {
    if ($(input).attr('type') == 'email' || $(input).attr('name') == 'email') {
        if ($(input).val().trim().match(/^([a-zA-Z0-9_\-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([a-zA-Z0-9\-]+\.)+))([a-zA-Z]{1,5}|[0-9]{1,3})(\]?)$/) == null) {
            return false;
        }
    }
    else {
        if ($(input).val().trim() == '') {
            return false;
        }
    }
}

function showValidate(input) {
    var thisAlert = $(input).parent();
    $(thisAlert).addClass('alert-validate');
}
function hideValidate(input) {
    var thisAlert = $(input).parent();
    $(thisAlert).removeClass('alert-validate');
}

function getCsrfHeader() {
    var csrfHeaderName = $("meta[name='_csrf_header']").attr("content");
    var csrfHeaderToken = $("meta[name='_csrf']").attr("content");
    var header = {};
    header[csrfHeaderName] = csrfHeaderToken;
    return header;
}

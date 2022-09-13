$(document).ready(function() {
    $("#admin-modal").on("show.bs.modal", function(event) {
        openAdminModal(event);

    });

    findAllAdmin();
});

var adminJson = [];

// 관리자 생성 모달창 이벤트
var openAdminModal = function(event) {
    document.getElementById("adminCreateForm").classList.remove('was-validated');
    $("#admin-id").val("");
    $("#admin-pw").val("");
    $("#admin-nickname").val("");

    setTimeout(function (){
        $("#admin-id").focus();
    }, 500);
}

function findAllAdmin(){
    var option = deepExtend({}, ajaxOptions);
    option.URL = "/api/v1/admins";
    option.PARAM = {};
    option.TYPE = "get";
    option.ASYNC = false;
    option.CALLBACK = function(response) {
        adminJson = response;
        generateAdminList(response);
    }
    option.ERROR_CALLBACK = function(response) {
        console.log(response);
    }
    ajaxWrapper.callAjax(option);
}

function generateAdminList(data){
    $("#bulk-select-body").empty();

    var trHtml = ``;
    for(var i =0; i < data.length; i ++){
        var role = data[i].role;
        if(role === 'ROLE_SUPERADMIN'){
            role = 'SUPERADMIN';
        }
        else if(role === 'ROLE_ADMIN'){
            role = 'ADMIN';
        }
        trHtml += `<tr id="ADMIN-ID-${data[i].userId}" class="align-middle">`;
        trHtml += `<td class="align-middle white-space-nowrap">`;
        if(role === 'SUPERADMIN'){
            trHtml += `<span class="fas fa-crown ms-1"></span>`;
        }
        else if(role === 'ADMIN'){
            trHtml += `<div class="form-check mb-0 ms-1"><input id="userId-${data[i].userId}" class="form-check-input" type="checkbox" data-bulk-select-row="data-bulk-select-row"/></div>`;
        }
        trHtml += `</td>`;
        trHtml += `<td class="text-center">${data[i].nickname}</td>`;
        trHtml += `<td class="text-center">${data[i].userId}</td>`;
        trHtml += `<td class="text-center">${role}</td>`;
        trHtml += `<td class="text-center">${data[i].createdAt}</td>`;
        trHtml += `<td class="text-center">${data[i].lastLoginAt}</td>`;
        trHtml += `</tr>`;
    }

    $("#bulk-select-body").append(trHtml);
    // 벌크설렉트 이벤트 추가
    bulkSelectInit(); // 해제
    bulkSelectInit(); // 재설정
}

function saveAdmin(){
    var adminId = $("#admin-id").val();
    var adminNickname = $("#admin-nickname").val();
    var adminPw = $("#admin-pw").val();

    if( adminId == "" || adminNickname == "" || adminPw == ""){
        viewSnackbarFailed("Please enter data!");
        document.getElementById("adminCreateForm").classList.add('was-validated');
    }
    else{
        var param = {
            "adminId" : adminId,
            "adminNickname" : adminNickname,
            "adminPw" : adminPw
        };
        console.log(param);
        var option = deepExtend({}, ajaxOptions);
        option.URL = "/api/v1/admin"
        option.TYPE = "post";
        option.PARAM = JSON.stringify(param);
        option.HEADERS = getCsrfHeader();
        option.CALLBACK = function(response) {
            viewSnackbar("관리자를 생성합니다.");
            adminJson = response;
            generateAdminList(response);
            $("#admin-modal").modal("hide");
        }
        option.ERROR_CALLBACK = function(response) {
            console.log(response);
        }
        ajaxWrapper.callAjax(option);
    }
}
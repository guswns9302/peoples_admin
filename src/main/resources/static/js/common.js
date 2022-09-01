"use strict";

$(document).ready(function () {
});

/**
 * 공통 스크립트
 */
var SSEEnum = Object.freeze({
    LOG : ""
});

/*
 * SSE
 */
var SSEObject = function(){
    function start(startUrl, eventName){
        var eventSource = new EventSource(startUrl);
        eventSource.addEventListener(eventName,function(event){
            if(eventName == 'chartData'){
                var sseJosnData = JSON.parse(event.data);
                if(sseJosnData.measuredAt == null){
                    checkedPointId(sseJosnData);
                }
                else{
                    updateChart(sseJosnData);
                }
            }
            else if(eventName == 'logData'){
                generateLog(event.data);
            }
        })
        eventSource.onerror = function(){
            eventSource.close();
        }
        return eventSource;
    }

    function stop(stopUrl, eventsData){
        eventsData.close();
        new EventSource(stopUrl);
    }

    this.start = function(startUrl, eventName){
        return start(startUrl, eventName);
    }
    this.stop = function(stopUrl, events){
        return stop(stopUrl, events);
    }
}

/*
 * object copy 메소드
 */
var deepExtend = function (out) {
    out = out || {};

    for (var i = 1, len = arguments.length; i < len; ++i) {
        var obj = arguments[i];

        if (!obj) {
            continue;
        }

        for (var key in obj) {
            if (!obj.hasOwnProperty(key)) {
                continue;
            }

            // based on
            // https://javascriptweblog.wordpress.com/2011/08/08/fixing-the-javascript-typeof-operator/
            if (Object.prototype.toString.call(obj[key]) === '[object Object]') {
                out[key] = deepExtend(out[key], obj[key]);
                continue;
            }

            out[key] = obj[key];
        }
    }

    return out;
}

var ajaxWrapper = {
    /**
     * Ajax Wrapper
     *
     * Parameter Option Object
     * 필수) URL - 전송 URL (String)
     * 필수) PARAM - 전송 파라미터 (String or Object)
     * 필수) HEADERS - 전송 헤더 (Json Object)
     * 필수) CALLBACK - 콜백함수 (Function)
     * 필수) TYPE - 전송 방식 (POST, GET, PUT, DELETE)
     * 선택) DATA_TYPE - 응답받을 데이터 타입 (String)
     * 선택) CONTENT_TYPE - 전송할 데이터 타입 (String)
     * 선택) ASYNC - 비동기 여부 (boolean : 미지정시 true)
     * 선택) ERROR_CALLBACK - 전송 실패 시 콜백 함수 (Function)
     * 선택) ERRORMSG - 전송 실패시 메시지 (String)
     * 선택) RETURN_INCLUDE - 요청시 포함시키면 결과에 포함됨 (Object)
     */

    callAjax: function (options) {
        if (options.URL != null) {
            if (options.ASYNC == null) {
                options.ASYNC = true;
            }

            $.ajax({
                type: options.TYPE,
                url: options.URL,
                traditional: true,
                data: options.PARAM,
                headers: options.HEADERS,
                async: options.ASYNC,
                dataType: options.DATA_TYPE,
                contentType: options.CONTENT_TYPE,
                success: function (result) {
                    options.CALLBACK(result, options.RETURN_INCLUDE || {});
                },
                error: function (request, status, error) {
                    if (options.ERROR_CALLBACK != undefined) {
                        options.ERROR_CALLBACK(request, status, error);
                    } else {
                        console.log(request);
                        console.log(status);
                        console.log(error);
                        alert("서버에 요청중 문제가 발생했습니다.\n관리자에게 문의하여 주십시오.");
                    }
                }
            });
        } else {
            alert("올바른 요청이 아닙니다.");
            return false;
        }
    }
}

// 복사해서 사용해야 함.
var ajaxOptions = {
    URL: "",
    PARAM: null, // TYPE get 을 제외하고 JSON.stringify 처리 필요
    HEADERS: null, // CSRF 보안을 위해 필요
    CALLBACK: function (response, status, req) {
        if (response.success) {
            console.log(response);
        } else {
            alert(response);
        }
    },
    ERROR_CALLBACK: function (request, status, error) {
        console.log(request);
        console.log(status);
        console.log(error);
        console.log("code : " + status + "\n" + "message : " + request.responseText + "\n" + "error : " + error);
    },
    ASYNC: null, // 기본값 true, false 시 중복 요청 X
    TYPE: null, // 설정해줘야 함 (POST, GET, PUT, DELETE)
    DATA_TYPE: "json",
    CONTENT_TYPE: "application/json; charset=utf-8;"
}

// csrf 보안을 위한 ajax 헤더 생성용 함수
function getCsrfHeader() {
    var csrfHeaderName = $("meta[name='_csrf_header']").attr("content");
    var csrfHeaderToken = $("meta[name='_csrf']").attr("content");
    var header = {};
    header[csrfHeaderName] = csrfHeaderToken;
    return header;
}

// 중첩된 JSON을 키에 dot 연산자를 추가하여 평탄화 한다.
function flatten (obj) {
    var newObj = {};
    for (var key in obj) {
        if (typeof obj[key] === 'object' && obj[key] !== null) {
            var temp = flatten(obj[key])
            for (var key2 in temp) {
                newObj[key+"."+key2] = temp[key2];
            }
        } else {
            newObj[key] = obj[key];
        }
    }
    return newObj;
}

function numberWithCommas(num) {
    var parts = num.toString().split(".");
    return parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",") + (parts[1] ? "." + parts[1] : "");
}

function ifEmpty(value, alterValue) {
    if (value == null || value == undefined) {
        if (alterValue != null && alterValue != undefined) {
            return alterValue;
        } else {
            return "";
        }
    } else {
        return value;
    }
}

function isEmpty(value) {
    if (value == null || value == undefined || value.length == 0) {
        return true;
    } else {
        return false;
    }
}

function isJson(item) {
    item = typeof item !== "string" ? JSON.stringify(item) : item;

    try {
        item = JSON.parse(item);
    } catch (e) {
        return false;
    }

    if (typeof item === "object" && item !== null) {
        return true;
    }

    return false;
}

function floatRoundByDecimalDigit(num, digit) {
    return +(Math.round(num + "e+" + digit)  + "e-" + digit);
}

function viewSnackbar(alertText) {
    var snackbar = document.getElementById("snackbar");
    if (snackbar == null) {
        var snackArea = document.getElementsByTagName("body")[0];
        snackbar = document.createElement("div");
        snackbar.setAttribute("id", "snackbar");
        snackArea.appendChild(snackbar);
    }
    snackbar.innerText = alertText;
    snackbar.className = "show";
    setTimeout(function(){ snackbar.className = snackbar.className.replace("show", ""); }, 1900);
}
function viewSnackbarFailed(alertText) {
    var snackbarFailed = document.getElementById("snackbarFailed");
    if (snackbarFailed == null) {
        var snackArea = document.getElementsByTagName("body")[0];
        snackbarFailed = document.createElement("div");
        snackbarFailed.setAttribute("id", "snackbarFailed");
        snackArea.appendChild(snackbarFailed);
    }
    snackbarFailed.innerText = alertText;
    snackbarFailed.className = "show";
    setTimeout(function(){ snackbarFailed.className = snackbarFailed.className.replace("show", ""); }, 1900);
}

Date.prototype.yyyymmdd = function(splitter) {
    var mm = this.getMonth() + 1;
    var dd = this.getDate();
    mm = mm >= 10 ? mm : "0" + mm;
    dd = dd >= 10 ? dd : "0" + dd;
    return [this.getFullYear(), mm, dd].join(splitter);
}

Date.prototype.yyyymm = function(splitter) {
    var mm = this.getMonth() + 1;
    mm = mm >= 10 ? mm : "0" + mm;
    return [this.getFullYear(), mm].join(splitter);
}

Date.prototype.yyyy = function() {
    return this.getFullYear();
}

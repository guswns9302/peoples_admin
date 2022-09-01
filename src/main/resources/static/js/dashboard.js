$(document).ready(function() {
    $("#themeControlToggle").change(function(){
        change_theme();
        drawAUChart();
        drawPieChart();
        drawUserChart();
        drawStudyChart();
    })

    var range = $("#timepicker").val();
    if(range == ''){
        var today = new Date();
        var year = today.getFullYear();
        var month = ('0' + (today.getMonth() + 1)).slice(-2);
        var day = ('0' + today.getDate()).slice(-2);
        var dateString = year + '-' + month  + '-' + day;
        $("#timepicker").val(dateString + ' to ' + dateString);
    }

    change_theme();
    drawAUChart();
    drawPieChart();
    drawUserChart();
    drawStudyChart();

});

window.onresize = function() {
    auChart.resize();
    pieChart.resize();
    studyChart.resize();
    userChart.resize();
};

var auChart;
var pieChart;
var userChart;
var studyChart;
var chart_font_style;
var chart_axis_style;

function change_theme(){
    var theme_view = localStorage.getItem('theme');
    if(theme_view === 'dark'){
        chart_font_style = {
            color: "rgb(255,255,255)",
            fontSize: 16
        };
        chart_axis_style = {
            color: "rgb(255,255,255)",
            fontSize: 14
        };
    }
    else{
        chart_font_style = {
            color: "rgb(0,0,0)",
            fontSize: 16
        };
        chart_axis_style = {
            color: "rgb(0,0,0)",
            fontSize: 14
        };
    }
}

function drawAUChart(){
    $("#chartArea-AU").empty();
    var chartHtml = ``;
    chartHtml += `<div id="chart-au" style="min-height: 100%; min-width: 300px;" data-echart-responsive="true"></div>`;
    $("#chartArea-AU").append(chartHtml);

    var chartDom = document.getElementById('chart-au');
    auChart = echarts.init(chartDom);
    var option;

    option = {
        grid: {
            left: '2%',
            right: '2%',
            bottom: '5%',
            top: '15%',
            containLabel: true
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'cross'
            },
            backgroundColor: 'rgba(255, 255, 255, 0.8)'
        },
        legend: {
            data: ['DAU', 'MAU'],
            textStyle: chart_font_style
        },
        xAxis: {
            type: 'category',
            data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
            axisLabel: chart_axis_style,
            boundaryGap: false
        },
        yAxis: {
            type: 'value',
            splitLine: {
                show: true,
                lineStyle:{
                    color: 'rgba(142, 142, 142, 1)',
                    type: "dashed"
                }
            },
            axisLabel: chart_axis_style
        },
        series: [
            {
                data: [12, 23, 42, 58, 52, 60, 64],
                type: 'line',
                smooth: true,
                name: 'DAU'
            },
            {
                data: [12, 11, 19, 16, 0, 8, 4],
                type: 'line',
                smooth: true,
                name: 'MAU'
            }
        ]
    };
    option && auChart.setOption(option);
}

function drawPieChart(){
    $("#chartArea-pie").empty();
    var chartHtml = ``;
    chartHtml += `<div id="chart-pie" style="min-height: 100%; min-width: 300px;" data-echart-responsive="true"></div>`;
    $("#chartArea-pie").append(chartHtml);

    var chartDom = document.getElementById('chart-pie');
    pieChart = echarts.init(chartDom);
    var option;

    option = {
        tooltip: {
            trigger: 'item'
        },
        legend: {
            orient: 'vertical',
            left: 'left',
            textStyle: chart_font_style
        },
        series: [
            {
                type: 'pie',
                radius: '70%',
                data: [
                    { value: 60, name: 'Kakao' },
                    { value: 38, name: 'Naver' },
                    { value: 10, name: 'Email' }
                ],
                emphasis: {
                    focus: 'self',
                },
                label: {
                    formatter: '{b}\n{@1}명\n({d}%)',
                    textStyle: chart_axis_style
                }
            }
        ]
    };
    option && pieChart.setOption(option);
}

function drawUserChart(){
    $("#chartArea-user").empty();
    var chartHtml = ``;
    chartHtml += `<div id="chart-user" style="min-height: 100%; min-width: 300px;" data-echart-responsive="true"></div>`;
    $("#chartArea-user").append(chartHtml);

    var chartDom = document.getElementById('chart-user');
    userChart = echarts.init(chartDom);
    var option;

    option = {
        grid: {
            left: '2%',
            right: '2%',
            bottom: '5%',
            top: '15%',
            containLabel: true
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'cross'
            },
            backgroundColor: 'rgba(255, 255, 255, 0.8)'
        },
        legend: {
            data: ['누적 회원', '신규 회원'],
            textStyle: chart_font_style
        },
        xAxis: {
            type: 'category',
            data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
            axisLabel: chart_axis_style,
            boundaryGap: false
        },
        yAxis: {
            type: 'value',
            splitLine: {
                show: true,
                lineStyle:{
                    color: 'rgba(142, 142, 142, 1)',
                    type: "dashed"
                }
            },
            axisLabel: chart_axis_style
        },
        series: [
            {
                data: [12, 23, 42, 58, 52, 60, 64],
                type: 'line',
                smooth: true,
                name: '누적 회원'
            },
            {
                data: [17, 13, 62, 18, 23, 42, 62],
                type: 'line',
                smooth: true,
                name: '신규 회원'
            }
        ]
    };
    option && userChart.setOption(option);
}

function drawStudyChart(){
    $("#chartArea-study").empty();
    var chartHtml = ``;
    chartHtml += `<div id="chart-study" style="min-height: 100%; min-width: 300px;" data-echart-responsive="true"></div>`;
    $("#chartArea-study").append(chartHtml);

    var chartDom = document.getElementById('chart-study');
    studyChart = echarts.init(chartDom);
    var option;

    option = {
        grid: {
            left: '2%',
            right: '2%',
            bottom: '5%',
            top: '15%',
            containLabel: true
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'cross'
            },
            backgroundColor: 'rgba(255, 255, 255, 0.8)'
        },
        legend: {
            data: ['누적 스터디', '활성 스터디'],
            textStyle: chart_font_style
        },
        xAxis: {
            type: 'category',
            data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
            axisLabel: chart_axis_style,
            boundaryGap: false
        },
        yAxis: {
            type: 'value',
            splitLine: {
                show: true,
                lineStyle:{
                    color: 'rgba(142, 142, 142, 1)',
                    type: "dashed"
                }
            },
            axisLabel: chart_axis_style
        },
        series: [
            {
                data: [12, 23, 42, 58, 52, 60, 64],
                type: 'line',
                smooth: true,
                name: '누적 스터디'
            },
            {
                data: [2, 13, 32, 48, 22, 40, 54],
                type: 'line',
                smooth: true,
                name: '활성 스터디'
            }
        ]
    };
    option && studyChart.setOption(option);
}
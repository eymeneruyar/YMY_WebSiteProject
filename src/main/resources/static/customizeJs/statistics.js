//---------------------------------- Parameter Definition - Start ----------------------------------//
var $textMutedColor = '#b9b9c3'
var $revenueReportChart = document.querySelector('#revenue-report-chart');
var revenueReportChartOptions;
var revenueReportChart;
//---------------------------------- Parameter Definition - End ------------------------------------//

//---------------------------------- Number One Company Card - Start ----------------------------------//
function fncInfoTopPayingCompany(){
    $.ajax({
        url: "./istatistikler/infoTopPayingCompany",
        type: "GET",
        dataType: "JSON",
        contentType : 'application/json; charset=utf-8',
        //async:false,
        success: function (data) {
            fncCreateTopPayingCompanyCard(data)
            //console.log(data)
        },
        error: function (err) {
            console.log(err)
        }
    })
}
fncInfoTopPayingCompany()

function fncCreateTopPayingCompanyCard(data){
    let html = `<h5>Tebrikler 🎉 ${data.result.company.name}!</h5>
                <p class="card-text font-small-3">Altın madalyayı kazandınız!</p>
                <h3 class="mb-75 mt-2 pt-50">
                    <a href="/firma_detay/${data.result.company.id}">${data.result.total}₺</a>
                </h3>
                <a href="/firma_detay/${data.result.company.id}" class="btn btn-primary">Detay</a>
                <img src="/dashboardPage/app-assets/images/illustration/badge.svg" class="congratulation-medal" alt="Medal Pic" />`
    $('#id_statisticsTopPayingCard').html(html)
}
//---------------------------------- Number One Company Card - End ------------------------------------//

//---------------------------------- Yearly Goal Overview - Start ------------------------------------//
function fncInfoYearlyGoalOverview(){
    var output
    $.ajax({
        url: "./istatistikler/infoYearlyGoalOverview",
        type: "GET",
        dataType: "JSON",
        contentType : 'application/json; charset=utf-8',
        async:false,
        success: function (data) {
            output = data
            console.log(data)
        },
        error: function (err) {
            console.log(err)
        }
    })
    return output
}

function fncDrawYearlyGoalOverviewCard(){
    var $goalStrokeColor2 = '#51e5a8';
    var $strokeColor = '#ebe9f1';
    var $textHeadingColor = '#5e5873';
    var $goalOverviewChart = document.querySelector('#goal-overview-radial-bar-chart');
    var goalOverviewChartOptions;
    var goalOverviewChart;

    var data = fncInfoYearlyGoalOverview()

    $('#id_statisticsYearlyGoalOverviewFinished').text(data.result.profit + ' ₺')
    $('#id_statisticsYearlyGoalOverviewGoal').text(data.result.yearlyGoal + ' ₺')

    goalOverviewChartOptions = {
        chart: {
            height: 245,
            type: 'radialBar',
            sparkline: {
                enabled: true
            },
            dropShadow: {
                enabled: true,
                blur: 3,
                left: 1,
                top: 1,
                opacity: 0.1
            }
        },
        colors: [$goalStrokeColor2],
        plotOptions: {
            radialBar: {
                offsetY: -10,
                startAngle: -150,
                endAngle: 150,
                hollow: {
                    size: '77%'
                },
                track: {
                    background: $strokeColor,
                    strokeWidth: '50%'
                },
                dataLabels: {
                    name: {
                        show: false
                    },
                    value: {
                        color: $textHeadingColor,
                        fontSize: '2.86rem',
                        fontWeight: '600'
                    }
                }
            }
        },
        fill: {
            type: 'gradient',
            gradient: {
                shade: 'dark',
                type: 'horizontal',
                shadeIntensity: 0.5,
                gradientToColors: [window.colors.solid.success],
                inverseColors: true,
                opacityFrom: 1,
                opacityTo: 1,
                stops: [0, 100]
            }
        },
        series: [data.result.percentage],
        stroke: {
            lineCap: 'round'
        },
        grid: {
            padding: {
                bottom: 30
            }
        }
    };
    goalOverviewChart = new ApexCharts($goalOverviewChart, goalOverviewChartOptions);
    goalOverviewChart.render();
}
fncDrawYearlyGoalOverviewCard()
//---------------------------------- Yearly Goal Overview - End --------------------------------------//

//---------------------------------- Revenue Report Card - Start ------------------------------------//
function fncOptionRevenueReportYears(){
    $("#id_statisticsListYear").empty()
    for (let i = 0; i < 10; i++) {
        $("#id_statisticsListYear").append('<option value='+(2021+i)+'>'+(2021+i)+'</option>')
    }
}
fncOptionRevenueReportYears()

$("#id_statisticsListYear").change(function (){
    revenueReportChart.destroy();
    fncInfoRevenueReport($(this).val())
})

function fncInfoRevenueReport(year){
    $.ajax({
        url: "./istatistikler/infoRevenueReport/" + year,
        type: "GET",
        dataType: "JSON",
        contentType : 'application/json; charset=utf-8',
        //async:false,
        success: function (data) {
            fncCreateRevenueReportCard(data)
            console.log(data)
        },
        error: function (err) {
            console.log(err)
        }
    })
}
fncInfoRevenueReport($("#id_statisticsListYear").val())

function fncCreateRevenueReportCard(data){

    //Earning,expense and profit variable definition
    let year = $("#id_statisticsListYear").val()
    let earningArr = [0,0,0,0,0,0,0,0,0,0,0,0]
    let expenseArr = [0,0,0,0,0,0,0,0,0,0,0,0]
    let profit = 0
    let earning = 0
    let expense = 0

    data.result.forEach(it => {
        //Box actions input
        if(it.description == 1){
            if(it.transactionDate.startsWith(year + '-01')){
                earningArr[0] += it.amount
            }else if(it.transactionDate.startsWith(year + '-02')){
                earningArr[1] += it.amount
            }else if(it.transactionDate.startsWith(year + '-03')){
                earningArr[2] += it.amount
            }else if(it.transactionDate.startsWith(year + '-04')){
                earningArr[3] += it.amount
            }else if(it.transactionDate.startsWith(year + '-05')){
                earningArr[4] += it.amount
            }else if(it.transactionDate.startsWith(year + '-06')){
                earningArr[5] += it.amount
            }else if(it.transactionDate.startsWith(year + '-07')){
                earningArr[6] += it.amount
            }else if(it.transactionDate.startsWith(year + '-08')){
                earningArr[7] += it.amount
            }else if(it.transactionDate.startsWith(year + '-09')){
                earningArr[8] += it.amount
            }else if(it.transactionDate.startsWith(year + '-10')){
                earningArr[9] += it.amount
            }else if(it.transactionDate.startsWith(year + '-11')){
                earningArr[10] += it.amount
            }else if(it.transactionDate.startsWith(year + '-12')){
                earningArr[11] += it.amount
            }
        }else{
            if(it.transactionDate.startsWith(year + '-01')){
                expenseArr[0] -= it.amount
            }else if(it.transactionDate.startsWith(year + '-02')){
                expenseArr[1] -= it.amount
            }else if(it.transactionDate.startsWith(year + '-03')){
                expenseArr[2] -= it.amount
            }else if(it.transactionDate.startsWith(year + '-04')){
                expenseArr[3] -= it.amount
            }else if(it.transactionDate.startsWith(year + '-05')){
                expenseArr[4] -= it.amount
            }else if(it.transactionDate.startsWith(year + '-06')){
                expenseArr[5] -= it.amount
            }else if(it.transactionDate.startsWith(year + '-07')){
                expenseArr[6] -= it.amount
            }else if(it.transactionDate.startsWith(year + '-08')){
                expenseArr[7] -= it.amount
            }else if(it.transactionDate.startsWith(year + '-09')){
                expenseArr[8] -= it.amount
            }else if(it.transactionDate.startsWith(year + '-10')){
                expenseArr[9] -= it.amount
            }else if(it.transactionDate.startsWith(year + '-11')){
                expenseArr[10] -= it.amount
            }else if(it.transactionDate.startsWith(year + '-12')){
                expenseArr[11] -= it.amount
            }
        }
    })

    earning = earningArr.reduce((sum,a) => sum + a, 0)
    expense = expenseArr.reduce((sum,a) => sum + a, 0)
    profit = earning + expense //Expense is negative number

    $('#id_statisticsEarningRevenueReport').text(earning + '₺')
    $('#id_statisticsExpenseRevenueReport').text(expense*-1 + '₺')
    $('#id_statisticsProfitRevenueReport').text(profit + '₺')

    revenueReportChartOptions = {
        chart: {
            height: 230,
            stacked: true,
            type: 'bar',
            toolbar: { show: true }
        },
        plotOptions: {
            bar: {
                columnWidth: '17%',
                endingShape: 'rounded'
            },
            distributed: true
        },
        colors: [window.colors.solid.primary, window.colors.solid.warning],
        series: [
            {
                name: 'Gelir',
                data: earningArr
            },
            {
                name: 'Gider',
                data: expenseArr
            }
        ],
        dataLabels: {
            enabled: false
        },
        legend: {
            show: false
        },
        grid: {
            padding: {
                top: -20,
                bottom: -10
            },
            yaxis: {
                lines: { show: false }
            }
        },
        xaxis: {
            categories: ['Oca', 'Şub', 'Mar', 'Nis', 'May', 'Haz', 'Tem', 'Ağu', 'Eyl','Eki','Kas','Ara'],
            labels: {
                style: {
                    colors: $textMutedColor,
                    fontSize: '0.86rem'
                }
            },
            axisTicks: {
                show: false
            },
            axisBorder: {
                show: false
            }
        },
        yaxis: {
            labels: {
                style: {
                    colors: $textMutedColor,
                    fontSize: '0.86rem'
                }
            }
        }
    };
    revenueReportChart = new ApexCharts($revenueReportChart, revenueReportChartOptions);
    revenueReportChart.render();
}
//---------------------------------- Revenue Report Card - End --------------------------------------//



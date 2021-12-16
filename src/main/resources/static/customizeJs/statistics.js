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
                    <a href="javascript:void(0);">${data.result.total}₺</a>
                </h3>
                <button type="button" class="btn btn-primary">Detay</button>
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
function fncInfoRevenueReport(){

}

function fncCreateRevenueReportCard(){

    var $textMutedColor = '#b9b9c3'
    var $revenueReportChart = document.querySelector('#revenue-report-chart');
    var revenueReportChartOptions;
    var revenueReportChart;

    revenueReportChartOptions = {
        chart: {
            height: 230,
            stacked: true,
            type: 'bar',
            toolbar: { show: false }
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
                name: 'Earning',
                data: [95, 177, 284, 256, 105, 63, 168, 218, 72]
            },
            {
                name: 'Expense',
                data: [-145, -80, -60, -180, -100, -60, -85, -75, -100]
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
            categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep'],
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
fncCreateRevenueReportCard()
//---------------------------------- Revenue Report Card - End --------------------------------------//



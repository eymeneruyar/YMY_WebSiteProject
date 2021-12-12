//---------------------------------- Earning Chart - Start ----------------------------------//
function fncInfoMontlyEarning(){
    var output
    $.ajax({
        url: "./yonetim/infoMontlyEarning",
        type: "GET",
        dataType: "JSON",
        contentType : 'application/json; charset=utf-8',
        async:false,
        success: function (data) {
            output = data
            //console.log(data)
        },
        error: function (err) {
            console.log(err)
        }
    })
    return output
}

function fncDrawEarningChart(){

    var $goalStrokeColor2 = '#51e5a8';
    var $earningsStrokeColor2 = '#28c76f66'
    var $earningsStrokeColor3 = '#28c76f33'
    var $earningsChart = document.querySelector('#earnings-chart')
    var earningsChartOptions
    var earningsChart
    var data = fncInfoMontlyEarning()

    $('#id_dashboardEarningChartProfit').text(data.result.profit + ' ₺')

    earningsChartOptions = {
        chart: {
            type: 'donut',
            height: 150,
            toolbar: {
                show: false
            }
        },
        dataLabels: {
            enabled: false
        },
        series: [data.result.earning, data.result.expense, data.result.profit],
        legend: { show: false },
        comparedResult: [2, -3, 8],
        labels: ['Gelir', 'Gider', 'Net Kar'],
        stroke: { width: 0 },
        colors: [$earningsStrokeColor2, $earningsStrokeColor3, window.colors.solid.success],
        grid: {
            padding: {
                right: -20,
                bottom: -8,
                left: -20
            }
        },
        plotOptions: {
            pie: {
                startAngle: -10,
                donut: {
                    labels: {
                        show: true,
                        name: {
                            offsetY: 15
                        },
                        value: {
                            offsetY: -15,
                            formatter: function (val) {
                                return parseInt(val) + '₺';
                            }
                        },
                        total: {
                            show: true,
                            offsetY: 15,
                            label: 'Gelir',
                            formatter: function (w) {
                                return data.result.earning + '₺';
                            }
                        }
                    }
                }
            }
        },
        responsive: [
            {
                breakpoint: 1325,
                options: {
                    chart: {
                        height: 100
                    }
                }
            },
            {
                breakpoint: 1200,
                options: {
                    chart: {
                        height: 120
                    }
                }
            },
            {
                breakpoint: 1045,
                options: {
                    chart: {
                        height: 100
                    }
                }
            },
            {
                breakpoint: 992,
                options: {
                    chart: {
                        height: 120
                    }
                }
            }
        ]
    };
    earningsChart = new ApexCharts($earningsChart, earningsChartOptions);
    earningsChart.render();

}
fncDrawEarningChart()
//---------------------------------- Earning Chart - End ------------------------------------//

//---------------------------------- General Statistics Card - Start ------------------------------------//
function fncInfoGeneralStatistics(){
    $.ajax({
        url: "./yonetim/infoGeneralStatistics",
        type: "GET",
        dataType: "JSON",
        contentType : 'application/json; charset=utf-8',
        //async:false,
        success: function (data) {
            $('#id_dashboardGeneralStatisticsCompany').text(data.result.totalCompany)
            $('#id_dashboardGeneralStatisticsCustomer').text(data.result.totalCustomer)
            $('#id_dashboardGeneralStatisticsWork').text(data.result.totalWork)
        },
        error: function (err) {
            console.log(err)
        }
    })
}
fncInfoGeneralStatistics()
//---------------------------------- General Statistics Card - End --------------------------------------//

//---------------------------------- Monthly Goal Overview Chart - Start --------------------------------------//
//---------------------------------- Monthly Goal Overview Chart - End ----------------------------------------//

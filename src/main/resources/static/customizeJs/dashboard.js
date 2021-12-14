//--------------------------------------- Definition of Days and Months - Start ---------------------------------------//
const months = ['Ocak', 'Şubat', 'Mart', 'Nisan', 'Mayıs', 'Haziran',
    'Temmuz', 'Ağustos', 'Eylül', 'Ekim', 'Kasım', 'Aralık'
]

const days_shorted = ['Pzr', 'Pzrts', 'Sal', 'Çarş', 'Perş', 'Cum', 'Cmrts']
const days = ['Pazar', 'Pazartesi', 'Salı', 'Çarşamba', 'Perşembe', 'Cuma', 'Cumartesi']
//--------------------------------------- Definition of Days and Months - End -----------------------------------------//

//---------------------------------- Data tables Definitions - Start ----------------------------------//
function dataTableCompany(){
    $("#id_dashboardTableCompany").DataTable( {

        order: [[2, 'desc']],
        dom:
            '<"card-header border-bottom p-1"<"head-label-company"><"dt-action-buttons text-right"B>><"d-flex justify-content-between align-items-center mx-0 row"<"col-sm-12 col-md-6"l><"col-sm-12 col-md-6"f>>t<"d-flex justify-content-between mx-0 row"<"col-sm-12 col-md-6"i><"col-sm-12 col-md-6"p>>',
        displayLength: 3,
        "bInfo": false, //Closed show total data
        "bLengthChange": false, //Closed show entries
        buttons: [
            {
                extend: 'collection',
                className: 'btn btn-outline-secondary dropdown-toggle mr-2',
                text: feather.icons['share'].toSvg({ class: 'font-small-4 mr-50' }) + 'Dışa Aktar',
                buttons: [
                    {
                        extend: 'print',
                        text: feather.icons['printer'].toSvg({ class: 'font-small-4 mr-50' }) + 'Yazdır',
                        className: 'dropdown-item',
                        exportOptions: { columns: [0,1,2,3,4] }
                    },
                    {
                        extend: 'csv',
                        text: feather.icons['file-text'].toSvg({ class: 'font-small-4 mr-50' }) + 'Csv',
                        className: 'dropdown-item',
                        exportOptions: { columns: [0,1,2,3,4] }
                    },
                    {
                        extend: 'excel',
                        text: feather.icons['file'].toSvg({ class: 'font-small-4 mr-50' }) + 'Excel',
                        className: 'dropdown-item',
                        exportOptions: { columns: [0,1,2,3,4] }
                    },
                    {
                        extend: 'pdf',
                        text: feather.icons['clipboard'].toSvg({ class: 'font-small-4 mr-50' }) + 'Pdf',
                        className: 'dropdown-item',
                        exportOptions: { columns: [0,1,2,3,4] }
                    },
                    {
                        extend: 'copy',
                        text: feather.icons['copy'].toSvg({ class: 'font-small-4 mr-50' }) + 'Kopyala',
                        className: 'dropdown-item',
                        exportOptions: { columns: [0,1,2,3,4] }
                    }
                ],
            }
        ],
        language: {
            search: 'Ara',
            searchPlaceholder: 'Ara',
            paginate: {
                // remove previous & next text from pagination
                previous: '&nbsp;',
                next: '&nbsp;'
            }
        }
    } );
    $('div.head-label-company').html('<h2 class="mb-0">Firma Borçları</h2>');
}
dataTableCompany()

function dataTableCustomer(){
    $("#id_dashboardTableCustomer").DataTable( {

        order: [[2, 'desc']],
        dom:
            '<"card-header border-bottom p-1"<"head-label-customer"><"dt-action-buttons text-right"B>><"d-flex justify-content-between align-items-center mx-0 row"<"col-sm-12 col-md-6"l><"col-sm-12 col-md-6"f>>t<"d-flex justify-content-between mx-0 row"<"col-sm-12 col-md-6"i><"col-sm-12 col-md-6"p>>',
        displayLength: 3,
        "bInfo": false, //Closed show total data
        "bLengthChange": false, //Closed show entries
        buttons: [
            {
                extend: 'collection',
                className: 'btn btn-outline-secondary dropdown-toggle mr-2',
                text: feather.icons['share'].toSvg({ class: 'font-small-4 mr-50' }) + 'Dışa Aktar',
                buttons: [
                    {
                        extend: 'print',
                        text: feather.icons['printer'].toSvg({ class: 'font-small-4 mr-50' }) + 'Yazdır',
                        className: 'dropdown-item',
                        exportOptions: { columns: [0,1,2,3,4] }
                    },
                    {
                        extend: 'csv',
                        text: feather.icons['file-text'].toSvg({ class: 'font-small-4 mr-50' }) + 'Csv',
                        className: 'dropdown-item',
                        exportOptions: { columns: [0,1,2,3,4] }
                    },
                    {
                        extend: 'excel',
                        text: feather.icons['file'].toSvg({ class: 'font-small-4 mr-50' }) + 'Excel',
                        className: 'dropdown-item',
                        exportOptions: { columns: [0,1,2,3,4] }
                    },
                    {
                        extend: 'pdf',
                        text: feather.icons['clipboard'].toSvg({ class: 'font-small-4 mr-50' }) + 'Pdf',
                        className: 'dropdown-item',
                        exportOptions: { columns: [0,1,2,3,4] }
                    },
                    {
                        extend: 'copy',
                        text: feather.icons['copy'].toSvg({ class: 'font-small-4 mr-50' }) + 'Kopyala',
                        className: 'dropdown-item',
                        exportOptions: { columns: [0,1,2,3,4] }
                    }
                ],
            }
        ],
        language: {
            search: 'Ara',
            searchPlaceholder: 'Ödeme Ara',
            paginate: {
                // remove previous & next text from pagination
                previous: '&nbsp;',
                next: '&nbsp;'
            }
        }
    } );
    $('div.head-label-customer').html('<h2 class="mb-0">Borçlu Müşteriler</h2>');
}
dataTableCustomer()
//---------------------------------- Data tables Definitions - End ------------------------------------//

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

//---------------------------------- Debtor Companies List - Start --------------------------------------//
function fncListDebtorCompanies(){
    $.ajax({
        url: "./yonetim/listDebtorCompanies",
        type: "GET",
        dataType: "JSON",
        contentType : 'application/json; charset=utf-8',
        //async:false,
        success: function (data) {
            if($.fn.DataTable.isDataTable("#id_dashboardTableCompany")){
                $("#id_dashboardTableCompany").DataTable().destroy()
            }
            fncCreateRowDataTableCompany(data)
            dataTableCompany()
            //console.log(data)
        },
        error: function (err) {
            console.log(err)
        }
    })
}
fncListDebtorCompanies()

function fncCreateRowDataTableCompany(data){
    let html = ``
    let totalDebt = 0
    //console.log(data)
    data.result.companyList.forEach(item => {
        totalDebt = data.result[item.id]
        html += `<tr role="row" class="odd">
                    <td><a href="/fatura/${item.id}">${item.name}</a></td>
                    <td>${item.authorisedPerson}</td>
                    <td>${item.phone}</td>
                    <td>${totalDebt}</td>
                    <td>${item.date}</td>
                    <td class="text-left">
                        <div class="dropdown">
                            <button type="button" class="btn btn-sm dropdown-toggle hide-arrow" data-toggle="dropdown">
                                <i class="fas fa-ellipsis-v"></i>
                            </button>
                            <div class="dropdown-menu">
                                <a class="dropdown-item" href="/kasa_haraketleri">
                                    <i class="far fa-credit-card"></i>
                                    <span>Ödeme Yap</span>
                                </a>
                                <a class="dropdown-item" href="javascript:void(0);">
                                    <i class="fas fa-info-circle"></i>
                                    <span>Detay</span>
                                </a>
                            </div>
                        </div>
                    </td>`
    })
    $('#id_dashboardTableRowCompany').html(html)
}
//---------------------------------- Debtor Companies List - End ----------------------------------------//

//---------------------------------- Debtor Customers List - Start --------------------------------------//
function fncListDebtorCustomer(){
    $.ajax({
        url: "./yonetim/listDebtorCustomers",
        type: "GET",
        dataType: "JSON",
        contentType : 'application/json; charset=utf-8',
        //async:false,
        success: function (data) {
            if($.fn.DataTable.isDataTable("#id_dashboardTableCustomer")){
                $("#id_dashboardTableCustomer").DataTable().destroy()
            }
            fncCreateRowDataTableCustomer(data)
            dataTableCustomer()
            //console.log(data)
        },
        error: function (err) {
            console.log(err)
        }
    })
}
fncListDebtorCustomer()

function fncCreateRowDataTableCustomer(data){
    let html = ``
    data.result.forEach(item => {
        html += `<tr role="row" class="odd">
                    <td><a href="/fatura/${item.id}">${item.invoiceCode}</a></td>
                    <td>${item.company.name}</td>
                    <td>${item.customer.plate}</td>
                    <td>${item.customer.phone}</td>
                    <td>${item.remainingDebt}</td>
                    <td class="text-left">
                        <div class="dropdown">
                            <button type="button" class="btn btn-sm dropdown-toggle hide-arrow" data-toggle="dropdown">
                                <i class="fas fa-ellipsis-v"></i>
                            </button>
                            <div class="dropdown-menu">
                                <a class="dropdown-item" href="/kasa_haraketleri">
                                    <i class="far fa-credit-card"></i>
                                    <span>Ödeme Yap</span>
                                </a>
                                <a class="dropdown-item" href="javascript:void(0);">
                                    <i class="fas fa-info-circle"></i>
                                    <span>Detay</span>
                                </a>
                            </div>
                        </div>
                    </td>`
    })
    $('#id_dashboardTableRowCustomer').html(html)
}
//---------------------------------- Debtor Customers List - End ----------------------------------------//

//---------------------------------- Agenda Note Card - Start ----------------------------------------//
function fncPaginationAgendaNoteCard(totalPage){
    if(totalPage > 0){
        $('#id_dashboardAgendaNoteCardPagination').twbsPagination({
            totalPages: totalPage,
            visiblePages: 5,
            prev: ' ',
            first: false,
            last: false,
            next: ' ',
            startPage: 1,
            onPageClick: function (event, page) {
                fncAgendaNoteCard(page-1)
                $('.pagination').find('li').addClass('page-item');
                $('.pagination').find('a').addClass('page-link');
            }
        });
    }
}

function fncAgendaNoteCard(pageNo){
    var returnData
    $.ajax({
        url: './yonetim/infoAgendaNoteCard/' + pageNo,
        type: 'GET',
        dataType: "JSON",
        contentType : 'application/json; charset=utf-8',
        success: function (data) {
            if(data.status === true){
                if(data.result.agendaList.length > 0){
                    fncPaginationAgendaNoteCard(data.result.totalPage)
                    fncCreateAgendaNoteCard(data.result.agendaList)
                }
            }

            returnData = data.result
        },
        error: function (err) {
            Swal.fire({
                title: "Error!",
                text: "An error occurred during the daily announcment listing operation!",
                icon: "error",
                customClass: {
                    confirmButton: 'btn btn-primary'
                },
                buttonsStyling: false
            });
            console.log(err)
        }
    })
    return returnData

}
fncAgendaNoteCard(0)

function fncCreateAgendaNoteCard(data){
    let date = new Date(data[0].reminderDate)
    let formatDate = fncConvertDate(data[0].reminderDate)
    let html = ``
    if(data.length > 0){
        html = `<div class="meetup-header d-flex align-items-center">
                <div class="meetup-day">
                    <h6 class="mb-0">${days[date.getDay()]}</h6>
                    <h3 class="mb-0">${date.getDate()}</h3>
                </div>
                <div class="my-auto">
                    <h4 class="card-title mb-25">${data[0].title}</h4>
                </div>
            </div>
            <div class="media">
                <div class="media-body">
                    <p class="card-text mb-0">${data[0].note}</p>
                </div>
            </div>
            <div class="media">
                <div class="avatar bg-light-primary rounded mr-1">
                    <div class="avatar-content">
                        <i class="fas fa-calendar-week avatar-icon font-medium-3"></i>
                    </div>
                </div>
                <div class="media-body">
                    <h6 class="mb-0">${formatDate.split("-")[0]} ${months[date.getMonth()]} ${days[date.getDay()]}, ${formatDate.split("-")[2]}</h6>
                </div>
            </div>`
        $("#id_dashboardAgendaNoteCardContent").html(html)
    }
}
//---------------------------------- Agenda Note Card - End ------------------------------------------//

//---------------------------------- Monthly Goal Overview Chart - Start --------------------------------------//
function fncInfoMonthlyGoalOverview(){
    var output
    $.ajax({
        url: "./yonetim/infoMonthlyGoalOverview",
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

function fncDrawMonthlyGoalOverviewChart(){

    var $goalStrokeColor2 = '#51e5a8';
    var $strokeColor = '#ebe9f1';
    var $textHeadingColor = '#5e5873';
    var $goalOverviewChart = document.querySelector('#goal-overview-radial-bar-chart');
    var goalOverviewChartOptions;
    var goalOverviewChart;

    var data = fncInfoMonthlyGoalOverview()

    $('#id_dashboardMonthlyGoalOverviewFinished').text(data.result.profit + ' ₺')
    $('#id_dashboardMonthlyGoalOverviewGoal').text(data.result.monthlyGoal + ' ₺')


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
fncDrawMonthlyGoalOverviewChart()
//---------------------------------- Monthly Goal Overview Chart - End ----------------------------------------//

//-------------------------------------- Date Convert - Start ------------------------------------------//
function fncConvertDate(date){
    let arr = date.split("-")
    return arr[2] + "-" + arr[1] + "-" + arr[0]
}
//-------------------------------------- Date Convert - End --------------------------------------------//


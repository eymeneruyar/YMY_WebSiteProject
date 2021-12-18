//-------------------------------------- Data Table Configuration - Start --------------------------------------//
function dataTable() {
    $("#id_companyDetailTable").DataTable( {

        order: [[2, 'desc']],
        dom:
            '<"card-header border-bottom p-1"<"head-label"><"dt-action-buttons text-right"B>><"d-flex justify-content-between align-items-center mx-0 row"<"col-sm-12 col-md-6"l><"col-sm-12 col-md-6"f>>t<"d-flex justify-content-between mx-0 row"<"col-sm-12 col-md-6"i><"col-sm-12 col-md-6"p>>',
        displayLength: 5,
        lengthMenu: [5, 10, 25, 50, 75, 100],
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
                        exportOptions: { columns: [0,1,2,3,4,5,6] }
                    },
                    {
                        extend: 'csv',
                        text: feather.icons['file-text'].toSvg({ class: 'font-small-4 mr-50' }) + 'Csv',
                        className: 'dropdown-item',
                        exportOptions: { columns: [0,1,2,3,4,5,6] }
                    },
                    {
                        extend: 'excel',
                        text: feather.icons['file'].toSvg({ class: 'font-small-4 mr-50' }) + 'Excel',
                        className: 'dropdown-item',
                        exportOptions: { columns: [0,1,2,3,4,5,6] }
                    },
                    {
                        extend: 'pdf',
                        text: feather.icons['clipboard'].toSvg({ class: 'font-small-4 mr-50' }) + 'Pdf',
                        className: 'dropdown-item',
                        exportOptions: { columns: [0,1,2,3,4,5,6] }
                    },
                    {
                        extend: 'copy',
                        text: feather.icons['copy'].toSvg({ class: 'font-small-4 mr-50' }) + 'Kopyala',
                        className: 'dropdown-item',
                        exportOptions: { columns: [0,1,2,3,4,5,6] }
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
}
dataTable()
//-------------------------------------- Data Table Configuration - End ----------------------------------------//

let arr = window.location.pathname.split("/")
let company_id = arr[2]

//-------------------------------------- Açılıştaki Select Durumları - Start ----------------------------------------//
$('#id_companyDetailListFilterPaidStatus').attr("disabled", true);
$('#id_companyDetailListFilterBillingStatus').attr("disabled", true);

function fncOptionPaidStatus(){
    $("#id_companyDetailListFilterPaidStatus").empty()
    $("#id_companyDetailListFilterPaidStatus").append('<option value=null>Lütfen bir seçim yapınız</option>')
    $("#id_companyDetailListFilterPaidStatus").append('<option value="2">Tümü</option>')
    $("#id_companyDetailListFilterPaidStatus").append('<option value="1">Var</option>')
    $("#id_companyDetailListFilterPaidStatus").append('<option value="0">Yok</option>')
}
fncOptionPaidStatus()

function fncOptionBillingStatus(){
    $("#id_companyDetailListFilterBillingStatus").empty()
    $("#id_companyDetailListFilterBillingStatus").append('<option value=null>Lütfen bir seçim yapınız</option>')
    $("#id_companyDetailListFilterBillingStatus").append('<option value="2">Tümü</option>')
    $("#id_companyDetailListFilterBillingStatus").append('<option value="1">Var</option>')
    $("#id_companyDetailListFilterBillingStatus").append('<option value="0">Yok</option>')
}
fncOptionBillingStatus()
//-------------------------------------- Açılıştaki Select Durumları - End ------------------------------------------//

//-------------------------------------- Filter Criteria - Start ------------------------------------------//
$('#id_companyDetailListFilterDate').change(function () {
    if($('#id_companyDetailListFilterDate').val() != null){
        $('#id_companyDetailListFilterPaidStatus').attr("disabled", false);
    }
})

$('#id_companyDetailListFilterPaidStatus').change(function () {
    if($('#id_companyDetailListFilterPaidStatus').val() != null){
        $('#id_companyDetailListFilterBillingStatus').attr("disabled", false);
    }
})
//-------------------------------------- Filter Criteria - End --------------------------------------------//

//-------------------------------------- List of All Invoice by Selected Company - Start ----------------------------------------//
function fncListAllInvoiceBySelectedCompany(){
    $.ajax({
        url: "./listAllInvoiceBySelectedCompany/" + company_id,
        type: "GET",
        dataType: "JSON",
        contentType : 'application/json; charset=utf-8',
        //async:false,
        success: function (data) {
            console.log(data)
            if($.fn.DataTable.isDataTable("#id_companyDetailTable")){
                $("#id_companyDetailTable").DataTable().destroy()
            }
            fncCreateRowDataTable(data)
            dataTable()
            //Data table title
            $('div.head-label').html('<h2 class="mb-0">'+data.result[0].company.name+'</h2>');
        },
        error: function (err) {
            console.log(err)
        }
    })
}
fncListAllInvoiceBySelectedCompany()

//-------------------------------------- List of All Invoice by Selected Company - End ------------------------------------------//
function fncListInvoiceByFilter(date,debtStatus,billingStatus){
    $.ajax({
        url: "./listInvoiceByFilter/" + company_id + '/' + date + '/' + debtStatus + '/' + billingStatus,
        type: "GET",
        dataType: "JSON",
        contentType : 'application/json; charset=utf-8',
        //async:false,
        success: function (data) {
            console.log(data)
            if($.fn.DataTable.isDataTable("#id_companyDetailTable")){
                $("#id_companyDetailTable").DataTable().destroy()
            }
            fncCreateRowDataTable(data)
            dataTable()
            //Data table title
            $('div.head-label').html('<h2 class="mb-0">'+data.result[0].company.name+'</h2>');
        },
        error: function (err) {
            console.log(err)
        }
    })
}

function fncFilterApply(){

    var date = $('#id_companyDetailListFilterDate').val();
    var debtStatus = $('#id_companyDetailListFilterPaidStatus').val()
    var billingStatus = $('#id_companyDetailListFilterBillingStatus').val()

    console.log(date)
    console.log(debtStatus)
    console.log(billingStatus)

    if(date == "null" || debtStatus == "null" || billingStatus == "null" || date == '' || debtStatus == '' || billingStatus == ''){
        Swal.fire({
            title: 'Uyarı!',
            text: "Lütfen tüm filtre alanlarını doldurunuz!",
            icon: "warning",
            customClass: {
                confirmButton: 'btn btn-primary'
            },
            buttonsStyling: false
        });
    }else if(date.includes("to") == false){
        Swal.fire({
            title: 'Uyarı!',
            text: "Lütfen bir tarih aralığı seçiniz!",
            icon: "warning",
            customClass: {
                confirmButton: 'btn btn-primary'
            },
            buttonsStyling: false
        });
    }else{
        fncListInvoiceByFilter(date,debtStatus,billingStatus)
    }

}

//-------------------------------------- Create Row Datatable - Start ------------------------------------------//
function fncCreateRowDataTable(data){
    let html = ``
    let htmlFoot = ``
    let totalDebt = 0
    let totalPaid = 0
    let totalRemainingDebt = 0
    if(data.result.length > 0){
        data.result.forEach(item => {
            totalDebt += item.debt
            totalPaid += item.paid
            totalRemainingDebt += item.remainingDebt
            converDate = fncConvertDate(item.date)
            let billingStatus = item.billingStatus
            if(billingStatus != null && billingStatus === "1"){
                billingStatus = "Evet"
            }else if(billingStatus != null && billingStatus === "0"){
                billingStatus = "Hayır"
            }
            if(item.paidStatus == true){ //Borcu yoksa
                html += `<tr style="background-color: #e3f6e3;" role="row" class="odd">
                    <td><a href="/fatura/${item.id}">${item.invoiceCode}</a></td>
                    <td>${item.customer.plate}</td>
                    <td>${billingStatus}</td>
                    <td>${item.debt}</td>
                    <td>${item.paid}</td>
                    <td>${item.remainingDebt}</td>
                    <td>${converDate}</td>
                    <td class="text-left">
                        <div class="dropdown">
                            <button disabled type="button" class="btn btn-sm dropdown-toggle hide-arrow" data-toggle="dropdown">
                                <i class="fas fa-ellipsis-v"></i>
                            </button>
                            <div class="dropdown-menu">
                                <a class="dropdown-item" href="/kasa_haraketleri">
                                    <i class="far fa-credit-card"></i>
                                    <span>Ödeme Yap</span>
                                </a>
                            </div>
                        </div>
                    </td>`
            }else{ //Borcu varsa
                html += `<tr style="background-color: #fae8e8;" role="row" class="odd">
                    <td><a href="/fatura/${item.id}">${item.invoiceCode}</a></td>
                    <td>${item.customer.plate}</td>
                    <td>${billingStatus}</td>
                    <td>${item.debt}</td>
                    <td>${item.paid}</td>
                    <td>${item.remainingDebt}</td>
                    <td>${converDate}</td>
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
                            </div>
                        </div>
                    </td>`
            }

        })
    }

    htmlFoot += `<tr>
                     <th></th>
                     <th></th>
                     <th></th>
                     <th>${totalDebt}</th>
                     <th>${totalPaid}</th>
                     <th>${totalRemainingDebt}</th>
                     <th></th>
                     <th></th>
                 </tr>`

    $('#id_companyDetailTableRow').html(html)
    $('#id_companyDetailTableRowFoot').html(htmlFoot)
}
//-------------------------------------- Create Row Datatable - End --------------------------------------------//

//-------------------------------------- Reset Form - Start ------------------------------------------//
function fncFilterReset(){
    $('#id_companyDetailListFilterDate').val("")
    fncOptionPaidStatus()
    fncOptionBillingStatus()
    $('#id_companyDetailListFilterPaidStatus').attr("disabled", true);
    $('#id_companyDetailListFilterBillingStatus').attr("disabled", true);
    fncListAllInvoiceBySelectedCompany()
}
//-------------------------------------- Reset Form - End --------------------------------------------//

//-------------------------------------- Date Convert - Start ------------------------------------------//
function fncConvertDate(date){
    let arr = date.split("-")
    return arr[2] + "-" + arr[1] + "-" + arr[0]
}
//-------------------------------------- Date Convert - End --------------------------------------------//

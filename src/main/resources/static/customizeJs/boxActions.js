//-------------------------------------- Data Table Configuration - Start --------------------------------------//
function dataTable() {
    $("#id_boxActionsTable").DataTable( {

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
                        exportOptions: { columns: [1,2,3,4,5,6,7,8] }
                    },
                    {
                        extend: 'csv',
                        text: feather.icons['file-text'].toSvg({ class: 'font-small-4 mr-50' }) + 'Csv',
                        className: 'dropdown-item',
                        exportOptions: { columns: [1,2,3,4,5,6,7,8] }
                    },
                    {
                        extend: 'excel',
                        text: feather.icons['file'].toSvg({ class: 'font-small-4 mr-50' }) + 'Excel',
                        className: 'dropdown-item',
                        exportOptions: { columns: [1,2,3,4,5,6,7,8] }
                    },
                    {
                        extend: 'pdf',
                        text: feather.icons['clipboard'].toSvg({ class: 'font-small-4 mr-50' }) + 'Pdf',
                        className: 'dropdown-item',
                        exportOptions: { columns: [1,2,3,4,5,6,7,8] }
                    },
                    {
                        extend: 'copy',
                        text: feather.icons['copy'].toSvg({ class: 'font-small-4 mr-50' }) + 'Kopyala',
                        className: 'dropdown-item',
                        exportOptions: { columns: [1,2,3,4,5,6,7,8] }
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
    $('div.head-label').html('<h2 class="mb-0">Kasa Hareketleri</h2>');
}
dataTable()
//-------------------------------------- Data Table Configuration - End ----------------------------------------//

let select_id = 0
let globalArr = []

//-------------------------------------- Açılıştaki Select Durumları - Start ----------------------------------------//
$('#id_boxActionsCompany').attr("disabled", true);
$('#id_boxActionsCustomer').attr("disabled", true);
$('#id_boxActionsInvoiceCode').attr("disabled", true);
//-------------------------------------- Açılıştaki Select Durumları - End ------------------------------------------//

//-------------------------------------- Box description Select Change - Start ----------------------------------------//
$("#id_boxActionsDescription").change(function (){
    if($(this).val() == 1){
        fncListCompany()
        $('#id_boxActionsCompany').attr("disabled", false);
    }
})
//-------------------------------------- Box description Select Change - End ------------------------------------------//

//-------------------------------------- Company Select Change - Start ----------------------------------------//
$("#id_boxActionsCompany").change(function (){
    fncListOfCustomerBySelectedCompany($(this).val())
    $('#id_boxActionsCustomer').attr("disabled", false);
})
//-------------------------------------- Company Select Change - End ------------------------------------------//

//-------------------------------------- Customer Select Change - Start ----------------------------------------//
$("#id_boxActionsCustomer").change(function (){
    fncListOfInvoiceCodeBySelectedCustomer($(this).val())
    $('#id_boxActionsInvoiceCode').attr("disabled", false);
})
//-------------------------------------- Customer Select Change - End ------------------------------------------//

//-------------------------------------- Create Row Datatable - Start ----------------------------------------//
function fncCreateRowDataTable(data){
    let html = ``
    let billingStatus = ""
    data.result.forEach( item => {
        globalArr.push(item)
        formatDate =  fncConvertDate(item.date)
        if(item.billingStatus != null && item.billingStatus === "1"){
            billingStatus = "Evet"
        }else if(item.billingStatus != null && item.billingStatus === "0"){
            billingStatus = "Hayır"
        }else{
            billingStatus = "Tümü"
        }
        html += `<tr  role="row" class="odd">
                    <td><a href="/fatura_duzenle/${item.id}">${item.invoiceCode}</a></td>
                    <td>${item.company.name}</td>
                    <td>${item.customer.name} ${item.customer.surname}</td>
                    <td>${item.customer.plate}</td>
                    <td>${billingStatus}</td>
                    <td>${item.debt}</td>
                    <td>${item.paid}</td>
                    <td>${item.remainingDebt}</td>
                    <td>${formatDate}</td>
                    <td class="text-left">
                        <button onclick="fncDelete(${item.id})" type="button" class="companyDelete btn btn-icon btn-outline-danger"><i class="far fa-trash-alt"></i></button>
                        <a href="/fatura_duzenle/${item.id}"  type="button" class="companyUpdate btn btn-icon btn-outline-primary"><i class="far fa-edit"></i></a>
                        <a href="/fatura/${item.id}" type="button" class="companyInfo btn btn-icon btn-outline-warning"><i class="fas fa-info-circle"></i></a>
                    </td>`
    })
    $("#id_boxActionsTableRow").html(html)
}
//-------------------------------------- Create Row Datatable - End ----------------------------------------//

//-------------------------------------- List of box actions payday to today - Start ----------------------------------------//
function fncListBoxActionsPaydayToToday(){
    $.ajax({
        url: "./kasa_haraketleri/listBoxActionsPaydayToToday",
        type: "GET",
        dataType: "JSON",
        contentType : 'application/json; charset=utf-8',
        //async:false,
        success: function (data) {

        },
        error: function (err) {
            console.log(err)
        }
    })
}
//-------------------------------------- List of box actions payday to today - End ------------------------------------------//

//-------------------------------------- List of company, customers, and invoice code - Start ----------------------------------------//
function fncListCompany(){
    $.ajax({
        url: "./kasa_haraketleri/listCompany",
        type: "GET",
        dataType: "JSON",
        contentType : 'application/json; charset=utf-8',
        //async:false,
        success: function (data) {
            fncOptionCompany(data)
        },
        error: function (err) {
            console.log(err)
        }
    })
}

function fncListOfCustomerBySelectedCompany(companyId){
    $.ajax({
        url: "./kasa_haraketleri/listOfCustomerBySelectedCompany/" + companyId,
        type: "GET",
        dataType: "JSON",
        contentType : 'application/json; charset=utf-8',
        //async:false,
        success: function (data) {
            fncOptionCustomer(data)
        },
        error: function (err) {
            console.log(err)
        }
    })
}

function fncListOfInvoiceCodeBySelectedCustomer(customerId){
    $.ajax({
        url: "./kasa_haraketleri/listOfInvoiceCodeBySelectedCustomer/" + customerId,
        type: "GET",
        dataType: "JSON",
        contentType : 'application/json; charset=utf-8',
        //async:false,
        success: function (data) {
            fncOptionInvoiceCode(data)
        },
        error: function (err) {
            console.log(err)
        }
    })
}

function fncOptionCompany(data){
    $("#id_boxActionsCompany").empty()
    $("#id_boxActionsCompany").append('<option value=null>Lütfen bir seçim yapınız</option>')
    data.result.forEach(item => {
        $("#id_boxActionsCompany").append('<option value="'+item.id+'">'+item.name+' </option>')
    })
}

function fncOptionCustomer(data){
    $("#id_boxActionsCustomer").empty()
    $("#id_boxActionsCustomer").append('<option value=null>Lütfen bir seçim yapınız</option>')
    data.result.forEach(item => {
        $("#id_boxActionsCustomer").append('<option value="'+item.id+'">'+item.plate+' - '+item.name+' '+item.surname+'</option>')
    })
}

function fncOptionInvoiceCode(data){
    $("#id_boxActionsInvoiceCode").empty()
    $("#id_boxActionsInvoiceCode").append('<option value=null>Lütfen bir seçim yapınız</option>')
    data.result.forEach(item => {
        $("#id_boxActionsInvoiceCode").append('<option value="'+item.id+'">'+item.invoiceCode+'</option>')
    })
}
//-------------------------------------- List of company, customers, and invoice code - End ------------------------------------------//

//-------------------------------------- Sweet Alert Box - Start ----------------------------------------//
function fncSweetAlert(data){
    if(data.status == true && data.result == null){
        Swal.fire({
            title: 'Başarılı!',
            text: "Dönen veri boş, lütfen işleminizi kontrol ediniz!",
            icon: "success",
            customClass: {
                confirmButton: 'btn btn-primary'
            },
            buttonsStyling: false
        });
    }else if(data.status == true && data.result != null){
        Swal.fire({
            title: 'Başarılı!',
            text: data.message,
            icon: "success",
            customClass: {
                confirmButton: 'btn btn-primary'
            },
            buttonsStyling: false
        });
    }else{
        if (!jQuery.isEmptyObject(data.error)) {
            //console.log(data.errors)
            Swal.fire({
                title: 'Hata!',
                text: data.error[0].defaultMessage,
                icon: "error",
                customClass: {
                    confirmButton: 'btn btn-primary'
                },
                buttonsStyling: false
            });
        }else{
            Swal.fire({
                title: 'Hata!',
                text: data.message,
                icon: "error",
                customClass: {
                    confirmButton: 'btn btn-primary'
                },
                buttonsStyling: false
            });
        }
    }
}
//-------------------------------------- Sweet Alert Box - End ------------------------------------------//

//-------------------------------------- Reset Form - Start ------------------------------------------//
function resetForm(){
    select_id = 0
    //fncListCustomer()
    $("#id_invoiceAddInvoiceNo").val(codeGenerator())

    $("#id_invoiceAddCompany").empty()
    fncListAllCompany()

    $("#id_invoiceAddCustomer").empty()
    $('#id_invoiceAddCustomer').attr("disabled", true);

    $("#id_invoiceAddDiscount").val("")
    $("#id_invoiceAddNote").val("")

    //Filtreleme seçenekleri dolu ise
    const date = $("#id_invoiceListFilterDate").val()
    const companyId = $('#id_invoiceListFilterCompany').val()
    const billingStatus = $('#id_invoiceListFilterBillingStatus').val()
    if(date != null && companyId != null && billingStatus != null){
        fncListInvoiceFilteredDateCompanyBillingStatus(date,companyId,billingStatus)
    }else{ //Filtreleme seçenekleri boş ise
        fncListInvoiceThisMonth()
    }

    //$('.source-item').repeaterVal()

}
//-------------------------------------- Reset Form - End --------------------------------------------//

//-------------------------------------- Date Convert - Start ------------------------------------------//
function fncConvertDate(date){
    let arr = date.split("-")
    return arr[2] + "-" + arr[1] + "-" + arr[0]
}
//-------------------------------------- Date Convert - End --------------------------------------------//
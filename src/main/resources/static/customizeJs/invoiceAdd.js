//-------------------------------------- Data Table Configuration - Start --------------------------------------//
function dataTable() {
    $("#id_invoiceAddTable").DataTable( {

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
        /*data: out.result,
        columns:[
            {data: 'code'},
            {data: 'company.name'},
            {data: 'name'},
            {data: 'phone'},
            {data: 'brand'},
            {data: 'model'},
            {data: 'plate'},
            {data: 'date'},
            {data: null, wrap: true, "render": function (item){
                    return '<div> <button onclick="fncDelete('+item.id+')" type="button" class="btn btn-icon btn-outline-danger"><i class="far fa-trash-alt"></i></button>\n' +
                        '<button onclick="fncUpdate('+item.id+')" type="button" class="btn btn-icon btn-outline-primary"><i class="far fa-edit"></i></button>\n' +
                        '<button onclick="fncDetail('+item.id+')" type="button" class="btn btn-icon btn-outline-warning"><i class="fas fa-info-circle"></i></button> </div>'
            }}
        ],*/
        language: {
            search: 'Ara',
            searchPlaceholder: 'İş Emri Ara',
            paginate: {
                // remove previous & next text from pagination
                previous: '&nbsp;',
                next: '&nbsp;'
            }
        }
    } );
    $('div.head-label').html('<h2 class="mb-0">İş Emri Listesi</h2>');
}
dataTable()
//-------------------------------------- Data Table Configuration - End ----------------------------------------//
let select_id = 0
let globalArr = []
let workArr = []
//-------------------------------------- Save or Update Dispatch Note Information - Start --------------------------------------//
function fncSaveButton(){

    event.preventDefault()

    const invoiceCode = $("#id_invoiceAddInvoiceNo").val()
    const invoiceBilling = $("#id_invoiceAddBillingStatus").val()
    const invoiceCompany = $("#id_invoiceAddCompany").val()
    const invoiceCustomer = $("#id_invoiceAddCustomer").val()
    const invoiceVat = $("#id_invoiceAddVat").val()
    const invoiceDiscount = $("#id_invoiceAddDiscount").val()
    const invoiceNote = $("#id_invoiceAddNote").val()
    const invoiceWork = $('.source-item').repeaterVal()["list_data"]

    for (let i = 0; i < invoiceWork.length; i++) {
        var temp = invoiceWork[i]
        //console.log(temp)
        if(temp.quantity == "" || temp.work == "" || temp.unitPrice == ""){
            delete invoiceWork[i]
        }else{
            workArr.push(temp)
        }
    }

    //console.log(invoiceWork)
    //console.log(workArr)

    const obj = {
        company: {id: invoiceCompany},
        customer: {id: invoiceCustomer},
        vat: invoiceVat,
        discount: invoiceDiscount,
        invoiceCode: invoiceCode,
        billingStatus: invoiceBilling,
        note: invoiceNote,
        workses: workArr
    }

    $.ajax({
        url:"./fatura_ekle/saveOrUpdate",
        type: "POST",
        data: JSON.stringify(obj),
        dataType: "JSON",
        contentType: "application/json; charset=utf-8",
        success: function (data){
            fncSweetAlert(data)
            resetForm()
            console.log(data)
        },
        error: function (err){
            console.log(err)
        }
    })

}
//-------------------------------------- Save or Update Dispatch Note Information - End ----------------------------------------//

//-------------------------------------- List of Invoice (Start of Month - Today) - Start ----------------------------------------//
function fncListInvoiceThisMonth(){
    var out
    $.ajax({
        url: "./fatura_ekle/listInvoiceThisMonth",
        type: "GET",
        dataType: "JSON",
        contentType : 'application/json; charset=utf-8',
        async:false,
        success: function (data) {
            //console.log(data)
            out = data
            if($.fn.DataTable.isDataTable("#id_invoiceAddTable")){
                $("#id_invoiceAddTable").DataTable().destroy()
            }
            fncCreateRowDataTable(data)
            dataTable()
        },
        error: function (err) {
            console.log(err)
        }
    })
    return out
}
fncListInvoiceThisMonth()
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
    $("#id_invoiceAddTableRow").html(html)
}
//-------------------------------------- List of Invoice (Start of Month - Today) - End ------------------------------------------//

//-------------------------------------- Filter Process - Start ----------------------------------------//
$('#id_invoiceListFilterCompany').attr("disabled", true);
$('#id_invoiceListFilterBillingStatus').attr("disabled", true);

$("#id_invoiceListFilterDate").change(function (){
    //console.log($(this).val())
    $('#id_invoiceListFilterCompany').attr("disabled", false);
    var companyData = fncListAllCompany()
    //console.log(companyData)
    fncOptionInvoiceListFilterCompany(companyData)
})

$("#id_invoiceListFilterCompany").change(function (){
    $('#id_invoiceListFilterBillingStatus').attr("disabled", false);
})

$("#id_invoiceListFilterBillingStatus").change(function (){
    const date = $("#id_invoiceListFilterDate").val()
    const companyId = $('#id_invoiceListFilterCompany').val()
    const billingStatus = $('#id_invoiceListFilterBillingStatus').val()
    //console.log(date)
    //console.log(companyId)
    //console.log(billingStatus)
    if(date != null && companyId != null && billingStatus != null){
        fncListInvoiceFilteredDateCompanyBillingStatus(date,companyId,billingStatus)
    }
})

function fncListInvoiceFilteredDateCompanyBillingStatus(date,companyId,billingStatus){
    var output
    $.ajax({
        url: "./fatura_ekle/listFilteredInvoice/" + date + "/" + companyId + "/" + billingStatus,
        type: "GET",
        dataType: "JSON",
        contentType : 'application/json; charset=utf-8',
        async:false,
        success: function (data) {
            output = data
            if($.fn.DataTable.isDataTable("#id_invoiceAddTable")){
                $("#id_invoiceAddTable").DataTable().destroy()
            }
            fncCreateRowDataTable(data)
            dataTable()
            console.log(data)
        },
        error: function (err) {
            console.log(err)
        }
    })
    return output
}
//-------------------------------------- Filter Process - End ------------------------------------------//

//-------------------------------------- Delete Invoice - Start ------------------------------------------//
function fncDelete(id){
    Swal.fire({
        title: 'Silme istediğinizden emin misiniz?',
        text: "Bu işlem geri alınamayacak!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Evet',
        cancelButtonText: 'Hayır',
        customClass: {
            confirmButton: 'btn btn-primary',
            cancelButton: 'btn btn-outline-danger ml-1'
        },
        buttonsStyling: false
    }).then(function (result) {
        if (result.value) {
            $.ajax({
                url: './fatura_ekle/deleteInvoice/' + id,
                type: 'DELETE',
                dataType: 'json',
                contentType: 'application/json; charset=utf-8',
                success: function (data) {
                    if( id != null){
                        Swal.fire({
                            icon: 'success',
                            title: "Silme işlemi başarılı!",
                            text: data.message,
                            customClass: {
                                confirmButton: 'btn btn-success'
                            }
                        });
                        resetForm()
                    }else{
                        Swal.fire({
                            icon: 'error',
                            title: "Hata",
                            text: data.message,
                            customClass: {
                                confirmButton: 'btn btn-success'
                            }
                        });
                    }
                },
                error: function (err) {
                    Swal.fire({
                        icon: 'error',
                        title: "Hata",
                        text: "Silme işlemi sırasında bir hata oluştu!",
                        customClass: {
                            confirmButton: 'btn btn-success'
                        }
                    });
                    console.log(err)
                }
            })
        }
    });
}
//-------------------------------------- Delete Invoice - End --------------------------------------------//

//-------------------------------------- Company List - Start ----------------------------------------//
function fncListAllCompany(){
    var output
    $.ajax({
        url: "./fatura_ekle/listCompanyByUserId",
        type: "GET",
        dataType: "JSON",
        contentType : 'application/json; charset=utf-8',
        async:false,
        success: function (data) {
            output = data
            fncOptionCompany(data)
        },
        error: function (err) {
            console.log(err)
        }
    })
    return output
}

function fncOptionCompany(data){
    $("#id_invoiceAddCompany").empty()
    $("#id_invoiceAddCompany").append('<option value=null>Lütfen bir seçim yapınız</option>')
    data.result.forEach((item) => {
        $("#id_invoiceAddCompany").append('<option value="'+item.id+'">'+item.name+' </option>')
    })
    $('#id_invoiceAddCustomer').attr("disabled", true);
}

function fncOptionInvoiceListFilterCompany(data){
    $("#id_invoiceListFilterCompany").empty()
    $("#id_invoiceListFilterCompany").append('<option value=null>Lütfen bir seçim yapınız</option>')
    data.result.forEach((item) => {
        $("#id_invoiceListFilterCompany").append('<option value="'+item.id+'">'+item.name+' </option>')
    })
}

fncListAllCompany()
//-------------------------------------- Company List - End ------------------------------------------//

//-------------------------------------- Customer List - Start ----------------------------------------//
function fncListCustomerBySelectedCompany(id){
    $.ajax({
        url: "./fatura_ekle/listCustomersBySelectedCompany/" + id,
        type: "GET",
        dataType: "JSON",
        contentType : 'application/json; charset=utf-8',
        //async:false,
        success: function (data) {
            fncOptionCustomer(data)
            //console.log(data)
        },
        error: function (err) {
            console.log(err)
        }
    })
}

function fncOptionCustomer(data){
    $("#id_invoiceAddCustomer").empty()
    if(data.result.length === 0){
        $("#id_invoiceAddCustomer").append('<option value="default">Müşteri bulunmamaktadır.</option>')
    }
    data.result.forEach((item) => {
        $("#id_invoiceAddCustomer").append('<option value="'+item.id+'">'+item.name+' '+item.surname+' - '+item.plate+'</option>')
    })
    $('#id_invoiceAddCustomer').attr("disabled", false);
}

$("#id_invoiceAddCompany").change(function (){
    //console.log($(this).val())
    fncListCustomerBySelectedCompany($(this).val())
})
//-------------------------------------- Customer List - End ------------------------------------------//

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

//-------------------------------------- Code Generator - Start ------------------------------------------//
function codeGenerator(){
    var output
    $.ajax({
        url: "./fatura_ekle/generateInvoiceCode",
        type: "GET",
        dataType: "JSON",
        contentType : 'application/json; charset=utf-8',
        async:false,
        success: function (data) {
            //console.log(data)
            output = data.result
            $("#id_invoiceAddInvoiceNo").val(data.result)
        },
        error: function (err) {
            console.log(err)
        }
    })
    return output
}
codeGenerator()
//-------------------------------------- Code Generator - End --------------------------------------------//

//-------------------------------------- Reset Form - Start ------------------------------------------//
function resetForm(){
    select_id = 0
    workArr = [] // Must be reset, otherwise it can stay past data.
    //fncListCustomer()
    $("#id_invoiceAddInvoiceNo").val(codeGenerator())

    $("#id_invoiceAddCompany").empty()
    fncListAllCompany()

    $("#id_invoiceAddCustomer").empty()
    $('#id_invoiceAddCustomer').attr("disabled", true);

    $("#id_invoiceAddDiscount").val("")
    $("#id_invoiceAddNote").val("")
    //resetForm2($('#form_repeater'))

    //Filtreleme seçenekleri dolu ise
    const date = $("#id_invoiceListFilterDate").val()
    const companyId = $('#id_invoiceListFilterCompany').val()
    const billingStatus = $('#id_invoiceListFilterBillingStatus').val()
    if(date != null && companyId != null && billingStatus != null){
        fncListInvoiceFilteredDateCompanyBillingStatus(date,companyId,billingStatus)
    }else{ //Filtreleme seçenekleri boş ise
        fncListInvoiceThisMonth()
    }

}

function resetForm2($form) {
    $form.find('input:text, input:password, input:number, select, textarea').val('');
}
//-------------------------------------- Reset Form - End --------------------------------------------//

//-------------------------------------- Date Convert - Start ------------------------------------------//
function fncConvertDate(date){
    let arr = date.split("-")
    return arr[2] + "-" + arr[1] + "-" + arr[0]
}
//-------------------------------------- Date Convert - End --------------------------------------------//




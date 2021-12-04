//-------------------------------------- Data Table Configuration - Start --------------------------------------//
function dataTable() {
    $("#id_invoiceEditTable").DataTable( {

        order: [[2, 'desc']],
        dom:
            '<"card-header border-bottom p-1"<"head-label"><"dt-action-buttons text-right"B>><"d-flex justify-content-between align-items-center mx-0 row"<"col-sm-12 col-md-6"l><"col-sm-12 col-md-6"f>>t<"d-flex justify-content-between mx-0 row"<"col-sm-12 col-md-6"i><"col-sm-12 col-md-6"p>>',
        displayLength: 10,
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
                        exportOptions: { columns: [1,2,3,4,5,6,7] }
                    },
                    {
                        extend: 'csv',
                        text: feather.icons['file-text'].toSvg({ class: 'font-small-4 mr-50' }) + 'Csv',
                        className: 'dropdown-item',
                        exportOptions: { columns: [1,2,3,4,5,6,7] }
                    },
                    {
                        extend: 'excel',
                        text: feather.icons['file'].toSvg({ class: 'font-small-4 mr-50' }) + 'Excel',
                        className: 'dropdown-item',
                        exportOptions: { columns: [1,2,3,4,5,6,7] }
                    },
                    {
                        extend: 'pdf',
                        text: feather.icons['clipboard'].toSvg({ class: 'font-small-4 mr-50' }) + 'Pdf',
                        className: 'dropdown-item',
                        exportOptions: { columns: [1,2,3,4,5,6,7] }
                    },
                    {
                        extend: 'copy',
                        text: feather.icons['copy'].toSvg({ class: 'font-small-4 mr-50' }) + 'Kopyala',
                        className: 'dropdown-item',
                        exportOptions: { columns: [1,2,3,4,5,6,7] }
                    }
                ],
            }
        ],
        language: {
            search: 'Ara',
            searchPlaceholder: 'İş Ara',
            paginate: {
                // remove previous & next text from pagination
                previous: '&nbsp;',
                next: '&nbsp;'
            }
        }
    } );
    $('div.head-label').html('<h2 class="mb-0">İş Listesi</h2>');
}
dataTable()
//-------------------------------------- Data Table Configuration - End ----------------------------------------//

let arr = window.location.pathname.split("/")
let invoice_id = arr[2]
let work_id = 0
let globalArr = []

//-------------------------------------- Save or Update Works Information - Start --------------------------------------//
$("#id_invoiceEditSaveForm").submit( (event) => {

    event.preventDefault()

    const invoiceEditBillingStatus = $("#id_invoiceEditBillingStatus").val()
    const invoiceEditDiscount = $("#id_invoiceEditDiscount").val()
    const invoiceEditVat = $("#id_invoiceEditVat").val()
    const invoiceEditWork = $("#id_invoiceEditWork").val()
    const invoiceEditQuantity = $("#id_invoiceEditQuantity").val()
    const invoiceEditUnitPrice = $("#id_invoiceEditUnitPrice").val()

    const obj = {
        id: invoice_id,
        invoiceCode: fncListWorks().result.invoiceCode,
        billingStatus: invoiceEditBillingStatus,
        discount: invoiceEditDiscount,
        vat: invoiceEditVat,
        workses: [
            {
                id: work_id,
                work: invoiceEditWork,
                quantity: invoiceEditQuantity,
                unitPrice: invoiceEditUnitPrice
            }
        ]
    }

    console.log(obj)

    $.ajax({
        url:"./update",
        type: "PUT",
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

})
//-------------------------------------- Save or Update Works Information - End ----------------------------------------//

//-------------------------------------- List of works - Start ----------------------------------------//
function fncListWorks(){
    var output
    $.ajax({
        url: "./listWorks/" + invoice_id,
        type: "GET",
        dataType: "JSON",
        contentType : 'application/json; charset=utf-8',
        async:false,
        success: function (data) {
            //console.log(data)
            output = data
            if($.fn.DataTable.isDataTable("#id_invoiceEditTable")){
                $("#id_invoiceEditTable").DataTable().destroy()
            }
            globalArr = [] //Important!!, must be remove otherwise can give false result
            fncCreateRowDataTable(data)
            dataTable()
        },
        error: function (err) {
            console.log(err)
        }
    })
    return output
}
fncListWorks()
function fncCreateRowDataTable(data){
    let html = ``
    let plate = data.result.customer.plate
    let discount = data.result.discount
    let vat = data.result.vat
    let billingStatus = data.result.billingStatus
    if(billingStatus != null && billingStatus === "1"){
        billingStatus = "Evet"
    }else if(billingStatus != null && billingStatus === "0"){
        billingStatus = "Hayır"
    }
    data.result.workses.forEach( item => {
        globalArr.push(item)
        if(item.status == true){
            html += `<tr  role="row" class="odd">
                    <td>${plate}</td>
                    <td>${item.work}</td>
                    <td>${item.quantity}</td>
                    <td>${item.unitPrice}</td>
                    <td>%${discount}</td>
                    <td>%${vat}</td>
                    <td>${billingStatus}</td>
                    <td class="text-left">
                        <button onclick="fncDelete(${item.id})" type="button" class="companyDelete btn btn-icon btn-outline-danger"><i class="far fa-trash-alt"></i></button>
                        <button onclick="fncUpdate(${item.id})" type="button" class="companyUpdate btn btn-icon btn-outline-primary"><i class="far fa-edit"></i></button>
                    </td>`
        }
    })
    $("#id_invoiceEditTableRow").html(html)
}
//-------------------------------------- List of works - End ------------------------------------------//

//-------------------------------------- Delete customer - Start ------------------------------------------//
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
                url: './delete/' + id,
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
//-------------------------------------- Delete customer - End --------------------------------------------//

//-------------------------------------- Update Customer Information - Start --------------------------------------------//
function fncUpdate(id){
    const itm_invoice = fncListWorks().result
    const itm_work = globalArr.find(item => item.id == id)
    //console.log("Globalarr--------------------")
    //console.log(globalArr)
    //console.log("itm_work---------------------")
    //console.log(itm_work)
    //console.log("itm_invoice-------------------")
    //console.log(itm_invoice)
    work_id = itm_work.id
    $("#id_invoiceEditBillingStatus").val(itm_invoice.billingStatus).trigger("change.select2")
    $("#id_invoiceEditDiscount").val(itm_invoice.discount)
    $("#id_invoiceEditVat").val(itm_invoice.vat).trigger("change.select2")
    $("#id_invoiceEditWork").val(itm_work.work)
    $("#id_invoiceEditQuantity").val(itm_work.quantity)
    $("#id_invoiceEditUnitPrice").val(itm_work.unitPrice)
}
//-------------------------------------- Update Customer Information - End ----------------------------------------------//

//-------------------------------------- Reset Form - Start ------------------------------------------//
function resetForm(){
    work_id = 0
    fncListWorks()
    $("#id_invoiceEditBillingStatus").val(0)
    $("#id_invoiceEditDiscount").val("")
    $("#id_invoiceEditVat").val(0)
    $("#id_invoiceEditWork").val("")
    $("#id_invoiceEditQuantity").val("")
    $("#id_invoiceEditUnitPrice").val("")
}
//-------------------------------------- Reset Form - End --------------------------------------------//

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

//-------------------------------------- Date Convert - Start ------------------------------------------//
function fncConvertDate(date){
    let arr = date.split("-")
    return arr[2] + "-" + arr[1] + "-" + arr[0]
}
//-------------------------------------- Date Convert - End --------------------------------------------//



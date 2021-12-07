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

let select_id = 0
let globalArr = []
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

    const obj = {
        company: {id: invoiceCompany},
        customer: {id: invoiceCustomer},
        vat: invoiceVat,
        discount: invoiceDiscount,
        invoiceCode: invoiceCode,
        billingStatus: invoiceBilling,
        note: invoiceNote,
        workses: $('.source-item').repeaterVal()["list_data"]
    }

    $.ajax({
        url:"./fatura_ekle/saveOrUpdate",
        type: "POST",
        data: JSON.stringify(obj),
        dataType: "JSON",
        contentType: "application/json; charset=utf-8",
        success: function (data){
            fncSweetAlert(data)
            //resetForm()
            console.log(data)
        },
        error: function (err){
            console.log(err)
        }
    })

}
//-------------------------------------- Save or Update Dispatch Note Information - End ----------------------------------------//

//-------------------------------------- Company List - Start ----------------------------------------//
function fncListAllCompany(){

    $.ajax({
        url: "./fatura_ekle/listCompanyByUserId",
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

function fncOptionCompany(data){
    $("#id_invoiceAddCompany").append('<option value="default">Lütfen bir seçim yapınız</option>')
    data.result.forEach((item) => {
        $("#id_invoiceAddCompany").append('<option value="'+item.id+'">'+item.name+' </option>')
    })
    $('#id_invoiceAddCustomer').attr("disabled", true);
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
            console.log(data)
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
    //fncListCustomer()
    $("#id_invoiceAddInvoiceNo").val(codeGenerator())

    $("#id_invoiceAddCompany").empty()
    fncListAllCompany()

    $("#id_invoiceAddCustomer").empty()
    $('#id_invoiceAddCustomer').attr("disabled", true);

    $("#id_invoiceAddDiscount").val("")
    $("#id_invoiceAddNote").val("")

    //$('.source-item').repeaterVal()

}
//-------------------------------------- Reset Form - End --------------------------------------------//


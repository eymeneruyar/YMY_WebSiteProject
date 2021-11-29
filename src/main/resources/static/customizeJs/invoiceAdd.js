
let select_id = 0
let globalArr = []
//-------------------------------------- Save or Update Dispatch Note Information - Start --------------------------------------//
function fncSaveButton(){

    event.preventDefault()

    const invoiceDate = $("#id_invoiceAddDate").val()
    const invoiceCompany = $("#id_invoiceAddCompany").val()
    const invoiceCustomer = $("#id_invoiceAddCustomer").val()
    const invoiceVat = $("#id_invoiceAddVat").val()
    const invoiceDiscount = $("#id_invoiceAddDiscount").val()
    const invoiceWork = $("#id_invoiceAddWork").val()
    const invoiceQuantity = $("#id_invoiceAddQuantity").val()
    const invoicePrice = $("#id_invoiceAddPrice").val()

    const obj = {
        company: {id: invoiceCompany},
        customer: {id: invoiceCustomer},
        vat: invoiceVat,
        discount: invoiceDiscount,
        invoiceCode: codeGenerator(),
        workses: $('.source-item').repeaterVal()["list_data"]
    }

    console.log(obj)


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

//-------------------------------------- Code Generator - Start ------------------------------------------//
function codeGenerator(){
    const date = new Date();
    const code = "SN36" + date.getFullYear() + (date.getMonth()+1)
    return code.toString()
}
//-------------------------------------- Code Generator - End --------------------------------------------//
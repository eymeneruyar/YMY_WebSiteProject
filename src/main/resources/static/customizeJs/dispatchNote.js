//-------------------------------------- Data Table Configuration - Start --------------------------------------//
function dataTable() {
    $("#id_dispatchNoteTable").DataTable( {

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
                        exportOptions: { columns: [1,2,3,4,5] }
                    },
                    {
                        extend: 'csv',
                        text: feather.icons['file-text'].toSvg({ class: 'font-small-4 mr-50' }) + 'Csv',
                        className: 'dropdown-item',
                        exportOptions: { columns: [1,2,3,4,5] }
                    },
                    {
                        extend: 'excel',
                        text: feather.icons['file'].toSvg({ class: 'font-small-4 mr-50' }) + 'Excel',
                        className: 'dropdown-item',
                        exportOptions: { columns: [1,2,3,4,5] }
                    },
                    {
                        extend: 'pdf',
                        text: feather.icons['clipboard'].toSvg({ class: 'font-small-4 mr-50' }) + 'Pdf',
                        className: 'dropdown-item',
                        exportOptions: { columns: [1,2,3,4,5] }
                    },
                    {
                        extend: 'copy',
                        text: feather.icons['copy'].toSvg({ class: 'font-small-4 mr-50' }) + 'Kopyala',
                        className: 'dropdown-item',
                        exportOptions: { columns: [1,2,3,4,5] }
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

let select_id = 0
let globalArr = []
//-------------------------------------- Save or Update Dispatch Note Information - Start --------------------------------------//
$("#id_dispatchNoteSaveForm").submit((event) => {

    event.preventDefault()

    const dispatchNoteCompany = $("#id_dispatchNoteCompany").val()
    const dispatchNoteCustomer = $("#id_dispatchNoteCustomer").val()
    const dispatchNoteVat = $("#id_dispatchNoteVat").val()
    const dispatchNoteDiscount = $("#id_dispatchNoteDiscount").val()
    const dispatchNoteWork = $("#id_dispatchNoteWork").val()
    const dispatchNoteQuantity = $("#id_dispatchNoteQuantity").val()
    const dispatchNotePrice = $("#id_dispatchNotePrice").val()

    const obj = {
        company: {id: dispatchNoteCompany},
        customer: {id: dispatchNoteCustomer},
        vat: dispatchNoteVat,
        discount: dispatchNoteDiscount,
        code: codeGenerator(),
        workses: $('.invoice-repeater').repeaterVal()['list_data']
    }

    console.log(obj)


})
//-------------------------------------- Save or Update Dispatch Note Information - End ----------------------------------------//

//-------------------------------------- Company List - Start ----------------------------------------//
function fncListAllCompany(){

    $.ajax({
        url: "./irsaliye/listCompanyByUserId",
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
    $("#id_dispatchNoteCompany").append('<option value="default">Lütfen bir seçim yapınız</option>')
    data.result.forEach((item) => {
        $("#id_dispatchNoteCompany").append('<option value="'+item.id+'">'+item.name+' </option>')
    })
    $('#id_dispatchNoteCustomer').attr("disabled", true);
}

fncListAllCompany()
//-------------------------------------- Company List - End ------------------------------------------//

//-------------------------------------- Customer List - Start ----------------------------------------//
function fncListCustomerBySelectedCompany(id){
    $.ajax({
        url: "./irsaliye/listCustomersBySelectedCompany/" + id,
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
    $("#id_dispatchNoteCustomer").empty()
    if(data.result.length === 0){
        $("#id_dispatchNoteCustomer").append('<option value="default">Müşteri bulunmamaktadır.</option>')
    }
    data.result.forEach((item) => {
        $("#id_dispatchNoteCustomer").append('<option value="'+item.id+'">'+item.name+' '+item.surname+' - '+item.plate+'</option>')
    })
    $('#id_dispatchNoteCustomer').attr("disabled", false);
}

$("#id_dispatchNoteCompany").change(function (){
    //console.log($(this).val())
    fncListCustomerBySelectedCompany($(this).val())
})
//-------------------------------------- Customer List - End ------------------------------------------//

//-------------------------------------- Code Generator - Start ------------------------------------------//
function codeGenerator() {
    const date = new Date();
    const time = date.getTime();
    return time.toString().substring(3);
}
//-------------------------------------- Code Generator - End --------------------------------------------//

//-------------------------------------- Reset Form - Start ------------------------------------------//
function resetForm(){
    select_id = 0
    fncListCustomer()
    $("#id_customerName").val(" ")
    $("#id_customerSurname").val(" ")
    $("#id_customerPhone").val(" ")
    $("#id_customerEmail").val(" ")
    $("#id_customerBrand").val(" ")
    $("#id_customerModel").val(" ")
    $("#id_customerPlate").val(" ")
    $("#id_customerNote").val(" ")

}
//-------------------------------------- Reset Form - End --------------------------------------------//

//-------------------------------------- Data Table Configuration - Start --------------------------------------//
$(document).ready(function() {
    $("#id_companyTable").DataTable( {

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
                        exportOptions: { columns: [1,2,3,4,5,6] }
                    },
                    {
                        extend: 'csv',
                        text: feather.icons['file-text'].toSvg({ class: 'font-small-4 mr-50' }) + 'Csv',
                        className: 'dropdown-item',
                        exportOptions: { columns: [1,2,3,4,5,6] }
                    },
                    {
                        extend: 'excel',
                        text: feather.icons['file'].toSvg({ class: 'font-small-4 mr-50' }) + 'Excel',
                        className: 'dropdown-item',
                        exportOptions: { columns: [1,2,3,4,5,6] }
                    },
                    {
                        extend: 'pdf',
                        text: feather.icons['clipboard'].toSvg({ class: 'font-small-4 mr-50' }) + 'Pdf',
                        className: 'dropdown-item',
                        exportOptions: { columns: [1,2,3,4,5,6] }
                    },
                    {
                        extend: 'copy',
                        text: feather.icons['copy'].toSvg({ class: 'font-small-4 mr-50' }) + 'Kopyala',
                        className: 'dropdown-item',
                        exportOptions: { columns: [1,2,3,4,5,6] }
                    }
                ],
            },

        ],
        language: {
            paginate: {
                // remove previous & next text from pagination
                previous: '&nbsp;',
                next: '&nbsp;'
            }
        }
    } );
    $('div.head-label').html('<h2 class="mb-0">Kayıtlı Firmalar</h2>');
} );
//-------------------------------------- Data Table Configuration - End ----------------------------------------//

let select_id = 0
let globalArr = []
//-------------------------------------- Save or Update Company Information - Start --------------------------------------//
$("#id_companySaveForm").submit((event) => {

    event.preventDefault()

    const company_name = $("#id_companyName").val()
    const company_official = $("#id_companyOfficial").val()
    const company_phone = $("#id_companyPhone").val()
    const company_email = $("#id_companyEmail").val()
    const company_taxOffice = $("#id_companyTaxOffice").val()
    const company_taxNumber = $("#id_companyTaxNumber").val()
    const company_city = $("#id_companyCity").val()
    const company_town = $("#id_companyTown").val()
    const company_address = $("#id_companyAddress").val()

    const obj = {
        name: company_name,
        authorisedPerson: company_official,
        phone: company_phone,
        email: company_email,
        taxOffice: company_taxOffice,
        taxNumber: company_taxNumber,
        city: company_city,
        town: company_town,
        address: company_address,
        code: codeGenerator()
    }

    //For update process
    if(select_id != 0){
        obj["id"] = select_id
    }

    $.ajax({
        url:"./firma/saveOrUpdate",
        type: "POST",
        data: JSON.stringify(obj),
        dataType: "JSON",
        contentType: "application/json; charset=utf-8",
        success: function (data){
            sweetAlert(data)
            resetForm()
            //console.log(data)
        },
        error: function (err){
            console.log(err)
        }
    })


})
//-------------------------------------- Save or Update Company Information - End ----------------------------------------//

//-------------------------------------- List Company Information - Start ----------------------------------------//
function listCompany(){
    $.ajax({
        url: "./firma/list",
        type: "GET",
        dataType: "JSON",
        contentType : 'application/json; charset=utf-8',
        success: function (data) {
            //console.log(data)
            createRowDataTable(data)
        },
        error: function (err) {
            console.log(err)
        }
    })
}
listCompany()

function createRowDataTable(data){
    let html = ``
    data.result.forEach( (item) => {
        html += `<tr  role="row" class="odd">
                    <td>${item.code}</td>
                    <td>${item.name}</td>
                    <td>${item.authorisedPerson}</td>
                    <td>${item.phone}</td>
                    <td>${item.email}</td>
                    <td>${item.date}</td>
                    <td class="text-left">
                        <button value="${item.id}" type="button" class="companyDelete btn btn-icon btn-outline-danger"><i class="far fa-trash-alt"></i></button>
                        <button value="${item.id}" type="button" class="companyInfo btn btn-icon btn-outline-warning"><i class="fas fa-info-circle"></i></button>
                        <button value="${item.id}" type="button" class="companyUpdate btn btn-icon btn-outline-primary"><i class="far fa-edit"></i></button>
                    </td>`

    } )
    $("#id_companyTable_row").html(html)
}
//-------------------------------------- List Company Information - End ------------------------------------------//

//-------------------------------------- Process of listing city and towns - Start ------------------------------------------//
function allCities(){

    $.ajax({
        url: "./firma/listCities",
        type: 'GET',
        contentType: 'application/json; charset=utf-8',
        success: function (data) {
            citiesOption(data)
        },
        error: function (err) {
            console.log(err)
        }
    })

}
allCities()

function listTownsBySelectedCity(id){

    $.ajax({
        url: "./firma/listTownsBySelectedCity/" + id,
        type: 'GET',
        contentType: 'application/json; charset=utf-8',
        success: function (data) {
            townsOption(data);
        },
        error: function (err) {
            console.log(err)
        }
    })

}

function citiesOption(data){
    data.result.forEach((item) => {
        $("#id_companyCity").append('<option value="'+item.cityKey+'">'+item.cityKey+' - '+item.name+' </option>')
    })
}

function townsOption(data){
    data.result.forEach((item) => {
        $("#id_companyTown").append('<option value="'+item.name+'">'+item.name+'</option>')
    })
}

$("#id_companyCity").change(function (){

    $('#id_companyTown').empty()
    $('#id_companyTown').append('<option value="default"> Lütfen Seçim Yapınız</option>');

    if ($(this).val() !== "default") {
        $('#id_companyTown').attr("disabled", false);  //ilçe selectlistini enable eder
        listTownsBySelectedCity($(this).val());
    }

})
//-------------------------------------- Process of listing city and towns - End --------------------------------------------//

//-------------------------------------- Sweet Alert Box - Start ----------------------------------------//
function sweetAlert(data){
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
function codeGenerator() {
    const date = new Date();
    const time = date.getTime();
    return time.toString().substring(3);
}
//-------------------------------------- Code Generator - End --------------------------------------------//

//-------------------------------------- Reset Form - Start ------------------------------------------//
function resetForm(){
    select_id = 0
    listCompany()
    $("#id_companySaveForm").trigger("reset")
}
//-------------------------------------- Reset Form - End --------------------------------------------//
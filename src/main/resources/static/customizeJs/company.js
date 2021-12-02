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
        async:false,
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
        globalArr.push(item)
        formatDate =  fncConvertDate(item.date)
        html += `<tr  role="row" class="odd">
                    <td>${item.code}</td>
                    <td>${item.name}</td>
                    <td>${item.authorisedPerson}</td>
                    <td>${item.phone}</td>
                    <td>${item.email}</td>
                    <td>${formatDate}</td>
                    <td class="text-left">
                        <button onclick="fncDelete(${item.id})" type="button" class="companyDelete btn btn-icon btn-outline-danger"><i class="far fa-trash-alt"></i></button>
                        <button onclick="fncUpdate(${item.id})" type="button" class="companyUpdate btn btn-icon btn-outline-primary"><i class="far fa-edit"></i></button>
                        <button onclick="fncDetail(${item.id})" type="button" class="companyInfo btn btn-icon btn-outline-warning"><i class="fas fa-info-circle"></i></button>
                    </td>`

    } )
    $("#id_companyTableRow").html(html)
}
//-------------------------------------- List Company Information - End ------------------------------------------//

//-------------------------------------- Delete Company Information - Start ------------------------------------------//
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
                url: './firma/delete/' + id,
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
//-------------------------------------- Delete Company Information - End --------------------------------------------//

//-------------------------------------- Update Company Information - Start --------------------------------------------//
function fncUpdate(id){
    const itm = globalArr.find(item => item.id == id)
    //console.log(itm)
    select_id = itm.id

    listTownsBySelectedCity(itm.city)
    $("#id_companyName").val(itm.name)
    $("#id_companyOfficial").val(itm.authorisedPerson)
    $("#id_companyPhone").val(itm.phone)
    $("#id_companyEmail").val(itm.email)
    $("#id_companyTaxOffice").val(itm.taxOffice)
    $("#id_companyTaxNumber").val(itm.taxNumber)
    $("#id_companyCity").val(itm.city).trigger('change.select2')
    $("#id_companyTown").val(itm.town).trigger('change.select2')
    $("#id_companyAddress").val(itm.address)
}
//-------------------------------------- Update Company Information - End ----------------------------------------------//

//--------------------------------------  Company Information Modal - Start ----------------------------------------------//
function fncDetail(id){
    //const itm = globalArr.find( item => item.id == id)
    var out
    $.ajax({
        url: "./firma/detail/" + id,
        type: 'GET',
        contentType: 'application/json; charset=utf-8',
        async:false,
        success: function (data) {
            out = data.result
        },
        error: function (err) {
            console.log(err)
        }
    })
    //console.log(out)
    var city = getInfoCityByCityKey(out.city)
    $("#id_companyDetailModal").modal('toggle')
    $("#id_companyDetailModalTitle").text(out.code + " - " + out.name)
    $("#id_companyDetailModalName").text(out.name)
    $("#id_companyDetailModalOfficial").text(out.authorisedPerson)
    $("#id_companyDetailModalPhone").text(out.phone)
    $("#id_companyDetailModalEmail").text(out.email)
    $("#id_companyDetailModalTaxOffice").text(out.taxOffice)
    $("#id_companyDetailModalTaxNumber").text(out.taxNumber)
    $("#id_companyDetailModalCity").text(city.result.name)
    $("#id_companyDetailModalTown").text(out.town)
    $("#id_companyDetailModalAddress").text(out.address)

}
//--------------------------------------  Company Information Modal - End ------------------------------------------------//

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
listTownsBySelectedCity(34)
//$('#id_companyTown').attr("disabled", true);

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

function getInfoCityByCityKey(id){
    var out;

    $.ajax({
        url: "./firma/getInfoCityByCityKey/" + id,
        type: 'GET',
        contentType: 'application/json; charset=utf-8',
        async: false,
        success: function (data) {
            out = data
        },
        error: function (err) {
            console.log(err)
        }
    })
    return out
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
    $('#id_companyCity').empty();
    $('#id_companyTown').empty();
    $('#id_companyCity').append('<option value="default"> Lütfen Seçim Yapınız</option>');
    allCities()
    //$('#id_companyTown').attr("disabled", true);
}
//-------------------------------------- Reset Form - End --------------------------------------------//

//-------------------------------------- Date Convert - Start ------------------------------------------//
function fncConvertDate(date){
    let arr = date.split("-")
    return arr[2] + "-" + arr[1] + "-" + arr[0]
}
//-------------------------------------- Date Convert - End --------------------------------------------//

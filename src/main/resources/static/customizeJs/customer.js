//-------------------------------------- Data Table Configuration - Start --------------------------------------//
$(document).ready(function() {
    $("#id_customerTable").DataTable( {

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
        language: {
            paginate: {
                // remove previous & next text from pagination
                previous: '&nbsp;',
                next: '&nbsp;'
            }
        }
    } );
    $('div.head-label').html('<h2 class="mb-0">Kayıtlı Müşteriler</h2>');
} );
//-------------------------------------- Data Table Configuration - End ----------------------------------------//

let select_id = 0
let globalArr = []
//-------------------------------------- Save or Update Customer Information - Start --------------------------------------//
$("#id_customerSaveForm").submit( (event) => {

    event.preventDefault()

    const customerCompany = $("#id_customerCompany").val()
    const customerName = $("#id_customerName").val()
    const customerSurname = $("#id_customerSurname").val()
    const customerPhone = $("#id_customerPhone").val()
    const customerEmail = $("#id_customerEmail").val()
    const customerBrand = $("#id_customerBrand").val()
    const customerModel = $("#id_customerModel").val()
    const customerPlate = $("#id_customerPlate").val()
    const customerNote = $("#id_customerNote").val()

    const obj = {
        code: codeGenerator(),
        company: {
            id: customerCompany
        },
        name: customerName,
        surname: customerSurname,
        phone: customerPhone,
        email: customerEmail,
        brand: customerBrand,
        model: customerModel,
        plate: customerPlate,
        note: customerNote
    }

    console.log(obj)

    if(select_id != 0){
        obj["id"] = select_id
    }

    $.ajax({
        url:"./musteri/saveOrUpdate",
        type: "POST",
        data: JSON.stringify(obj),
        dataType: "JSON",
        contentType: "application/json; charset=utf-8",
        success: function (data){
            fncSweetAlert(data)
            resetForm()
            //console.log(data)
        },
        error: function (err){
            console.log(err)
        }
    })

})
//-------------------------------------- Save or Update Customer Information - End ----------------------------------------//

//-------------------------------------- Company List - Start ----------------------------------------//
function fncListAllCompany(){

    $.ajax({
        url: "./musteri/listAllCompany",
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
    data.result.forEach((item) => {
        $("#id_customerCompany").append('<option value="'+item.id+'">'+item.name+' </option>')
    })
}

fncListAllCompany()
//-------------------------------------- Company List - End ------------------------------------------//

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
function codeGenerator() {
    const date = new Date();
    const time = date.getTime();
    return time.toString().substring(3);
}
//-------------------------------------- Code Generator - End --------------------------------------------//

//-------------------------------------- Reset Form - Start ------------------------------------------//
function resetForm(){
    select_id = 0
    //listCompany()
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



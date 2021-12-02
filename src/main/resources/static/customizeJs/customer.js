//-------------------------------------- Data Table Configuration - Start --------------------------------------//
function dataTable() {
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
    $('div.head-label').html('<h2 class="mb-0">Kayıtlı Müşteriler</h2>');
}
dataTable()
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
        },
        error: function (err){
            console.log(err)
        }
    })

})
//-------------------------------------- Save or Update Customer Information - End ----------------------------------------//

//-------------------------------------- List of customer - Start ----------------------------------------//
var out
function fncListCustomer(){
    let id = $("#id_customerCompany").val()

    $.ajax({
        url: "./musteri/listBySelectedCompany/" + id,
        type: "GET",
        dataType: "JSON",
        contentType : 'application/json; charset=utf-8',
        async:false,
        success: function (data) {
            console.log(data)
            out = data
            if($.fn.DataTable.isDataTable("#id_customerTable")){
                $("#id_customerTable").DataTable().destroy()
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
//fncListCustomer()

function fncCreateRowDataTable(data){
    let html = ``
    data.result.forEach( item => {
        globalArr.push(item)
        formatDate =  fncConvertDate(item.date)
        html += `<tr  role="row" class="odd">
                    <td>${item.code}</td>
                    <td>${item.company.name}</td>
                    <td>${item.name} ${item.surname}</td>
                    <td>${item.phone}</td>
                    <td>${item.brand}</td>
                    <td>${item.model}</td>
                    <td>${item.plate}</td>
                    <td>${formatDate}</td>
                    <td class="text-left">
                        <button onclick="fncDelete(${item.id})" type="button" class="companyDelete btn btn-icon btn-outline-danger"><i class="far fa-trash-alt"></i></button>
                        <button onclick="fncUpdate(${item.id})" type="button" class="companyUpdate btn btn-icon btn-outline-primary"><i class="far fa-edit"></i></button>
                        <button onclick="fncDetail(${item.id})" type="button" class="companyInfo btn btn-icon btn-outline-warning"><i class="fas fa-info-circle"></i></button>
                    </td>`
    })
    $("#id_customerTableRow").html(html)
}

$("#id_customerCompany").change(function (){
    fncListCustomer()
})
//-------------------------------------- List of customer - End ------------------------------------------//

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
                url: './musteri/delete/' + id,
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
    const itm = globalArr.find(item => item.id == id)
    //console.log(itm)
    select_id = itm.id
    $("#id_customerCompany").val(itm.company.id).trigger("change.select2")
    $("#id_customerName").val(itm.name)
    $("#id_customerSurname").val(itm.surname)
    $("#id_customerPhone").val(itm.phone)
    $("#id_customerEmail").val(itm.email)
    $("#id_customerBrand").val(itm.brand)
    $("#id_customerModel").val(itm.model)
    $("#id_customerPlate").val(itm.plate)
    $("#id_customerNote").val(itm.note)
}
//-------------------------------------- Update Customer Information - End ----------------------------------------------//

//-------------------------------------- Customer Information Detail - Start ----------------------------------------------//
function fncDetail(id){
    const itm = fncDetailCustomerById(id)
    $("#id_customerDetailModal").modal('toggle')
    $("#id_customerDetailModalTitle").text(itm.code + " - " + itm.name + " " + itm.surname)
    $("#id_customerDetailModalCompany").text(itm.company.name)
    $("#id_customerDetailModalName").text(itm.name)
    $("#id_customerDetailModalSurname").text(itm.surname)
    $("#id_customerDetailModalPhone").text(itm.phone)
    $("#id_customerDetailModalEmail").text(itm.email)
    $("#id_customerDetailModalBrand").text(itm.brand)
    $("#id_customerDetailModalModel").text(itm.model)
    $("#id_customerDetailModalPlate").text(itm.plate)
    $("#id_customerDetailModalNote").text(itm.note)
}

function fncDetailCustomerById(id){
    var output
    $.ajax({
        url: "./musteri/detail/" + id,
        type: 'GET',
        contentType: 'application/json; charset=utf-8',
        async:false,
        success: function (data) {
            output = data.result
        },
        error: function (err) {
            console.log(err)
        }
    })
    return output
}
//-------------------------------------- Customer Information Detail - End ------------------------------------------------//

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

//-------------------------------------- Date Convert - Start ------------------------------------------//
function fncConvertDate(date){
    let arr = date.split("-")
    return arr[2] + "-" + arr[1] + "-" + arr[0]
}
//-------------------------------------- Date Convert - End --------------------------------------------//



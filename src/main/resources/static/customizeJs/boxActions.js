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
                        exportOptions: { columns: [0,1,2,3,4,5,6,7] }
                    },
                    {
                        extend: 'csv',
                        text: feather.icons['file-text'].toSvg({ class: 'font-small-4 mr-50' }) + 'Csv',
                        className: 'dropdown-item',
                        exportOptions: { columns: [0,1,2,3,4,5,6,7] }
                    },
                    {
                        extend: 'excel',
                        text: feather.icons['file'].toSvg({ class: 'font-small-4 mr-50' }) + 'Excel',
                        className: 'dropdown-item',
                        exportOptions: { columns: [0,1,2,3,4,5,6,7] }
                    },
                    {
                        extend: 'pdf',
                        text: feather.icons['clipboard'].toSvg({ class: 'font-small-4 mr-50' }) + 'Pdf',
                        className: 'dropdown-item',
                        exportOptions: { columns: [0,1,2,3,4,5,6,7] }
                    },
                    {
                        extend: 'copy',
                        text: feather.icons['copy'].toSvg({ class: 'font-small-4 mr-50' }) + 'Kopyala',
                        className: 'dropdown-item',
                        exportOptions: { columns: [0,1,2,3,4,5,6,7] }
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

let select_id = 0
let globalArr = []

//-------------------------------------- Save Box Actions Input/Output - Start --------------------------------------//
$("#id_boxActionsSaveForm").submit((event) => {
    event.preventDefault()

    const baDescription = $("#id_boxActionsDescription").val()
    const baCompany = $("#id_boxActionsCompany").val()
    const baCustomer = $("#id_boxActionsCustomer").val()
    const baInvoiceCode = $("#id_boxActionsInvoiceCode").val()
    const baTitle = $("#id_boxActionsTitle").val()
    const baPaymentMethod = $("#id_boxActionsPaymentMethod").val()
    const baAmount = $("#id_boxActionsAmount").val()
    const baTransactionDate = $("#id_boxActionsTransactionDate").val()
    const baNote = $("#id_boxActionsNote").val()

    const obj = {
        description: baDescription,
        company: {id:baCompany},
        customer: {id:baCustomer},
        invoice: {id:baInvoiceCode},
        title: baTitle,
        paymentMethod: baPaymentMethod,
        amount: baAmount,
        transactionDate: baTransactionDate,
        note: baNote
    }

    console.log(obj)

    $.ajax({
        url:"./kasa_haraketleri/save",
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
//-------------------------------------- Save Box Actions Input/Output - End ----------------------------------------//

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

//-------------------------------------- Create Row Datatable - Start ----------------------------------------//
function fncCreateRowDataTable(data){
    let html = ``
    let billingStatus = ""
    data.result.forEach( item => {
        globalArr.push(item)
        formatDate =  fncConvertDate(item.transactionDate)
        if(item.description == 0){ //Kasa çıkış
            html += `<tr style="background-color: #fae8e8;" role="row" class="odd">
                    <td>${item.description == 0 ? "Çıkış" : "Giriş"}</td>
                    <td></td>
                    <td></td>
                    <td>${item.title}</td>
                    <td>${item.note.substring(0,10)}...</td>
                    <td>${item.paymentMethod}</td>
                    <td>${item.amount}</td>
                    <td>${formatDate}</td>
                    <td class="text-left">
                        <button onclick="fncUndo(${item.id})" type="button" class="btn btn-icon btn-outline-primary"><i class="fas fa-undo-alt"></i></button>
                        <button onclick="fncDetail(${item.id})" type="button" class="btn btn-icon btn-outline-warning"><i class="fas fa-info-circle"></i></button>
                    </td>`
        }else{ //Kasa giriş
            html += `<tr style="background-color: #e3f6e3;" role="row" class="odd">
                    <td>${item.description == 1 ? "Giriş" : "Çıkış"}</td>
                    <td>${item.company.name}</td>
                    <td>${item.customer.plate}</td>
                    <td>${item.title}</td>
                    <td>${item.note.substring(0,10)}...</td>
                    <td>${item.paymentMethod}</td>
                    <td>${item.amount}</td>
                    <td>${formatDate}</td>
                    <td class="text-left">
                        <button onclick="fncUndo(${item.id})"  type="button" class="btn btn-icon btn-outline-primary"><i class="fas fa-undo-alt"></i></button>
                        <button onclick="fncDetail(${item.id})" type="button" class="btn btn-icon btn-outline-warning"><i class="fas fa-info-circle"></i></button>
                    </td>`
        }

    })
    $("#id_boxActionsTableRow").html(html)
}
//-------------------------------------- Create Row Datatable - End ----------------------------------------//

//-------------------------------------- List of box actions payday to today - Start ----------------------------------------//
function fncListBoxActionsPaydayToToday(){
    $.ajax({
        url: "./kasa_haraketleri/listBoxActionsPaydayToToday",
        type: "GET",
        dataType: "JSON",
        contentType : 'application/json; charset=utf-8',
        //async:false,
        success: function (data) {
            console.log(data)
            if($.fn.DataTable.isDataTable("#id_boxActionsTable")){
                $("#id_boxActionsTable").DataTable().destroy()
            }
            globalArr = [] //Important!!, must be remove otherwise can give false result
            fncCreateRowDataTable(data)
            dataTable()
        },
        error: function (err) {
            console.log(err)
        }
    })
}
fncListBoxActionsPaydayToToday()
//-------------------------------------- List of box actions payday to today - End ------------------------------------------//

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
        $("#id_boxActionsInvoiceCode").append('<option value="'+item.id+'">'+item.invoiceCode+' - '+item.remainingDebt+'</option>')
    })
}
//-------------------------------------- List of company, customers, and invoice code - End ------------------------------------------//

//-------------------------------------- Detail of payment process - Start ------------------------------------------//
function fncDetail(id){
    const itm = globalArr.find(item => item.id == id)
    $("#id_boxActionsDetailModal").modal('toggle')
    $("#id_boxActionsDetailModalDescription").text(itm.description == 0 ? "Çıkış" : "Giriş")
    if(itm.description == 0){
        $("#id_boxActionsDetailModalTitle").text("Çıkış - " + itm.title)
        $("#id_boxActionsDetailModalCompany").text("------------")
        $("#id_boxActionsDetailModalCustomer").text("------------")
        $("#id_boxActionsDetailModalInvoiceCode").text("------------")
    }else if(itm.description == 1){
        $("#id_boxActionsDetailModalTitle").text("Giriş - " + itm.title)
        $("#id_boxActionsDetailModalCompany").text(itm.company.name)
        $("#id_boxActionsDetailModalCustomer").text(itm.customer.name + " " + itm.customer.surname)
        $("#id_boxActionsDetailModalInvoiceCode").text(itm.invoice.invoiceCode)
    }
    $("#id_boxActionsDetailModalTitle2").text(itm.title)
    $("#id_boxActionsDetailModalPaymentMethod").text(itm.paymentMethod)
    $("#id_boxActionsDetailModalAmount").text(itm.amount)
    $("#id_boxActionsDetailModalTransactionDate").text(itm.transactionDate)
    $("#id_boxActionsDetailModalNote").text(itm.note)
}
//-------------------------------------- Detail of payment process - End --------------------------------------------//

//-------------------------------------- Get back of payment process - Start --------------------------------------------//
function fncUndo(id){

    Swal.fire({
        title: 'Ödeme işlemini geri almak istediğinizden emin misiniz?',
        //text: "Bu işlem geri alınamayacak!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Evet',
        cancelButtonText: 'Hayır',
        customClass: {
            confirmButton: 'btn btn-primary',
            cancelButton: 'btn btn-outline-danger ml-1'
        },
        buttonsStyling: false
    }).then(function (result){
        if(result.value){
            $.ajax({
                url: "./kasa_haraketleri/undoBoxActions/" + id,
                type: "PUT",
                dataType: "JSON",
                contentType : 'application/json; charset=utf-8',
                //async:false,
                success: function (data) {
                    resetForm()
                },
                error: function (err) {
                    console.log(err)
                }
            })
        }
    })
}
//-------------------------------------- Get back of payment process - End ----------------------------------------------//

//-------------------------------------- Filter Section - Start ------------------------------------------//
$('#id_boxActionsFilterCompany').attr("disabled", true);
$('#id_boxActionsFilterDescription').change(function () {
    if($(this).val() == 1){
        $('#id_boxActionsFilterCompany').attr("disabled", false);
        fncFilterListCompany()
    }else{
        $('#id_boxActionsFilterCompany').attr("disabled", true);
        $('#id_boxActionsFilterCompany').empty()
    }
})

function fncFilterListCompany(){
    $.ajax({
        url: "./kasa_haraketleri/listCompany",
        type: "GET",
        dataType: "JSON",
        contentType : 'application/json; charset=utf-8',
        //async:false,
        success: function (data) {
            fncFilterOptionCompany(data)
        },
        error: function (err) {
            console.log(err)
        }
    })
}

function fncFilterOptionCompany(data){
    $("#id_boxActionsFilterCompany").empty()
    $("#id_boxActionsFilterCompany").append('<option value=null>Lütfen bir seçim yapınız</option>')
    data.result.forEach(item => {
        $("#id_boxActionsFilterCompany").append('<option value="'+item.id+'">'+item.name+' </option>')
    })
}

function fncFilterApply(){

    var filterDesc = $('#id_boxActionsFilterDescription').val()
    var filterDate = $('#id_boxActionsFilterDate').val()
    var filterCompany = $('#id_boxActionsFilterCompany').val()

    //console.log(filterDesc)
    //console.log(filterDate)
    //console.log(filterCompany)

    //Filtered data by selected description of box and date
    if(filterDesc != null && (filterDate != null && filterDate != "") && filterCompany == null){
        $.ajax({
            url: "./kasa_haraketleri/listBoxActionsByDescriptionAndDate/" + filterDesc + '/' + filterDate,
            type: "GET",
            dataType: "JSON",
            contentType : 'application/json; charset=utf-8',
            //async:false,
            success: function (data) {
                if($.fn.DataTable.isDataTable("#id_boxActionsTable")){
                    $("#id_boxActionsTable").DataTable().destroy()
                }
                fncCreateRowDataTable(data)
                dataTable()            },
            error: function (err) {
                console.log(err)
            }
        })
    }else if(filterDesc != null && filterDate != null && filterCompany != null){
        $.ajax({
            url: "./kasa_haraketleri/listBoxActionsByDescriptionAndDateAndCompany/" + filterDesc + '/' + filterDate + '/' + filterCompany,
            type: "GET",
            dataType: "JSON",
            contentType : 'application/json; charset=utf-8',
            //async:false,
            success: function (data) {
                if($.fn.DataTable.isDataTable("#id_boxActionsTable")){
                    $("#id_boxActionsTable").DataTable().destroy()
                }
                fncCreateRowDataTable(data)
                dataTable()            },
            error: function (err) {
                console.log(err)
            }
        })
    }else{
        Swal.fire({
            title: 'Uyarı!',
            text: "Filtreleme yapmak için kasa tanımı ve tarih alanları seçilmelidir!",
            icon: "warning",
            customClass: {
                confirmButton: 'btn btn-primary'
            },
            buttonsStyling: false
        });
    }

}

//-------------------------------------- Filter Section - End --------------------------------------------//

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

//-------------------------------------- Reset Form - Start ------------------------------------------//
function resetForm(){

    $("#id_boxActionsDescription").val(null)
    $("#id_boxActionsCompany").empty()
    $('#id_boxActionsCompany').attr("disabled", true);

    $("#id_boxActionsCustomer").empty()
    $('#id_boxActionsCustomer').attr("disabled", true);

    $("#id_boxActionsInvoiceCode").empty()
    $('#id_boxActionsInvoiceCode').attr("disabled", true);

    $("#id_boxActionsTitle").val("")
    $("#id_boxActionsPaymentMethod").val(null)
    $("#id_boxActionsAmount").val("")
    $("#id_boxActionsTransactionDate").val("")
    $("#id_boxActionsNote").val("")

    //Filtreleme seçenekleri dolu ise
    const desc = $("#id_boxActionsFilterDescription").val()
    const date = $('#id_boxActionsFilterDate').val()
    const companyId = $('#id_boxActionsFilterCompany').val()
    if(date != null && companyId != null && desc != null){
        fncFilterApply()
    }else{ //Filtreleme seçenekleri boş ise
        fncListBoxActionsPaydayToToday()
    }

}
//-------------------------------------- Reset Form - End --------------------------------------------//

//-------------------------------------- Date Convert - Start ------------------------------------------//
function fncConvertDate(date){
    let arr = date.split("-")
    return arr[2] + "-" + arr[1] + "-" + arr[0]
}
//-------------------------------------- Date Convert - End --------------------------------------------//

//--------------------------------------- Definition of Days and Months - Start ---------------------------------------//
const months = ['Ocak', 'Şubat', 'Mart', 'Nisan', 'Mayıs', 'Haziran',
    'Temmuz', 'Ağustos', 'Eylül', 'Ekim', 'Kasım', 'Aralık'
]

const days_shorted = ['Pzr', 'Pzrts', 'Sal', 'Çarş', 'Perş', 'Cum', 'Cmrts']
const days = ['Pazar', 'Pazartesi', 'Salı', 'Çarşamba', 'Perşembe', 'Cuma', 'Cumartesi']
//--------------------------------------- Definition of Days and Months - End -----------------------------------------//

//---------------------------------- Agenda Note Save - Start ----------------------------------//
var globalArr = []
var selected_id = 0
$('#id_agendaSaveForm').submit((event) => {

    event.preventDefault()

    const agendaTitle = $('#id_agendaTitle').val()
    const agendaDate = $('#id_agendaDate').val()
    const agendaNote = $('#id_agendaNote').val()

    const obj = {
        title: agendaTitle,
        reminderDate: agendaDate,
        note: agendaNote
    }

    console.log(obj)

    //For update process
    if(select_id != 0){
        obj["id"] = select_id
    }

    $.ajax({
        url: "./ajandam/saveOrUpdate",
        type: "POST",
        dataType: "JSON",
        data: JSON.stringify(obj),
        contentType : 'application/json; charset=utf-8',
        //async:false,
        success: function (data) {
            console.log(data)
            fncSweetAlert(data)
            resetForm()
        },
        error: function (err) {
            console.log(err)
        }
    })

})
//---------------------------------- Agenda Note Save - End ----------------------------------//

//---------------------------------- Agenda Note List - Start ----------------------------------//
function fncListNotes(){
    $.ajax({
        url: "./ajandam/list",
        type: "GET",
        dataType: "JSON",
        contentType : 'application/json; charset=utf-8',
        //async:false,
        success: function (data) {
            console.log(data)
            globalArr = []
            fncCreateRowDataTable(data)
        },
        error: function (err) {
            console.log(err)
        }
    })
}
fncListNotes()

function fncCreateRowDataTable(data){
    let html = ``
    if(data.result.length > 0){
        data.result.forEach(item => {
            globalArr.push(item)
            //console.log(globalArr)
            let date = new Date(item.reminderDate)
            let formatDate = fncConvertDate(item.reminderDate)
            html += `<div class="col-lg-4 col-md-6 col-12">
                    <div class="card card-developer-meetup">
                        <div class="meetup-img-wrapper rounded-top text-center">
                            <img src="/dashboardPage/app-assets/images/illustration/email.svg" alt="Meeting Pic" height="170" />
                        </div>
                        <div class="card-body">
                            <div class="meetup-header d-flex align-items-center">
                                <div class="meetup-day">
                                    <h6 class="mb-0">${days_shorted[date.getDay()]}</h6>
                                    <h3 class="mb-0">${formatDate.split("-")[0]}</h3>
                                </div>
                                <div class="my-auto">
                                    <h4 class="card-title mb-25">${item.title}</h4>
                                </div>
                            </div>
                            <div class="media">
                                <div class="media-body">
                                    <p class="card-text mb-0">${item.note}</p>
                                </div>
                            </div>
                            <div class="media">
                                <div class="avatar bg-light-primary rounded mr-1">
                                    <div class="avatar-content">
                                        <i class="fas fa-calendar-week avatar-icon font-medium-3"></i>
                                    </div>
                                </div>
                                <div class="media-body">
                                    <h6 class="mb-0">${formatDate.split("-")[0]} ${months[date.getMonth()]} ${days[date.getDay()]}, ${formatDate.split("-")[2]}</h6>
                                </div>
                            </div>
                            <div class="media mt-2">
                                <div class="media-body">
                                    <div class="text-right">
                                        <button onclick="fncDeleteNote(${item.id})" type="button" class="btn btn-icon btn-outline-danger ">
                                            <i class="far fa-trash-alt"></i>
                                        </button>
                                        <button onclick="fncUpdateNote(${item.id})" type="button" class="btn btn-icon btn-outline-primary">
                                            <i class="far fa-edit"></i>
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>`
        })
        $('#id_agendaNoteCard').html(html)
    }

}
//---------------------------------- Agenda Note List - End ------------------------------------//

//---------------------------------- Agenda Note Delete - Start ------------------------------------//
function fncDeleteNote(id){
    Swal.fire({
        title: 'Silmek istediğinizden emin misiniz?',
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
                url: "./ajandam/delete/" + id,
                type: "DELETE",
                dataType: "JSON",
                contentType : 'application/json; charset=utf-8',
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
//---------------------------------- Agenda Note Delete - End ------------------------------------//

//---------------------------------- Agenda Note Update - End ------------------------------------//
function fncUpdateNote(id){
    const itm = globalArr.find(item => item.id == id)
    select_id = itm.id

    $('#id_agendaTitle').val(itm.title)
    $('#id_agendaDate').val(itm.reminderDate)
    $('#id_agendaNote').val(itm.note)
}
//---------------------------------- Agenda Note Update - End ------------------------------------//

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

    $('#id_agendaSaveForm').trigger('reset')
    fncListNotes()

}
//-------------------------------------- Reset Form - End --------------------------------------------//

//-------------------------------------- Date Convert - Start ------------------------------------------//
function fncConvertDate(date){
    let arr = date.split("-")
    return arr[2] + "-" + arr[1] + "-" + arr[0]
}
//-------------------------------------- Date Convert - End --------------------------------------------//


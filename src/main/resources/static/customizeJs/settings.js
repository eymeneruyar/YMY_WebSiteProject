//-------------------------------------- Save or Update User Information - Start --------------------------------------//
$('#id_settingsUserInfoSaveForm').submit(event => {

    event.preventDefault()

    const settingsName = $('#id_settingsName').val()
    const settingsSurname = $('#id_settingsSurname').val()
    const settingsEmail = $('#id_settingsEmail').val()
    const settingsCompany = $('#id_settingsCompany').val()

    const obj = {
        name: settingsName,
        surname: settingsSurname,
        email: settingsEmail,
        companyName: settingsCompany
    }

    $.ajax({
        url:"./ayarlar/update",
        type: "POST",
        data: JSON.stringify(obj),
        dataType: "JSON",
        contentType: "application/json; charset=utf-8",
        success: function (data){
            sweetAlert(data)
            //console.log(data)
        },
        error: function (err){
            console.log(err)
        }
    })

})
//-------------------------------------- Save or Update User Information - End ----------------------------------------//

//-------------------------------------- Save or Update User Password - Start --------------------------------------//
$('#id_settingsChangePasswordSaveForm').submit(event => {

    event.preventDefault()

    const settingsOldPassword = $('#id_settingsOldPassword').val()
    const settingsNewPassword = $('#id_settingsNewPassword').val()
    const settingsReNewPassword = $('#id_settingsReNewPassword').val()


    const obj = {
        oldPassword: settingsOldPassword,
        newPassword: settingsNewPassword,
        reNewPassword: settingsReNewPassword
    }

    $.ajax({
        url:"./ayarlar/changePassword",
        type: "POST",
        data: JSON.stringify(obj),
        dataType: "JSON",
        contentType: "application/json; charset=utf-8",
        success: function (data){
            sweetAlert(data)
            $('#id_settingsChangePasswordSaveForm').trigger('reset')
            //console.log(data)
        },
        error: function (err){
            console.log(err)
        }
    })

})
//-------------------------------------- Save or Update User Information - End ----------------------------------------//

//-------------------------------------- Get Image Process - Start ----------------------------------------//
function getProfileImage() {
    $.ajax({
        url: '/ayarlar/showImage',
        type: 'GET',
        dataType: 'Json',
        success: function (data) {
            setProfileImage(data.result);
        },
        error: function (err) {
            console.log(err)
        }
    })
}

File.prototype.convertToBase64 = function(callback){

    var FR= new FileReader();
    FR.onload = function(e) {
        callback(e.target.result)
    };
    FR.readAsDataURL(this);
}

function setProfileImage(bytes) {
    $("#id_settingsProfileImage").attr("src", "data:image/*;base64," + bytes);
}

getProfileImage()
//-------------------------------------- Get Image Process - End ------------------------------------------//

//-------------------------------------- Upload Image Process - Start ----------------------------------------//
function imageUpload(formData) {
    $.ajax({
        url: '/ayarlar/uploadImage',
        type: "POST",
        headers: {'IsAjax': 'true'},
        dataType: "json",
        processData: false,
        contentType: false,
        data: formData,
        success: function (data) {
            fncSweetAlert(data)
            $('#id_settingsUploadImageSaveForm').trigger("reset");
            getProfileImage(data.result);
        },
        error: function (err) {
            console.log(err)
        }
    });
}

$('#id_settingsUploadImageSaveForm').submit((event) => {
    event.preventDefault();
    let formData = new FormData();
    let image = document.getElementById("id_settingsUploadImage");
    if(image.files[0] !== undefined){
        formData.append("image", image.files[0]);
        imageUpload(formData);
        $("#id_settingsProfileImageWarning").text("");
    }
});

$('#id_settingsUploadImage').change((event) => {
    event.preventDefault();
    let image_input = document.getElementById("id_settingsUploadImage");
    const image = image_input.files[0];
    if(image !== undefined){
        image.convertToBase64(function (base64){
            $("#id_settingsProfileImage").attr("src", base64);
            $("#id_settingsProfileImageWarning").text("*");
        })
    }
});
//-------------------------------------- Upload Image Process - End ------------------------------------------//

//-------------------------------------- Monthly and Yearly Goal - Start ------------------------------------------//
function fncOptionYear(){
    $("#id_settingsYear").empty()
    for (let i = 0; i < 10; i++) {
        $("#id_settingsYear").append('<option value="'+(2021+i)+'">'+(2021+i)+'</option>')
    }
}
fncOptionYear()

$('#id_settingsMonthlyGoalSaveForm').submit(event => {

    event.preventDefault()

    const monthlyKey = $('#id_settingsMonth').val()
    const monthlyGoal = $('#id_settingsMonthGoal').val()

    const obj = {
        key: monthlyKey,
        goal: monthlyGoal
    }

    $.ajax({
        url:"./ayarlar/saveOrUpdateMonthlyGoal",
        type: "POST",
        data: JSON.stringify(obj),
        dataType: "JSON",
        contentType: "application/json; charset=utf-8",
        success: function (data){
            sweetAlert(data)
            $('#id_settingsMonthlyGoalSaveForm').trigger('reset')
            //console.log(data)
        },
        error: function (err){
            console.log(err)
        }
    })

})

$('#id_settingsYearlyGoalSaveForm').submit(event => {

    event.preventDefault()

    const yearlyKey = $('#id_settingsYear').val()
    const yearlyGoal = $('#id_settingsYearGoal').val()

    const obj = {
        key: yearlyKey,
        goal: yearlyGoal
    }

    $.ajax({
        url:"./ayarlar/saveOrUpdateYearlyGoal",
        type: "POST",
        data: JSON.stringify(obj),
        dataType: "JSON",
        contentType: "application/json; charset=utf-8",
        success: function (data){
            sweetAlert(data)
            $('#id_settingsYearlyGoalSaveForm').trigger('reset')
            //console.log(data)
        },
        error: function (err){
            console.log(err)
        }
    })

})
//-------------------------------------- Monthly and Yearly Goal - End --------------------------------------------//

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


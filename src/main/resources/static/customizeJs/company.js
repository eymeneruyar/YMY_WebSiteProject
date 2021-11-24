//-------------------------------------- Save Company Information - Start --------------------------------------//
$("#id_companySave").submit((event) => {

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
        address: company_address
    }

    console.log(obj)

    $.ajax({
        url:"./firma/saveOrUpdate",
        type: "POST",
        data: JSON.stringify(obj),
        dataType: "JSON",
        contentType: "application/json; charset=utf-8",
        success: function (data){
            sweetAlert(data)
            console.log(data)
        },
        error: function (err){
            console.log(err)
        }
    })


})
//-------------------------------------- Save Company Information - End ----------------------------------------//

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
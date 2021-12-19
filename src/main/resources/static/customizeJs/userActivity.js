function fncUserActivity(){

    $.ajax({
        url:"/api/userActivity",
        type: 'GET',
        dataType: 'JSON',
        success: function (data){
            //console.log(data)

            $("#id_templateUser").text(data.result.name + ' ' + data.result.surname)

            //Rol ifadeleri düzenlendi.
            if(data.result.roles === "ROLE_ADMIN"){
                $("#id_templateUserRole").text(data.result.roles.replace("ROLE_ADMIN","Admin"))
            }

            //template 'de bulunan avatar resim kısmı
            $.ajax({
                url: '/ayarlar/showImage',
                type: 'GET',
                dataType: 'JSON',
                success: function (data) {
                    $("#id_templateImageProfile").attr("src", "data:image/*;base64," + data.result);
                },
                error: function (err) {
                    console.log(err)
                }
            })

        },
        error: function (err){
            console.log(err)
        }
    })

}
fncUserActivity()
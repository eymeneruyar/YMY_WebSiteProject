

function fncGetServices(){
    $.ajax({
        url: "./hizmetlerimiz/list",
        type: "GET",
        dataType: "JSON",
        contentType : 'application/json; charset=utf-8',
        success: function (data) {
            console.log(data)
            fncListServices(data)
        },
        error: function (err) {
            console.log(err)
        }
    })
}
fncGetServices()

function fncListServices(data){
        let html = ``
        if(data.result.length > 0){
            data.result.forEach(item => {
                html += `<div class="col-lg-4 col-md-6 portfolio-item filter-app">
                <img src="/homePage/assets/img/services/${item.id}/${item.image[0]}" class="img-fluid" alt="">
                <div class="portfolio-info">
                  <h4>${item.category}</h4>
                  <p>${item.title}</p>
                  <a href="/homePage/assets/img/services/${item.id}/${item.image[0]}" data-gallery="portfolioGallery" class="portfolio-lightbox preview-link"><i class="bx bx-plus"></i></a>
                  <a href="/hizmetlerimiz/${item.id}" class="details-link" title="More Details"><i class="bx bx-link"></i></a>
                </div>
              </div>`
            })
        }
        $('#id_homeServices').html(html)
    }


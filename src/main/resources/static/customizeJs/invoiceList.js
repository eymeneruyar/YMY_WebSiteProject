//-------------------------------------- Data Table Configuration - Start --------------------------------------//
function dataTable() {
    $("#id_invoiceAddTable").DataTable( {

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
            search: 'Ara',
            searchPlaceholder: 'İş Emri Ara',
            paginate: {
                // remove previous & next text from pagination
                previous: '&nbsp;',
                next: '&nbsp;'
            }
        }
    } );
    $('div.head-label').html('<h2 class="mb-0">İş Emri Listesi</h2>');
}
dataTable()
//-------------------------------------- Data Table Configuration - End ----------------------------------------//
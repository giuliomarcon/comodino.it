function editQuantityModal(productID) {
    $('#productIdQuantityModal').val(productID);
    $(function () {
        $('#editquantitymodal').modal('toggle');
    });
}

function editPriceModal(productID) {
    $('#productIdPriceModal').val(productID);
    $(function () {
        $('#editpricemodal').modal('toggle');
    });
}

function removeProductModal(productID) {
    $('#productIDRemoveModal').val(productID);
    $(function () {
        $('#removeproductmodal').modal('toggle');
    });
}

function addProductPhotoModal(productID) {
    $('#productIDPhotoModal').val(productID);
    $(function () {
        $('#addproductphotomodal').modal('toggle');
    });
}

$("#addproduct").autocomplete({
    source: function (request, response) {
        $.ajax({
            url: "/searchautocomplete",
            dataType: "json",
            data: {
                term: request.term
            },
            success: function (data) {
                response(data);
            }
        });
    },
    minLength: 3,
    select: function (event, ui) {
    }
});
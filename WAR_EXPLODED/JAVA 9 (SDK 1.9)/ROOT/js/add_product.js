function addNewProductModal(productID) {
    $('#addProductId').val(productID);
    $(function(){$('#addproductmodal').modal('toggle');});

}
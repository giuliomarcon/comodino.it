function openReviewReplyModal(reviewID) {
    //alert("Order: " + orderID + " Product: " + productID + " Shop: " + shopID);
    $('#reviewIdReviewReplyModal').val(reviewID);
    $(function(){$('#openreviewreplymodal').modal('toggle');});
}

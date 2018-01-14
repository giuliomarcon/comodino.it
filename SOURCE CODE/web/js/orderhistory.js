var sstelle = 1;
var pstelle = 1;

function openDisputeModal(orderID, productID, shopID) {
    //alert("Order: " + orderID + " Product: " + productID + " Shop: " + shopID);
    $('#orderIdDisputeModal').val(orderID);
    $('#productIdDisputeModal').val(productID);
    $('#shopIdDisputeModal').val(shopID);
    $(function(){$('#opendisputemodal').modal('toggle');});
}

function openReviewModal(orderID, productID, shopID) {
    //alert("Order: " + orderID + " Product: " + productID + " Shop: " + shopID);
    $('#orderIdReviewModal').val(orderID);
    $('#productIdReviewModal').val(productID);
    $('#shopIdReviewModal').val(shopID);
    $(function(){$('#openreviewmodal').modal('toggle');});

}

function setSStar(id)
{
    var sstelle_hover = id.id.substr(8,id.id.lenght);

    for (i = 0; i <= sstelle_hover; i++)
        $("#sstella_" + i).attr("class","fa fa-star rating_star");

    for (i = parseInt(sstelle_hover)+1; i <= 5; i++)
        $("#sstella_" + i).attr("class","fa fa-star-o rating_star");

    sstelle = sstelle_hover;

    $('#openreviewmodal input[name="srating"]').val(sstelle);
    console.log(sstelle);
}

function setPStar(id)
{
    var pstelle_hover = id.id.substr(8,id.id.lenght);

    for (i = 0; i <= pstelle_hover; i++)
        $("#pstella_" + i).attr("class","fa fa-star rating_star");

    for (i = parseInt(pstelle_hover)+1; i <= 5; i++)
        $("#pstella_" + i).attr("class","fa fa-star-o rating_star");

    pstelle = pstelle_hover;

    $('#openreviewmodal input[name="prating"]').val(pstelle);
    console.log(pstelle);
}

$("#opendisputeform").validate();
$( "#titleDisputeModal").rules( "add", {
    required: true,
    minlength: 3,
    maxlength: 100
});
$( "#descriptionDisputeModal").rules( "add", {
    required: true,
    minlength: 3,
    maxlength: 500
});

$("#openreviewform").validate();
$( "#titleReviewModal").rules( "add", {
    required: true,
    minlength: 3,
    maxlength: 100
});
$( "#descriptionReviewModal").rules( "add", {
    required: true,
    minlength: 3,
    maxlength: 500
});

jQuery.extend(jQuery.validator.messages, {
    required: jQuery.validator.format("Questo campo è obbligatorio"),
    minlength: jQuery.validator.format("Sono necessari almeno {0} caratteri"),
    maxlength: jQuery.validator.format("Sono permessi al massimo {0} caratteri")
});

$(document).ready(function() {
    var ordini = $("#OrdiniInCorso ul.list-group");
    ordini.each( function () {
        if (!$.trim( $(this).html() ).length){
            $(this).html("<h4 class='text-center'>Tutti i prodotti di quest'ordine sono già stati ritirati</h4>");
        }
    });
    ordini = $("#OrdiniCompletati ul.list-group");
    ordini.each( function () {
        if (!$.trim( $(this).html() ).length){
            $(this).html("<h4 class='text-center'>Tutti i prodotti di quest'ordine sono ancora da ritirare</h4>");
        }
    });
});

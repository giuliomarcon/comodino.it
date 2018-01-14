var i_quanti = 10;

function openModal(titolo)
{
    $("#content-modal-vendors").empty();
    $("#card_titolo_vendors").text(titolo);


    var post = {nome_prodotto:titolo,quanti:i_quanti,offset:0,getQuantity:"0"};
    $.post( "/getVendorServlet", post)
        .done(function( data ) {
            $("#content-modal-vendors").html(data);

            $('#vendorsModal').modal('show');
        });

    $.post( "/getVendorServlet", {nome_prodotto:titolo,getQuantity:"1"})
        .done(function( quantity ) {
            if (quantity < i_quanti)
                i_quanti = quantity;

            $("#pagination_numbers_vendors").html("<h6 style=\"color:#ffffff\">Mostrando i risultati 1-" + i_quanti + " su " + quantity + "</h6>");
            $("#footer_vendors").html("<div class=\"row\">" +
                "<div class=\"col-md-6\">" +

                "</div>" +
                "<div class=\"col-md-6\">" +
                "<a href=\"#\" onclick=\"addOffset('" + titolo + "'," + 0 + ",'" + quantity + "')\">Mostra ancora&nbsp&nbsp<i class=\"fa fa-caret-right\" aria-hidden=\"true\"></i></a>" +
                "</div>" +
                "</div>");
        });
}

function addOffset(titolo,offset,tot)
{
    offset += i_quanti;
    if ( (offset + i_quanti) <= tot)
    {
        $("#footer_vendors").html("<div class=\"row\">" +
            "<div class=\"col-md-6 col-xs-6\">" +
            "<a href=\"#\" onclick=\"lessOffset('" + titolo + "'," + offset + ",'" + tot + "')\"><i class=\"fa fa-caret-left\" aria-hidden=\"true\"></i>&nbsp&nbspMostra meno\n</a>" +
            "</div>" +
            "<div class=\"col-md-6 col-xs-6\">" +
            "<a href=\"#\" onclick=\"addOffset('" + titolo + "'," + offset + ",'" + tot + "')\">Mostra ancora&nbsp&nbsp<i class=\"fa fa-caret-right\" aria-hidden=\"true\"></i></a>" +
            "</div>" +
            "</div>");
    }
    else
    {
        $("#footer_vendors").html("<div class=\"row\">" +
            "<div class=\"col-md-6\">" +
            "<a href=\"#\" onclick=\"lessOffset('" + titolo + "'," + offset + ",'" + tot + "')\"><i class=\"fa fa-caret-left\" aria-hidden=\"true\"></i>&nbsp&nbspMostra meno\n</a>" +
            "</div>" +
            "<div class=\"col-md-6\">" +
            "</div>" +
            "</div>");
    }

    var post = {nome_prodotto:titolo,quanti:i_quanti,offset:offset,getQuantity:"0"};
    $.post( "/getVendorServlet", post)
        .done(function( data ) {
            $("#content-modal-vendors").html(data);
            var fine_offset = (offset+i_quanti) > tot ? tot : (offset+i_quanti);
            $("#pagination_numbers_vendors").html("<h6 style=\"color:#ffffff\">Mostrando i risultati " + (offset+1) + "-" + fine_offset + " su " + tot + "</h6>");
        });
}

function lessOffset(titolo,offset,tot)
{
    offset -= i_quanti;

    if (offset == 0) {
        $("#footer_vendors").html("<div class=\"row\">" +
            "<div class=\"col-md-6\">" +
            "</div>" +
            "<div class=\"col-md-6\">" +
            "<a href=\"#\" onclick=\"addOffset('" + titolo + "'," + offset + ",'" + tot + "')\">Mostra ancora&nbsp&nbsp<i class=\"fa fa-caret-right\" aria-hidden=\"true\"></i></a>" +
            "</div>" +
            "</div>");
    }
    else
    {
        $("#footer_vendors").html("<div class=\"row\">" +
            "<div class=\"col-md-6\">" +
            "<a href=\"#\" onclick=\"lessOffset('" + titolo + "'," + offset + ",'" + tot + "')\"><i class=\"fa fa-caret-left\" aria-hidden=\"true\"></i>&nbsp&nbspMostra meno\n</a>" +
            "</div>" +
            "<div class=\"col-md-6\">" +
            "<a href=\"#\" onclick=\"addOffset('" + titolo + "'," + offset + ",'" + tot + "')\">Mostra ancora&nbsp&nbsp<i class=\"fa fa-caret-right\" aria-hidden=\"true\"></i></a>" +
            "</div>" +
            "</div>");
    }

    var post = {nome_prodotto:titolo,quanti:i_quanti,offset:offset,getQuantity:"0"};
    $.post( "/getVendorServlet", post)
        .done(function( data ) {
            $("#content-modal-vendors").html(data);
            $("#pagination_numbers_vendors").html("<h6 style=\"color:#ffffff\">Mostrando i risultati " + (offset+1) + "-" + (offset+i_quanti) + " su " + tot + "</h6>");
        });

}

function scrollToAnchor(aid){
    var aTag = $("#"+ aid);
    $('html,body').animate({scrollTop: aTag.offset().top},'slow');
}

$("#tomap").click(function() {
    scrollToAnchor('map');
});


function goToAnchor(){
    if(window.location.hash) {
        scrollToAnchor("reviewanchor");
    }
}

$(document).ready(function() {
    goToAnchor();
});

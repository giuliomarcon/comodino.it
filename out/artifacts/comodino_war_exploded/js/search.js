var i_quanti = 10;
var stelle = 0;

$(document).ready(function() {
    showApply();
    setPriceOrder();
});

function setPriceOrder()
{
    var url = (window.location.href).replace("#","");

    if (url.indexOf("sort=price-desc") !== -1)
        $("#priceOrderIcon").attr("class","fa fa-arrow-down rotate");
    else if (url.indexOf("sort=price-asc") !== -1)
        $("#priceOrderIcon").attr("class","fa fa-arrow-up rotate");
}

function changePriceOrder()
{
    var url = (window.location.href).replace("#","");

    $(".rotate").toggleClass("down");
    setTimeout(function () {
        if (url.indexOf("sort=price-desc") !== -1)
            window.location.href = updateURLParameter(url,"sort","price-asc");
        else if (url.indexOf("sort=price-asc") !== -1)
            window.location.href = updateURLParameter(url,"sort","price-desc");
        else
            window.location.href = updateURLParameter(url,"sort","price-desc");
    }, 100);
}

function showApply() {

    var pricemax = $('#PriceMax');
    var pricemin = $('#PriceMin');
    var pricebutton = $('#PriceButton');
    var url = window.location.href;

    if( $(pricemax).val() === "" && $(pricemin).val() === "" && url.indexOf("minPrice") === -1 && url.indexOf("maxPrice") === -1){
        $(pricebutton).removeClass("animated fadeInDown");
        $(pricebutton).addClass("animated fadeOutDown");
        setTimeout(function(){ $(pricebutton).css("display", "none"); }, 1000);
    }
    else{
        $(pricebutton).css("display", "initial");
        $(pricebutton).removeClass("animated fadeOutDown");
        $(pricebutton).addClass("animated fadeInDown");
    }
}
function filterPrice() {
    var pricemax = $('#PriceMax').val();
    var pricemin = $('#PriceMin').val();
    var url = (window.location.href).replace("#","");

    if(pricemin !== "")
        url = updateURLParameter(url, "minPrice", pricemin);
    else {
        url = updateURLParameter(url, "minPrice", "");
        url = url.replace("&minPrice=","");
    }

    if(pricemax !== "")
        url = updateURLParameter(url, "maxPrice", pricemax);
    else {
        url = updateURLParameter(url, "maxPrice", "");
        url = url.replace("&maxPrice=","");
    }

    window.location.href = url;
}

function filter(elem,tipo)
{
    var url = (window.location.href).replace("#","");
    var uri = "&" + tipo + "=" + encodeURI(elem.name);
    if (elem.checked === false)
        window.location.href = url + uri;
    else
    {
        window.location.href = url.replace(uri,"");
    }
}

function filterRadio(elem,tipo)
{
    var url = (window.location.href).replace("#","");
    var uri = "&" + tipo + "=" + encodeURI(elem.name);
    if (elem.checked == true && (url.indexOf(uri) == -1) )
        window.location.href = updateURLParameter(url, tipo, elem.name);
    else
    {
        window.location.href = url.replace(uri,"");
    }
}

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

function resetStar()
{
    stelle = 0;
    for (i = 0; i <= 5; i++)
        $("#stella_" + i).attr("class","fa fa-star-o rating_star");
}

function setStar(id)
{
    stelle_hover = id.id.substr(7,id.id.lenght);

    for (i = 0; i <= stelle_hover; i++)
        $("#stella_" + i).attr("class","fa fa-star rating_star");

    for (i = parseInt(stelle_hover)+1; i <= 5; i++)
        $("#stella_" + i).attr("class","fa fa-star-o rating_star");

    stelle = stelle_hover;
}

function setStarFilter()
{
    var url = (window.location.href).replace("#","");
    window.location.href = updateURLParameter(url,"minRat",(parseInt(stelle)+1));

}

function updateURLParameter(url, param, paramVal)
{
    var TheAnchor = null;
    var newAdditionalURL = "";
    var tempArray = url.split("?");
    var baseURL = tempArray[0];
    var additionalURL = tempArray[1];
    var temp = "";

    if (additionalURL)
    {
        var tmpAnchor = additionalURL.split("#");
        var TheParams = tmpAnchor[0];
        TheAnchor = tmpAnchor[1];
        if(TheAnchor)
            additionalURL = TheParams;

        tempArray = additionalURL.split("&");

        for (var i=0; i<tempArray.length; i++)
        {
            if(tempArray[i].split('=')[0] != param)
            {
                newAdditionalURL += temp + tempArray[i];
                temp = "&";
            }
        }
    }
    else
    {
        var tmpAnchor = baseURL.split("#");
        var TheParams = tmpAnchor[0];
        TheAnchor  = tmpAnchor[1];

        if(TheParams)
            baseURL = TheParams;
    }

    if(TheAnchor)
        paramVal += "#" + TheAnchor;

    var rows_txt = temp + "" + param + "=" + paramVal;
    return baseURL + "?" + newAdditionalURL + rows_txt;
}
function addToCart(productID,shopID)
{
    var post = {productID:productID, shopID:shopID};
    $.post( "/addcartitem", post)
        .done(function () {
            $.post("/getcart", {type:"drop"})
                .done(function(data) {
                    $("#cartdrop").html(data);
                    $.post("/getcart", {type:"header"})
                        .done(function(data) {
                            $("#cartheader").html(data);
                        });
                });
        });


    //window.location.href = document.location + "&success=Prodotto aggiunto al carrello";
    //var valore = $("#cart-size").text();
    //$("#cart-size").text( parseInt(valore)+1 );
}
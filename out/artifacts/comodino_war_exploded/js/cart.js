jQuery('').insertAfter('.quantity input');
jQuery(".quantity").each(function () {
    var spinner = jQuery(this),
        input = spinner.find('input[type="number"]'),
        btnUp = spinner.find('.quantity-up'),
        btnDown = spinner.find('.quantity-down'),
        min = input.attr('min'),
        max = input.attr('max');

    btnUp.click(function () {
        var oldValue = parseFloat(input.val());
        if (oldValue >= max) {
            var newVal = oldValue;
        } else {
            var newVal = oldValue + 1;
        }
        spinner.find("input").val(newVal);
        spinner.find("input").trigger("change");
    });

    btnDown.click(function () {
        var oldValue = parseFloat(input.val());
        if (oldValue <= min) {
            var newVal = oldValue;
        } else {
            var newVal = oldValue - 1;
        }
        spinner.find("input").val(newVal);
        spinner.find("input").trigger("change");
    });

});

function removeItem(prodID, shopID) {
    $.post("/removecartitem", {"productID": prodID, "shopID": shopID}).done(function () {
        $("#" + prodID + "_" + shopID).fadeOut("700", function () {
            updateCart();
            updateTotal();

            if (isCartEmpty()) {
                $("#cartlist").html("<li class=\"list-group-item text-center nessunbordo\"><h3>Il carrello è vuoto, aggiungi qualche prodotto!</h3></li>");
                $("#nextbtn").text("Vai alla homepage");
                $("#nextbtn").attr("href", "/index.jsp");
            }
        });
    });
}

function isCartEmpty() {
    var items = $(".cart-item");
    var isempty = true;
    $(items).each(function (index, item) {
        if ($(item).is(":visible")) {
            isempty = false;
        }
    });
    return isempty;
}

function updatePrice(prodID, tipo, shopID) {
    var quantita = $("#quantity_" + prodID + "_" + shopID).val();
    var post = {productID: prodID, shopID: shopID};
    if (tipo === "+") {
        //aggiunto = (parseFloat(attuale) + parseFloat(aggiungere)).toFixed(2);
        $.post("/addcartitem", post).done(function () {
            updateCart();
            updateTotal();
        });
    }
    else if (tipo === "-" && quantita > 1) {
        //aggiunto = (parseFloat(attuale) - parseFloat(aggiungere)).toFixed(2);
        $.post("/decreasecartitem", post).done(function () {
            updateCart();
            updateTotal();
        });
    }
}

function setQuantity(prodID, shopID, value) {
    var post = {productID: prodID, shopID: shopID, quantity: value};
    if (value !== null && value > 0) {
        $.post("/restricted/setcartitem", post).done(function () {
            updateCart();
            updateTotal();
        });
    }
}

function updateTotal() {
    var items = $(".cart-item");
    var totale = 0.0;
    $(items).each(function (index, item) {
        var prezzo_item = parseFloat($(item).find(".itemprice").text().slice(0, -1).replace(",", ".")).toFixed(2);
        var quantita_item = parseInt($(item).find(".quantity>input").val());
        if (quantita_item <= 0) {
            quantita_item = parseInt($(item).find(".quantity>input").attr("value"));
        }
        if ($(item).is(":visible")) {
            //console.log(prezzo_item, quantita_item);
            totale += prezzo_item * quantita_item;
        }
        else {
            //console.log("Nascosto: ", item);
        }
    });
    totale = totale.toFixed(2);
    $("#total").text("Totale: " + totale + "€");
}

function updateCart() {
    $.post("/getcart", {type: "header"})
        .done(function (data) {
            $("#cartheader").html(data);
        });
    $.post("/getcart", {type: "drop"})
        .done(function (data) {
            $("#cartdrop").html(data);
        });
}

$(document).ready(function () {
    var allQuantityBoxes = $("input[type=\"number\"]");
    $(allQuantityBoxes).each(function (index, value) {
        $(value).focusout(function () {
            setQuantity($(value).attr("data-prod"), $(value).attr("data-shop"), $(value).val());
        });
    });
    updateTotal();
});

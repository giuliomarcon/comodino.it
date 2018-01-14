$("#createShopForm").validate();
$( "#shop-name" ).rules( "add", {
    required: true,
    minlength: 3,
    maxlength: 16,
    messages: {
        required: "Questo campo è obbligatorio",
    }
});
$( "#shop-description" ).rules( "add", {
    required: true,
    minlength: 5,
    maxlength: 100,
    messages: {
        required: "Questo campo è obbligatorio",
    }
});
$( "#shop-website" ).rules( "add", {
    required: true,
    url: true,
    minlength: 3,
    maxlength: 100,
    messages: {
        required: "Questo campo è obbligatorio",
        url: "Inserisci un link valido",
    }
});
$( "#shop-address" ).rules( "add", {
    required: {
        depends: function () {
            return $("#shop-city").val() ||
                $("#shop-state").val() ||
                $("#shop-ZIP").val() ||
                $("#shop-openingHours").val();
        }
    },
    minlength: 3,
    maxlength: 100,
    messages: {
        required: "Questo campo è obbligatorio se hai compilato altri campi del negozio fisico",
    }
});
$( "#shop-city" ).rules( "add", {
    required: {
        depends: function () {
            return $("#shop-address").val() ||
                $("#shop-state").val() ||
                $("#shop-ZIP").val() ||
                $("#shop-openingHours").val();
        }
    },
    minlength: 3,
    maxlength: 100,
    messages: {
        required: "Questo campo è obbligatorio se hai compilato altri campi del negozio fisico",
    }
});
$( "#shop-state" ).rules( "add", {
    required: {
        depends: function () {
            return $("#shop-address").val() ||
                $("#shop-city").val() ||
                $("#shop-ZIP").val() ||
                $("#shop-openingHours").val();
        }
    },
    messages: {
        required: "Seleziona lo stato dove è situato il negozio",
    }
});
$( "#shop-ZIP" ).rules( "add", {
    required: {
        depends: function () {
            return $("#shop-address").val() ||
                $("#shop-state").val() ||
                $("#shop-city").val() ||
                $("#shop-openingHours").val();
        }
    },
    minlength: 3,
    maxlength: 100,
    messages: {
        required: "Questo campo è obbligatorio se hai compilato altri campi del negozio fisico",
    }
});
$( "#shop-openingHours" ).rules( "add", {
    required: {
        depends: function () {
            return $("#shop-address").val() ||
                $("#shop-state").val() ||
                $("#shop-city").val() ||
                $("#shop-ZIP").val();
        }
    },
    minlength: 3,
    maxlength: 100,
    messages: {
        required: "Questo campo è obbligatorio se hai compilato altri campi del negozio fisico",
    }
});
jQuery.extend(jQuery.validator.messages, {
    minlength: jQuery.validator.format("Sono necessari almeno {0} caratteri"),
    maxlength: jQuery.validator.format("Sono permessi al massimo {0} caratteri")
});
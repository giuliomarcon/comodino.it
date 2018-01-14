
var cleaveCreditCard = new Cleave('.input-credit-card', {
    creditCard:              true,
    onCreditCardTypeChanged: function (type) {

        $(".mycard.active").removeClass('active');

        $("." + type).addClass('active');
    }
});
$("#PaymentForm").validate();
$( "#card-holder-name" ).rules( "add", {
    required: true,
    messages: {
        required: "Questo campo è obbligatorio"
    }
});
$( "#card-number" ).rules( "add", {
    required: true,
    messages: {
        required: "Questo campo è obbligatorio"
    }
});
$( "#cvv" ).rules( "add", {
    required: true,
    digits: true,
    minlength: 3,
    maxlength: 3,
    messages: {
        required: "Questo campo è obbligatorio",
        minlength: "Sono richieste {0} cifre",
        maxlength: "Sono richieste {0} cifre",
        digits: "Sono ammesse solo cifre"

    }
});
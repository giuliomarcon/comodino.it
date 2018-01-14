$( document ).ready(function() {
    $(".tile").height($("#tile1").width());
    $(".carousel").height($("#tile1").width());
    $(".item").height($("#tile1").width());

    $(window).resize(function() {
        if(this.resizeTO) clearTimeout(this.resizeTO);
        this.resizeTO = setTimeout(function() {
            $(this).trigger('resizeEnd');
        }, 10);
    });

    $(window).bind('resizeEnd', function() {
        $(".tile").height($("#tile1").width());
        $(".carousel").height($("#tile1").width());
        $(".item").height($("#tile1").width());
    });

    $('.tile.bagno').click(function () {
        window.location.href = "/search?cat=Bagno&q=";
    });
    $('.tile.garden').click(function () {
        window.location.href = "/search?cat=Balcone+e+Giardino&q=";
    });
    $('.tile.kitchen').click(function () {
        window.location.href = "/search?cat=Cucina&q=";
    });
    $('.tile.studio').click(function () {
        window.location.href = "/search?cat=Studio+e+Ufficio&q=";
    });
    $('.tile.livingroom').click(function () {
        window.location.href = "/search?cat=Soggiorno&q=";
    });
    $('.tile.illumination').click(function () {
        window.location.href = "/search?cat=Illuminazione&q=";
    });
    $('.tile.bedroom').click(function () {
        window.location.href = "/search?cat=Camera+da+Letto&q=";
    });

    var url = window.location.href;
    if (url.indexOf("?token=") >= 0)
    {
        $('#resetpasswordmodal').modal('show');
    }
});

function check_pws() {
    var pwa = $("#pwda").val();
    var pwb = $("#pwdb").val();

    var re = new RegExp("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{6,}$"); // minimo 6 caratteri, una maiuscola, una minuscola e un numero

    if ( pwa !== "" && pwb !== "" && pwa !== pwb )
    {
        $("#error_message").css("display","initial");
        $("#error_message").text("Le password non coincidono");
        $("#error_message").removeClass("animated fadeOut");
        $("#error_message").addClass("animated fadeIn");
        $("#doButtonReset").css('pointer-events',"none");
        $("#doButton").addClass("disabled");
    }
    else if ( pwa !== "" && pwb !== "" && pwa === pwb )
    {
        $("#error_message").addClass("animated fadeOut");
        setTimeout(function(){ $("#error_message").css("display", "none"); }, 500);
        $("#doButtonReset").css('pointer-events',"initial");
        $("#doButton").removeClass("disabled");
    }
    else if (!re.test(pwa) || !re.test(pwb)) {
        $("#error_message").css("display","initial");
        $("#error_message").text("La password deve contere almeno 6 caratteri, una maiuscola, una minuscola e un numero");
        $("#error_message").removeClass("animated fadeOut");
        $("#error_message").addClass("animated fadeIn");
        $("#doButtonReset").css('pointer-events',"none");
        $("#doButton").addClass("disabled");
    } else {
        $("#error_message").addClass("animated fadeOut");
        setTimeout(function(){ $("#error_message").css("display", "none"); }, 500);
        $("#doButtonReset").css('pointer-events',"initial");
        $("#doButton").removeClass("disabled");
    }
}



/*
$("#form").validate({
    rules: {
        email: {
            required: true,
            email: true
        },
        password: {
            required: {
                depends: function () {
                    return $("#form").attr("action") !== "/passwordRequest";
                }
            }
        },
        firstname: {
            required: {
                depends: function () {
                    return $("#form").attr("action") === "/register";
                }
            }
        },
        lastname: {
            required: {
                depends: function () {
                    return $("#form").attr("action") === "/register";
                }
            }
        }
    },
    messages: {
        email: {
            required: "Questo campo è obbligatorio",
            email: "Inserisci un'email valida"
        },
        password: "Questo campo è obbligatorio",
        firstname: "Inserisci il tuo nome",
        lastname: "Inserisci il tuo cognome"
    }
});
jQuery.validator.setDefaults({
    debug: true,
    success: "valid"
});
jQuery.extend(jQuery.validator.messages, {
    email: jQuery.validator.format("Inserisci una mail valida")
});
*/
/*



$("#signupform")
    .validate({
        rules: {
            password: "required",
            postcode: {
                required: true,
                minlength: 3
            }
        },
        messages: {
            password: "Field Password is required",
            postcode: {
                required: "Field PostCode is required",
                minlength: "Field PostCode must contain at least 3 characters"
            }
    });


$("#resetpasswordform").validate({
    rules: {
        pwda: "required",
        pwdb: {
            equalTo: "#password"
        }
    }
});
jQuery.validator.setDefaults({
    debug: true,
    success: "valid"
});
jQuery.extend(jQuery.validator.messages, {
    equalTo: jQuery.validator.format("Le password non combaciano")
});
*/


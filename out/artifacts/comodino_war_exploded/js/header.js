var first = true;

$(function(){

    $(".input-group-btn .dropdown-menu li a").click(function(){

        var selText = $(this).html();
        console.log("#"+selText.split(' ').join('')+"-radio");
        var radio = $("#"+selText.split(' ').join('')+"-radio");
        if(selText === "Tutte le categorie"){
            $('input[name="cat"]').prop('checked', false);
        }
        else {
            radio.prop("checked", true);
        }
        selText += "&nbsp;&nbsp;<span class=\"caret\"></span>";
        //working version - for multiple buttons //
        $(this).parents('.input-group-btn').find('.btn-search').html(selText);

    });

});

function openLoginModal()
{
    $('#loginsignupforgotheader').data('validator', null);
    $("#loginsignupforgotheader").validate({
        rules: {
            email: {
                required: true,
                email: true
            },
            password: "required"
        },
        messages: {
            email: {
                required: "Questo campo è obbligatorio",
                email: "Inserisci un'email valida"
            },
            password: "Questo campo è obbligatorio"
        }
    });

    $(".nologin").css("visibility","hidden");
    $('#LoginSignup').modal('show');
    if (first === false)
        show_login(0);
}

function openSignupModal()
{
    $('#LoginSignup').modal('show');
    show_signup(0);
    first = false;
}


$.validator.methods.email = function( value, element ) {
    return this.optional( element ) || /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/.test( value );
}

function show_login(vel)
{
    $('#loginsignupforgotheader').data('validator', null);
    $("#loginsignupforgotheader").validate({
        rules: {
            email: {
                required: true,
                email: true
            },
            password: "required"
        },
        messages: {
            email: {
                required: "Questo campo è obbligatorio",
                email: "Inserisci un'email valida"
            },
            password: "Questo campo è obbligatorio"
        }
    });

    $('.nologin').each(function () {
        var v = $(this);
        v.animate({opacity: 0}, vel, function () {
            $(".nologin").css("visibility","hidden");
        });
    });

    $(".yeslogin").animate({opacity:1},vel);
    $('.login').animate({marginTop:'-100px'}, vel);
    $('#card_titolo').animate({opacity: 0}, vel, function () {
        $('#card_titolo').text("Login");
        $('#card_titolo').animate({opacity: 1}, vel);
    });
    $('#card_change_button').html('Non hai ancora un account? <a href="#" onclick="show_signup(500);">Registrati</a>');
    $('#card_forgot_button').css("display","initial");
    $('#card_forgot_button').animate({opacity:1},vel);
    $('#card_forgot_button').removeClass('fadeOut');
    $('#loginsignupforgotheader').attr('action','/login');
    $("#doButton").text("Entra");
    $("#card_forgot_button").html('Hai dimenticato la <a onclick="show_forgot();" style="cursor:pointer">password</a>?');
    $("#doButton").css('pointer-events',"initial");
    $("#doButton").removeClass("disabled");

    $( "#pw" ).unbind("keyup");
}

function show_signup(vel)
{
    $('#loginsignupforgotheader').data('validator', null);
    $("#loginsignupforgotheader").validate({
        rules: {
            email: {
                required: true,
                email: true
            },
            password: "required",
            firstname: "required",
            lastname: "required"
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

    $("#doButton").css('pointer-events',"none");
    $(".nologin").css("visibility","initial");
    $('.noforgot').each(function () {
        var v = $(this);
        v.css("display","table");
        v.animate({opacity: 1}, vel);
    });
    $('.nologin').each(function () {
        var v = $(this);
        v.animate({opacity: 1}, vel);
    });
    $('.login').animate({marginTop:'0px'}, vel);
    $('#card_titolo').animate({marginBottom:'50px'}, vel);
    $('#card_titolo').animate({opacity: 0}, vel, function () {
        $('#card_titolo').text("Registrati");
        $('#card_titolo').animate({opacity: 1}, vel);
        //$('#card_titolo').animate({marginBottom:'50px'}, 500);
    });
    $('#card_change_button').html('Hai già un account? <a href="#" onclick="show_login(500);">Loggati</a>');
    /*
    $('#card_forgot_button').animate({opacity:0},vel, function () {
        $('#card_forgot_button').css("display","none");
    });
    */
    $('#loginsignupforgotheader').attr('action','/register');
    $("#doButton").text("Registrati");

    checkre()
    $( "#pw" ).bind( "keyup", function () {
        checkre();
    });
}

function checkre()
{
    var re = new RegExp("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{6,}$"); // minimo 6 caratteri, una maiuscola, una minuscola e un numero
    var v = $('#pw').val();
    if (!re.test(v)) {
        $("#card_forgot_button").css("display","initial");
        $("#card_forgot_button").text("La password deve contere almeno 6 caratteri, una maiuscola, una minuscola e un numero");
        $("#card_forgot_button").removeClass("animated fadeOut");
        $("#card_forgot_button").addClass("animated fadeIn");
        $("#doButton").css('pointer-events',"none");
        $("#doButton").addClass("disabled");
    } else {
        $("#card_forgot_button").addClass("animated fadeOut");
        setTimeout(function(){ $("#card_forgot_button").css("display", "none"); }, 500);
        $("#doButton").css('pointer-events',"initial");
        $("#doButton").removeClass("disabled");
    }
}

function show_forgot(vel)
{
    $('#loginsignupforgotheader').data('validator', null);
    $("#loginsignupforgotheader").validate({
        rules: {
            email: {
                required: true,
                email: true
            }
        },
        messages: {
            email: {
                required: "Questo campo è obbligatorio",
                email: "Inserisci un'email valida"
            }
        }
    });

    $(".nologin").css("visibility","hidden");
    $(".yeslogin").animate({opacity:0,marginTop:"-40px"},vel, function () {
        $(".noforgot").css("margin-right","10000px");
    });

    $('#card_titolo').animate({opacity: 0}, 500, function () {
        $('#card_titolo').text("Inserisci la mail");
        $('#card_titolo').animate({opacity: 1}, 500);
    });

    $('#card_change_button').html('Hai già un account? <a href="#" onclick="show_login_from_forgot(500);">Loggati</a>');
    $('#card_forgot_button').animate({opacity:0},vel, function () {
        $('#card_forgot_button').css("display","none");
    });

    $('#loginsignupforgotheader').attr('action','/passwordRequest');
    $("#doButton").text("Invia");
    $( "#pw" ).unbind("keyup");
}

function show_login_from_forgot(vel)
{
    $('#loginsignupforgotheader').data('validator', null);
    $("#loginsignupforgotheader").validate({
        rules: {
            email: {
                required: true,
                email: true
            },
            password: "required"
        },
        messages: {
            email: {
                required: "Questo campo è obbligatorio",
                email: "Inserisci un'email valida"
            },
            password: "Questo campo è obbligatorio"
        }
    });

    $(".nologin").css("pointer-events","none");
    $(".noforgot").css("margin-right","0px");
    $(".yeslogin").animate({marginTop:"0px",opacity:1},vel);
    $('#card_titolo').animate({opacity: 0}, vel/2, function () {
        $('#card_titolo').text("Login");
        $('#card_titolo').animate({opacity: 1}, vel/2);
    });
    $('#card_change_button').html('Non hai ancora un account? <a href="#" onclick="show_signup(500);">Registrati</a>');
    $('#card_forgot_button').css("display","initial");
    $('#card_forgot_button').animate({opacity:1},vel);
    $('#loginsignupforgotheader').attr('action','/login');
    $("#doButton").text("Entra");
    $( "#pw" ).unbind("keyup");
}

function openCart() {

    $.post("/getcart", {type:"drop"})
        .done(function(data) {
            $("#cartdrop").html(data);
        });
    $.post("/getcart", {type:"header"})
        .done(function(data) {
            $("#cartheader").html(data);
        });
}

function readNotifications() {

    $.post("/restricted/readnotifications")
        .done(function(data) {
        });
}

$("#search").autocomplete({
    source: function (request, response) {
        $.ajax({
            url: "/searchautocomplete",
            dataType: "json",
            data: {
                term: request.term
            },
            success: function (data) {
                response(data);
            }
        });
    },
    minLength: 2,
    select: function (event, ui) {
    }
});

$("#searchMobile").autocomplete({
    source: function (request, response) {
        $.ajax({
            url: "/searchautocomplete",
            dataType: "json",
            data: {
                term: request.term
            },
            success: function (data) {
                response(data);
            }
        });
    },
    minLength: 2,
    select: function (event, ui) {
    }
});

$(document).ready(function() {
    $("#popup").animate({opacity: 1}, 800);
    setTimeout(function() {
        $("#popup").fadeOut();
    }, 6000);

    $('#signup_login_card').keypress(function (e) {
        var key = e.which;
        if(key === 13)  // invio
            $('#loginsignupforgotheader').submit();
    });

    $('#searchMobile').keypress(function (e) {
        var key = e.which;
        if(key === 13)  // invio
            doSearchMobile();
    });
});

function doSearchMobile() {
    window.location.href = "/search?q=" + ($("#searchMobile").val());
}

function createNotificationURL(reviewType, contextPath, shopID){
    var URL="#";

    if (reviewType == 'NotificationProductReview'){
        URL = contextPath + "/restricted/vendor/reviews.jsp";
    } else if (reviewType == 'NotificationShopReview'){
        URL = contextPath + "/shop.jsp?id=" + shopID;
    } else if (reviewType == 'NotificationDispute'){
        URL = contextPath + "/restricted/vendor/dispute_list.jsp";
    }

    return URL;
}

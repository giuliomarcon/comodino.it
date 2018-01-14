$(function(){

    $("form .btn-group .dropdown-menu li a").click(function(){

        var selText = $(this).text();
        var radio = $(this).parents("li").find('input');
        radio.prop("checked", true);
        selText += "&nbsp;&nbsp;<span class=\"caret\"></span>";

        $(this).parents('.btn-group').find('button[data-toggle="dropdown"]').html(selText);
        $(this).parents('.btn-group').find('button[data-toggle="salva"]').show();

    });

});

function filtraInAttesa() {
    $(".disputa.terminata").fadeOut(600);
    $("#filtrodispute").text("Vedi tutte le dispute");
    $("#filtrodispute").attr("onclick", "filtraTutte()");
}
function filtraTutte() {
    $(".disputa.terminata").fadeIn(800);
    $("#filtrodispute").text("Vedi solo dispute aperte");
    $("#filtrodispute").attr("onclick", "filtraInAttesa()");
}
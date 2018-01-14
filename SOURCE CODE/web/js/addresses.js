function removeAddress(addressID) {
    $.ajax({
        type: "POST",
        url: "/restricted/removeaddress",
        data: {
            'addressID': addressID
        },
        success: function(result){
            $("#"+addressID).fadeOut(300);
        }
    });
}

function editAddress(addressID) {
    $("[data-address='input']").removeClass("hidden");
    var editButton = $("a[data-id='editAddress']");
    editButton.attr('onclick',"saveAddress(" + addressID + ")");
    editButton.removeClass("btn-default");
    editButton.addClass("btn-success");
    editButton.find("i").addClass("fa-save");editButton.find("i").removeClass("fa-pencil");
    var removeButton = $("a[data-id='removeAddress']");
    removeButton.attr('onclick',"backAddress(" + addressID + ")");
    removeButton.find("i").addClass("fa-times"); removeButton.find("i").removeClass("fa-trash");
    $("[data-address='address']").addClass("hidden");
}

function saveAddress(addressID) {
    $("[data-address='input']").addClass("hidden");
    var  editButton = $("a[data-id='editAddress']");
    editButton.attr('onclick',"editAddress(" + addressID + ")");
    editButton.removeClass("btn-success");
    editButton.addClass("btn-default");
    editButton.find("i").addClass("fa-pencil"); editButton.find("i").removeClass("fa-save");
    $("#"+addressID+"-form").submit();
    var removeButton = $("a[data-id='removeAddress']");
    removeButton.attr('onclick',"removeAddress(" + addressID + ")");
    removeButton.find("i").addClass("fa-trash"); removeButton.find("i").removeClass("fa-times");
    $("[data-address='address']").removeClass("hidden");
}

function backAddress(addressID) {
    $("[data-address='input']").addClass("hidden");
    var  editButton = $("a[data-id='editAddress']");
    editButton.attr('onclick',"editAddress(" + addressID + ")");
    editButton.removeClass("btn-success");
    editButton.addClass("btn-default");
    editButton.find("i").addClass("fa-pencil"); editButton.find("i").removeClass("fa-save");
    var removeButton = $("a[data-id='removeAddress']");
    removeButton.attr('onclick',"removeAddress(" + addressID + ")");
    removeButton.find("i").addClass("fa-trash"); removeButton.find("i").removeClass("fa-times");
    $("[data-address='address']").removeClass("hidden");
}
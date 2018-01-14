
function checkPass() {
    //Store the password field objects into variables ...
    var curpass = document.getElementById('CurrentPassword');
    var pass1 = document.getElementById('NewPassword');
    var pass2 = document.getElementById('RepeatPassword');
    //Compare the values in the password field
    //and the confirmation field
    if (pass1.value === pass2.value && pass1.value !== "") {
        //The passwords match.
        //Set the color to the good color and inform
        //the user that they have entered the correct password
        document.getElementById('PwdCheck').classList.remove('fa-repeat');
        document.getElementById('PwdCheck').classList.remove('fa-times');
        document.getElementById('PwdCheck').classList.add('fa-check');
        if (curpass !== "") {
            document.getElementById("submitPwd").onclick = function () {
                $('#changePwdForm').submit();
            };
        }
    } else {
        //The passwords do not match.
        //Set the color to the bad color and
        //notify the user.
        document.getElementById('PwdCheck').classList.remove('fa-repeat');
        document.getElementById('PwdCheck').classList.remove('fa-check');
        document.getElementById('PwdCheck').classList.add('fa-times');
        document.getElementById("submitPwd").onclick = false;
    }
}

$(document).ready(function() {
    $("input[type='image']").click(function() {
        $("input[id='userPhoto']").click();
    });
    $("input[id='userPhoto']").change(function() {
        $('#userPhotoForm').submit();
    });
});
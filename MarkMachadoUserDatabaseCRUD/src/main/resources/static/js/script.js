function verify() {
    var password1 = document.forms['form']['password'].value;
    var password2 = document.forms['form']['verifyPassword'].value;

    // This first checks if the password is between 5 and 10 characters
    if (password1.length <= 5 || password1.length >= 10) {
        document.getElementById("error").innerHTML = "Password must be between 5 and 10 characters";
        return false;
    }

    // This then checks if there is a number in the password but has to have at least 2
    var numCount = (password1.match(/[0-9]/g) || []).length;
    if (numCount < 2) {
        document.getElementById("error").innerHTML = "Password must contain at least 2 numbers";
        return false;
    }

    // Check for at least one uppercase letter in the password
    if (!/[A-Z]/.test(password1)) {
        document.getElementById("error").innerHTML = "Password must contain at least one uppercase letter";
        return false;
    }

    // Check for at least one special character in the password but the special characters are limited to !$*#
    if (!/[!$*#]/.test(password1)) {
        document.getElementById("error").innerHTML = "Password must contain at least one special character";
        return false;
    }

    // Verify if passwords match
    if (password1 !== password2) {
        document.getElementById("error").innerHTML = "Passwords do not match";
        return false;
    }

    // All criteria met
    return true;
}
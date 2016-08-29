app = angular.module('app', []);

url = {
    login: context + '/login',
    profile: context + '/profile',
    settings: context + '/settings',
    error: context + '/error',
    logout: context + '/logout',
    accessDenied: context + '/403'
};

restUrl = {
    profile: context + '/api/profile',
    account: context + '/api/account'
};

configJson = {
    headers: {
        'Content-Type': 'application/json'
    }
};

message = {
    success: {
        success: 'Success!',
        logout: 'Logout successful!',
        accountCreated: 'Account created successfully!',
        accountDeleted: 'Account deleted successfully!'
    },
    error: {
        invalidCredentials: 'Invalid login or password',
        signup: 'Unable to sign up',
        fieldEmpty: 'One of the fields is empty',
        emailFormat: 'Unacceptable e-mail format',
        passwordMatch: 'Passwords should match',
        emailNotModified: 'e-mail is not modified',
        profileNotFound: 'Profile not found',
        accountNotFound: 'Account not found',
        internalError: 'Internal server error'
    }
};


app.controller('MainCtrl', function ($scope) {
});


app.service('ValidateService', function () {
    this.validateEmail = function (email) {
        var pattern = /^\w[\w\.]*\w\@[a-zA-Z0-9]+\.[a-zA-Z]{2,}$/;      // match: aa@aa.aa
        var regex = new RegExp(pattern);

        return regex.test(email);
    };

    this.validateConfirmPassword = function (newPassword, confirmPassword) {
        return (newPassword == confirmPassword);
    };
});

app.service('StatusService', function() {
    this.setStatus = function (statusElement, isSuccessful, message) {
        var elementClass = 'alert';
        elementClass += isSuccessful ? ' alert-success' : ' alert-danger';

        statusElement.style.visibility = 'visible';
        statusElement.innerHTML = message;
        statusElement.setAttribute('class', elementClass);
    };

    this.hideStatus = function(statusElement) {
        statusElement.style.visibility = 'hidden';
    };
});

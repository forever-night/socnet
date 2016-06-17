app = angular.module('app', []);

url = {
    signIn: '/socnet/sign_in/',
    profile: '/socnet/profile/'
};

restUrl = {
    profile: '/socnet/api/profile/',
    account: '/socnet/api/account/'
};

configJson = {
    headers: {
        'Content-Type': 'application/json'
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

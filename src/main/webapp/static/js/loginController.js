app.controller('LoginCtrl', function($scope, StatusService) {
    var status = document.getElementById('status');


    $scope.logoutSuccessful = function() {
        StatusService.setStatus(status, true, message.success.logout);
    };

    $scope.signupSuccessful = function() {
        StatusService.setStatus(status, true, message.success.accountCreated);
    };

    $scope.deleteSuccessful = function() {
        StatusService.setStatus(status, true, message.success.accountDeleted);
    };

    $scope.loginError = function() {
        StatusService.setStatus(status, false, message.error.invalidCredentials);
    };

    $scope.hideStatus = function() {
        status.style.visibility = 'hidden';
    };
});
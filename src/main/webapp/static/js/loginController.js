app.controller('LoginCtrl', function($scope, StatusService) {
    var status = document.getElementById('status');


    $scope.logoutSuccessful = function() {
        StatusService.setStatus(status, true, "Logout successful!");
    };

    $scope.signupSuccessful = function() {
        StatusService.setStatus(status, true, "Account created successfully!");
    };

    $scope.loginError = function() {
        StatusService.setStatus(status, false, "Invalid login or password.");
    };

    $scope.hideStatus = function() {
        status.style.visibility = "hidden";
    };
});
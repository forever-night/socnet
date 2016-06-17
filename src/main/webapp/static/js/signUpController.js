app.controller('SignUpCtrl', function ($scope, $http, $window, ValidateService, StatusService) {
    $scope.account = new Account();
    $scope.confirmPassword = '';

    var status = document.getElementById('status');


    $scope.postAccount = function(data) {
        var config = {
            headers: {'Content-Type': 'application/json'}
        };

        var request = $http.post(restUrl.account, data, config).then(
            function success(response) {
                $window.location.href = response.headers('Location');
            },
            function error(response) {
                StatusService.setStatus(status, false, 'Unable to sign up');
            }
        );
    };

    $scope.signUp = function () {
        StatusService.hideStatus(status);


        if ($scope.account.login == '' || $scope.account.email == '' || $scope.account.password == '') {
            StatusService.setStatus(status, false, 'One of the fields is empty');
            return;
        }


        var validateEmail = ValidateService.validateEmail($scope.account.email);

        if (!validateEmail) {
            StatusService.setStatus(status, false, 'Unacceptable e-mail format');
            return;
        }

        // TODO check if login is taken
        // TODO check if email is taken


        var validatePassword = ValidateService.validateConfirmPassword($scope.account.password, $scope.confirmPassword);

        if (validatePassword) {
            $scope.postAccount($scope.account);
        } else {
            StatusService.setStatus(status, false, 'Passwords should match');
            return false;
        }
    };
});

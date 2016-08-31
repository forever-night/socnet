app.controller('SignupCtrl', function ($scope, $http, $window, ValidateService, StatusService) {
    $scope.account = new Account();
    $scope.confirmPassword = '';

    var status = document.getElementById('status');
    var csrfToken = document.getElementsByName('_csrf')[0].content;


    $scope.postAccount = function(data) {
        var config = {
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN' : csrfToken
            }
        };

        return $http.post(restUrl.account, data, config).then(
            function success() {
                $window.location.href = url.login + "?signup";
            },
            function error(response) {
                switch (response.status) {
                    case 400:
                        StatusService.setStatus(status, false, message.error.fieldEmpty);
                        break;
                    case 422:
                        StatusService.setStatus(status, false, message.error.loginTaken);
                        break;
                    default:
                        StatusService.setStatus(status, false, message.error.signup);
                }
            }
        );
    };

    $scope.signUp = function () {
        StatusService.hideStatus(status);


        if ($scope.account.login == '' || $scope.account.email == '' || $scope.account.password == '') {
            StatusService.setStatus(status, false, message.error.fieldEmpty);
            return;
        }


        var validateEmail = ValidateService.validateEmail($scope.account.email);

        if (!validateEmail) {
            StatusService.setStatus(status, false, message.error.emailFormat);
            return;
        }


        var validatePassword = ValidateService.validateConfirmPassword($scope.account.password, $scope.confirmPassword);

        if (validatePassword) {
            return $scope.postAccount($scope.account);
        } else {
            StatusService.setStatus(status, false, message.error.passwordMatch);
            return false;
        }
    };
});
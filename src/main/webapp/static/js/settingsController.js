app.controller('SettingsCtrl', function ($scope, $http, $window, ValidateService, StatusService) {
    $scope.profileSelected = true;
    $scope.accountSelected = false;
    $scope.profile = null;
    $scope.account = null;
    $scope.newAccount = null;

    var persistentProfile = new Profile();

    var statusElement = {
        profile : document.getElementById('statusProfile'),
        email : document.getElementById('statusEmail'),
        password : document.getElementById('statusPassword'),
        delete : document.getElementById('statusDelete')
    };

    var tab = {
        profile : document.getElementById('tabProfile'),
        account : document.getElementById('tabAccount')
    };


    var emailUpdateStatus, passwordUpdateStatus, deleteStatus;

    var csrfToken = document.getElementsByName('_csrf')[0].content;


    statusElement.profile.style.visibility = 'hidden';
    statusElement.email.style.visibility = 'hidden';
    statusElement.password.style.visibility = 'hidden';
    statusElement.delete.style.visibility = 'hidden';


    $scope.getProfile = function () {
        return $http.get(restUrl.profile + "/" + login).then(
            function (response) {
                if (response.status == 204)
                    $window.location.href = url.error + '?errorMessage=' + message.error.profileNotFound;
                else {
                    var data = response.data;

                    if (data != null)
                        $scope.profile = new Profile(
                            data.name,
                            data.dateOfBirth,
                            data.phone,
                            data.country,
                            data.city,
                            data.info
                        );
                }
            },
            function (response) {
                $window.location.href = url.error + '?errorMessage=Error ' + response.status;
            }
        );
    };

    $scope.putProfile = function (data) {
        var config = {
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN' : csrfToken
            }
        };

        return $http.put(restUrl.profile + "/" + $scope.account.login, data, config);
    };

    $scope.getAccount = function () {
        return $http.get(restUrl.account + "/" + login).then(
            function (response) {
                if (response.status == 204)
                    $window.location.href = url.error + '?errorMessage=' + message.error.accountNotFound;
                else {
                    var data = response.data;

                    if (data != null)
                        $scope.account = new Account(
                            data.login,
                            data.email);

                    $scope.newAccount = copy($scope.account);
                }
            },
            function error(response) {
                $window.location.href = url.error + '?errorMessage=Error ' + response.status;
            }
        );
    };

    $scope.putAccountEmail = function (data) {
        var config = {
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN' : csrfToken
            }
        };


        return $http.put(restUrl.account + "/" + login + '/email', data, config).then(
            function success(response) {
                if (response.status == 200)
                    emailUpdateStatus = 1;
            },
            function error(response) {
                if (response.status != 403)
                    emailUpdateStatus = -1;
            }
        );
    };

    $scope.putAccountPassword = function (data) {
        var config = {
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN' : csrfToken
            }
        };

        return $http.put(restUrl.account + "/" + login + '/password', data, config).then(
            function success(response) {
                if (response.status == 200)
                    passwordUpdateStatus = 1;
            },
            function error(response) {
                if (response.status != 403)
                    passwordUpdateStatus = -1;
            }
        );
    };

    $scope.removeAccount = function () {
        var config = {
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN' : csrfToken
            }
        };

        return $http.delete(restUrl.account + "/" + $scope.account.login, config).then(
            function success(response) {
                if (response.status == 200)
                    deleteStatus = 1;
            },
            function error(response) {
                if (response.status != 403)
                    deleteStatus = -1;
            }
        );
    };

    $scope.setProfileSelected = function (value) {
        $scope.profileSelected = value;
        $scope.accountSelected = !value;

        tab.profile.setAttribute('class', 'active');
        tab.account.removeAttribute('class');

        statusElement.profile.style.visibility = 'hidden';
        statusElement.email.visibility = 'hidden';
        statusElement.password.style.visibility = 'hidden';
        statusElement.delete.style.visibility = 'hidden';


        $scope.getProfile().then(
            function () {
                persistentProfile = copyTo($scope.profile, persistentProfile);

                statusElement.profile.style.visibility = 'hidden';
            }
        );
    };

    $scope.setAccountSelected = function (value) {
        $scope.accountSelected = value;
        $scope.profileSelected = !value;

        tab.account.setAttribute('class', 'active');
        tab.profile.removeAttribute('class');

        statusElement.profile.style.visibility = 'hidden';
        statusElement.email.style.visibility = 'hidden';
        statusElement.password.style.visibility = 'hidden';
        statusElement.delete.style.visibility = 'hidden';


        $scope.getAccount();
    };

    $scope.resetForm = function () {
        $scope.profile = copyTo(persistentProfile, $scope.profile);

        statusElement.profile.style.visibility = 'hidden';
    };

    $scope.saveForm = function () {
        if ($scope.profile == null)
            return;

        $scope.putProfile($scope.profile).then(
            function () {
                StatusService.setStatus(statusElement.profile, true, message.success.success);
                persistentProfile = copyTo($scope.profile, persistentProfile);
            },
            function () {
                StatusService.setStatus(statusElement.profile, false, message.error.internalError);
            }
        );
    };

    $scope.changeEmail = function () {
        statusElement.password.style.visibility = 'hidden';
        statusElement.delete.style.visibility = 'hidden';


        if ($scope.account == null)
            return;

        if ($scope.newAccount.email == null) {
            StatusService.setStatus(statusElement.email, false, message.error.fieldEmpty);
            return;
        }


        var validated = ValidateService.validateEmail($scope.newAccount.email);

        if (!validated) {
            StatusService.setStatus(statusElement.email, false, message.error.emailFormat);
            return;
        }


        if ($scope.newAccount.email == $scope.account.email) {
            StatusService.setStatus(statusElement.email, true, message.error.emailNotModified);
            return;
        }

        $scope.putAccountEmail($scope.newAccount).then(
            function () {
                if (emailUpdateStatus != null && emailUpdateStatus > 0) {
                    StatusService.setStatus(statusElement.email, true, message.success.success);
                    $scope.account.email = $scope.newAccount.email;
                }

                emailUpdateStatus = null;
            },
            function () {
                StatusService.setStatus(statusElement.email, false, message.error.internalError);

                emailUpdateStatus = null;
            });
    };

    $scope.changePassword = function () {
        statusElement.email.style.visibility = 'hidden';
        statusElement.delete.style.visibility = 'hidden';


        var oldPassword = document.getElementById('inputOldPassword').value;
        var newPassword = document.getElementById('inputNewPassword').value;
        var confirmPassword = document.getElementById('inputConfirmPassword').value;

        var validated = $scope.validatePassword(oldPassword, newPassword, confirmPassword);

        if (!validated)
            return;


        $scope.account.password = oldPassword;

        var newAccount = copy($scope.account);
        newAccount.password = newPassword;


        $scope.putAccountPassword(newAccount).then(
            function () {
                if (passwordUpdateStatus != null && passwordUpdateStatus > 0)
                    StatusService.setStatus(statusElement.password, true, message.success.success);

                passwordUpdateStatus = null;

            //    TODO clear form
            },
            function() {
                StatusService.setStatus(statusElement.password, false, message.error.internalError);
                passwordUpdateStatus = null;
            });
    };

    $scope.deleteAccount = function () {
        StatusService.hideStatus(statusElement.email);
        StatusService.hideStatus(statusElement.password);


        var confirm = window.confirm("Delete account?");

        if (confirm) {
            $scope.removeAccount().then(
                function() {
                    var config = {
                        headers: {
                            'Content-Type': 'application/json',
                            'X-CSRF-TOKEN' : csrfToken
                        }
                    };

                    deleteStatus = null;
                    $http.post(url.logout, "", config).then(
                        function() {
                            $window.location.href = url.login + "?delete";
                        });
                },
                function() {
                    StatusService.setStatus(statusElement.delete, false, message.error.accountNotFound);
                    deleteStatus = null;
                });
        }
    };

    $scope.validatePassword = function (oldPassword, newPassword, confirmPassword) {
        if (oldPassword == '' || newPassword == '' || confirmPassword == '') {
            StatusService.setStatus(statusElement.password, false, message.error.fieldEmpty);
            return false;
        }

        var validateConfirmPassword = ValidateService.validateConfirmPassword(newPassword, confirmPassword);

        if (validateConfirmPassword)
            return true;
        else {
            StatusService.setStatus(statusElement.password, false, message.error.passwordMatch);
            return false;
        }
    };


    $scope.setProfileSelected(true);
    $scope.getAccount();
});
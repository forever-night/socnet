app.controller('SettingsCtrl', function ($scope, $http, $window, ProfileService, ValidateService, StatusService, AccountService) {
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

    var csrfToken = document.getElementsByName('_csrf')[0].content;


    statusElement.profile.style.visibility = 'hidden';
    statusElement.email.style.visibility = 'hidden';
    statusElement.password.style.visibility = 'hidden';
    statusElement.delete.style.visibility = 'hidden';


    $scope.getProfile = function () {
        return ProfileService.get(login).then(
            function success(response) {
                if (response == null)
                    $window.location.href = url.errorWithMessage + message.error.profileNotFound;
                else
                    $scope.profile = response;
            },
            function error(response) {
                $window.location.href = url.errorWithMessage + 'Error ' + response.status;
            }
        );
    };

    $scope.getAccount = function () {
        return AccountService.get(login).then(
            function success(response) {
                if (response == null)
                    $window.location.href = url.errorWithMessage + message.error.accountNotFound;
                else {
                    $scope.account = response;
                    $scope.newAccount = copy($scope.account);
                }
            },
            function error(response) {
                $window.location.href = url.errorWithMessage + 'Error ' + response.status;
            }
        );
    };

    $scope.setProfileSelected = function (value) {
        $scope.profileSelected = value;
        $scope.accountSelected = !value;

        if (value) {
            tab.profile.setAttribute('class', 'active');
            tab.account.removeAttribute('class');
        }

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

        if (value) {
            tab.account.setAttribute('class', 'active');
            tab.profile.removeAttribute('class');
        }

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

        ProfileService.put($scope.account.login, $scope.profile, csrfToken).then(
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

        AccountService.putEmail(login, $scope.newAccount, csrfToken).then(
            function success() {
                StatusService.setStatus(statusElement.email, true, message.success.success);
                $scope.account.email = $scope.newAccount.email;
            },
            function error(response) {
                StatusService.setStatus(statusElement.email, false,
                    message.error.internalError + ': ' + response.status);
            }
        );
    };

    $scope.changePassword = function () {
        statusElement.email.style.visibility = 'hidden';
        statusElement.delete.style.visibility = 'hidden';


        var oldPassword = document.getElementById('inputOldPassword');
        var newPassword = document.getElementById('inputNewPassword');
        var confirmPassword = document.getElementById('inputConfirmPassword');

        var validated = $scope.validatePassword(oldPassword.value, newPassword.value, confirmPassword.value);

        if (!validated)
            return;


        $scope.account.password = oldPassword.value;

        var newAccount = copy($scope.account);
        newAccount.password = newPassword.value;
        newAccount.oldPassword = oldPassword.value;


        AccountService.putPassword(login, newAccount, csrfToken).then(
            function success() {
                StatusService.setStatus(statusElement.password, true, message.success.success);

                oldPassword.value = '';
                newPassword.value = '';
                confirmPassword.value = '';
            },
            function error(response) {
                if (response.status == 401)
                    StatusService.setStatus(statusElement.password, false, message.error.wrongPassword);
                else
                    StatusService.setStatus(statusElement.password, false,
                        message.error.internalError + ': ' + response.status);
            }
        );
    };

    $scope.deleteAccount = function () {
        StatusService.hideStatus(statusElement.email);
        StatusService.hideStatus(statusElement.password);


        var confirm = window.confirm('Delete account?');

        if (confirm) {
            AccountService.remove(login, csrfToken).then(
                function success(response) {
                    if (response.status == 204)
                        StatusService.setStatus(statusElement.delete, false, message.error.accountNotFound);
                    else {
                        var config = {
                            headers: {
                                'Content-Type': 'application/json',
                                'X-CSRF-TOKEN': csrfToken
                            }
                        };

                        // TODO replace with service
                        $http.post(url.logout, '', config).then(
                            function () {
                                $window.location.href = url.login + '?delete';
                            });
                    }
                },
                function error(response) {
                    StatusService.setStatus(statusElement.delete, false,
                        message.error.internalError + ': ' + response.status);
                }
            );
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
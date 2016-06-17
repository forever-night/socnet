app.controller('SettingsCtrl', function ($scope, $http, $window, ValidateService, StatusService) {
    $scope.profileSelected = true;
    $scope.accountSelected = false;
    $scope.profile = null;
    $scope.account = null;
    $scope.newAccount = null;

    var persistentProfile = new Profile();

    var statusProfile = document.getElementById('statusProfile');
    var statusEmail = document.getElementById('statusEmail');
    var statusPassword = document.getElementById('statusPassword');
    var tabProfile = document.getElementById('tabProfile');
    var tabAccount = document.getElementById('tabAccount');

    var updatedAccountId, updatePasswordStatus;


    statusProfile.style.visibility = 'hidden';
    statusEmail.style.visibility = 'hidden';
    statusPassword.style.visibility = 'hidden';


    $scope.getProfile = function () {
        return $http.get(restUrl.profile + profileId).then(
            function (response) {
                var data = response.data;

                if (data != null)
                    $scope.profile = new Profile(
                        data.id,
                        data.name,
                        data.dateOfBirth,
                        data.phone,
                        data.country,
                        data.currentCity,
                        data.info
                    );
            }
        );
    };

    $scope.putProfile = function (data) {
        return $http.put(restUrl.profile + $scope.profile.id, data, configJson);
    };

    $scope.getAccount = function () {
        return $http.get(restUrl.account + profileId).then(
            function (response) {
                var data = response.data;

                if (data != null)
                    $scope.account = new Account(
                        data.id,
                        data.email);

                $scope.newAccount = copy($scope.account);
            }
        );
    };

    $scope.putAccountEmail = function (data) {
        return $http.put(restUrl.account + profileId + '/email', data, configJson).then(
            function (response) {
                if (response.data != null)
                    updatedAccountId = response.data;
            }
        );
    };

    $scope.putAccountPassword = function (data) {
        return $http.put(restUrl.account + profileId + '/password', data, configJson).then(
            function (response) {
                if (response.data != null)
                    updatePasswordStatus = response.data;
            }
        );
    };

    $scope.removeAccount = function () {
        return $http.delete(restUrl.account + profileId);
    };

    $scope.setProfileSelected = function (value) {
        $scope.profileSelected = value;
        $scope.accountSelected = !value;

        tabProfile.setAttribute('class', 'active');
        tabAccount.removeAttribute('class');

        statusProfile.style.visibility = 'hidden';
        statusEmail.style.visibility = 'hidden';
        statusPassword.style.visibility = 'hidden';


        $scope.getProfile().then(
            function () {
                persistentProfile = copyTo($scope.profile, persistentProfile);

                statusProfile.style.visibility = 'hidden';
            }
        );
    };

    $scope.setAccountSelected = function (value) {
        $scope.accountSelected = value;
        $scope.profileSelected = !value;

        tabAccount.setAttribute('class', 'active');
        tabProfile.removeAttribute('class');

        statusProfile.style.visibility = 'hidden';
        statusEmail.style.visibility = 'hidden';
        statusPassword.style.visibility = 'hidden';


        $scope.getAccount();
    };

    $scope.resetForm = function () {
        $scope.profile = copyTo(persistentProfile, $scope.profile);

        statusProfile.style.visibility = 'hidden';
    };

    $scope.saveForm = function () {
        if ($scope.profile == null)
            return;

        $scope.putProfile($scope.profile).then(
            function () {
                StatusService.setStatus(statusProfile, true, 'Success!');
                persistentProfile = copyTo($scope.profile, persistentProfile);
            },
            function () {
                StatusService.setStatus(statusProfile, false, 'error in updating profile');
            }
        );
    };

    $scope.changeEmail = function () {
        statusPassword.style.visibility = 'hidden';


        if ($scope.account == null)
            return;

        if ($scope.newAccount.email == null) {
            StatusService.setStatus(statusEmail, false, 'field is empty');
            return;
        }


        var validated = ValidateService.validateEmail($scope.newAccount.email);

        if (!validated) {
            StatusService.setStatus(statusEmail, false, 'unacceptable e-mail format');
            return;
        }

        // TODO check if email is taken


        $scope.putAccountEmail($scope.newAccount).then(
            function () {
                if (updatedAccountId != null && updatedAccountId > 0) {
                    StatusService.setStatus(statusEmail, true, 'Success!');
                    $scope.account.email = $scope.newAccount.email;
                } else
                    StatusService.setStatus(statusEmail, false, 'error in updating e-mail');

                updatedAccountId = null;
            });
    };

    $scope.changePassword = function () {
        statusEmail.style.visibility = 'hidden';


        var oldPassword = document.getElementById('inputOldPassword').value;
        var newPassword = document.getElementById('inputNewPassword').value;
        var confirmPassword = document.getElementById('inputConfirmPassword').value;

        var validated = $scope.validatePassword(oldPassword, newPassword, confirmPassword);

        if (!validated)
            return;


        $scope.account.password = oldPassword;

        var passwordChangeDto = {
            account: $scope.account,
            newPassword: newPassword
        };


        $scope.putAccountPassword(passwordChangeDto).then(
            function () {
                switch (updatePasswordStatus) {
                    case 0:
                        StatusService.setStatus(statusPassword, true, 'Success!');
                        document.getElementById('inputOldPassword').value = '';
                        document.getElementById('inputNewPassword').value = '';
                        document.getElementById('inputConfirmPassword').value = '';
                        $scope.account.password = '';
                        break;
                    case 1:
                        StatusService.setStatus(statusPassword, false, 'wrong password');
                        break;
                    case 2:
                        StatusService.setStatus(statusPassword, false, "can't authenticate");
                        break;
                    default:
                        StatusService.setStatus(statusPassword, false, 'unexpected error');
                        break;
                }

                updatePasswordStatus = null;
            });
    };

    $scope.deleteAccount = function () {
        StatusService.hideStatus(statusEmail);
        StatusService.hideStatus(statusPassword);
        // statusEmail.style.visibility = 'hidden';
        // statusPassword.style.visibility = 'hidden';


        var confirm = window.confirm("Delete account?");

        if (confirm) {
            $scope.removeAccount().then(
                function () {
                    $window.location.href = url.signIn;
                }
            );
        }
    };

    $scope.validatePassword = function (oldPassword, newPassword, confirmPassword) {
        if (oldPassword == '' || newPassword == '' || confirmPassword == '') {
            StatusService.setStatus(statusPassword, false, 'one of the fields is empty');
            return false;
        }

        var validateConfirmPassword = ValidateService.validateConfirmPassword(newPassword, confirmPassword);

        if (validateConfirmPassword)
            return true;
        else {
            StatusService.setStatus(statusPassword, false, 'new password and confirm password should match');
            return false;
        }
    };

    // $scope.setStatus = function (statusElement, isSuccessful, message) {
    //     var elementClass = 'alert';
    //     elementClass += isSuccessful ? ' alert-success' : ' alert-danger';
    //
    //     statusElement.style.visibility = 'visible';
    //     statusElement.innerHTML = message;
    //     statusElement.setAttribute('class', elementClass);
    // };


    $scope.setProfileSelected(true);
});
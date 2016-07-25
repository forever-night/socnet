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
            },
            function () {
                $window.location.href = url.error;
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

        return $http.put(restUrl.profile + "/" + $scope.profile.id, data, config);
    };

    $scope.getAccount = function () {
        return $http.get(restUrl.account + "/" + login).then(
            function (response) {
                var data = response.data;

                if (data != null)
                    $scope.account = new Account(
                        data.id,
                        data.login,
                        data.email);

                $scope.newAccount = copy($scope.account);
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
                if (response.status == 403)
                    $window.location.href = url.accessDenied;
                else
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
                if (response.status == 403)
                    $window.location.href = url.accessDenied;
                else
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

        $http.delete(restUrl.account + "/" + $scope.account.id + "?login=" + login, config).then(
            function success(response) {
                if (response.status == 200)
                    deleteStatus = 1;
            },
            function error(response) {
                if (response.status == 403)
                    $window.location.href = url.accessDenied;
                else
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
                StatusService.setStatus(statusElement.profile, true, 'Success!');
                persistentProfile = copyTo($scope.profile, persistentProfile);
            },
            function () {
                StatusService.setStatus(statusElement.profile, false, 'Error! Try again later');
            }
        );
    };

    $scope.changeEmail = function () {
        statusElement.password.style.visibility = 'hidden';
        statusElement.delete.style.visibility = 'hidden';


        if ($scope.account == null)
            return;

        if ($scope.newAccount.email == null) {
            StatusService.setStatus(statusElement.email, false, 'Field is empty');
            return;
        }


        var validated = ValidateService.validateEmail($scope.newAccount.email);

        if (!validated) {
            StatusService.setStatus(statusElement.email, false, 'Unacceptable e-mail format');
            return;
        }


        if ($scope.newAccount.email == $scope.account.email) {
            StatusService.setStatus(statusElement.email, true, 'E-mail is the same.');
            return;
        }


        $scope.putAccountEmail($scope.newAccount).then(
            function () {
                if (emailUpdateStatus != null && emailUpdateStatus > 0) {
                    StatusService.setStatus(statusElement.email, true, 'Success!');
                    $scope.account.email = $scope.newAccount.email;
                }

                emailUpdateStatus = null;
            },
            function () {
                StatusService.setStatus(statusElement.email, false, 'Error in updating e-mail');

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
                    StatusService.setStatus(statusElement.password, true, 'Success!');

                passwordUpdateStatus = null;
            },
            function() {
                StatusService.setStatus(statusElement.password, false, 'Error in updating password!');
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
                    if (deleteStatus != null && deleteStatus > 0)
                        $http.post(url.logout, "", config);

                    deleteStatus = null;
                },
                function() {
                    StatusService.setStatus(statusElement.delete, false, 'Account not found!');
                    deleteStatus = null;
                });
        }
    };

    $scope.validatePassword = function (oldPassword, newPassword, confirmPassword) {
        if (oldPassword == '' || newPassword == '' || confirmPassword == '') {
            StatusService.setStatus(statusElement.password, false, 'One of the fields is empty');
            return false;
        }

        var validateConfirmPassword = ValidateService.validateConfirmPassword(newPassword, confirmPassword);

        if (validateConfirmPassword)
            return true;
        else {
            StatusService.setStatus(statusElement.password, false, 'Passwords should match');
            return false;
        }
    };


    $scope.setProfileSelected(true);
});
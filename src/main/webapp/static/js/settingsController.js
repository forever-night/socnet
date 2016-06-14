app.controller('SettingsCtrl', function ($scope, $http) {
    $scope.profileSelected = true;
    $scope.accountSelected = false;
    $scope.profile = null;
    $scope.account = null;

    var persistentProfile = new Profile();
    var status = document.getElementById('status');

    status.style.visibility = 'hidden';


    $scope.getProfile = function () {
        var request = $http.get(restUrl.profile + profileId).then(
            function (response) {
                var data = response.data;
                console.log(data);

                if (data != null || data !== undefined)
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
            function (response) {
                console.log('error ' + response.status);
            }
        );

        return request;
    };

    $scope.putProfile = function (data) {
        var config = {
            headers: {
                'Content-Type': 'application/json'
            }
        };

        console.log(data);

        var request = $http.put(restUrl.profile + $scope.profile.id, data, config).then(
            function (response) {
                console.log('put ' + response.data);
            }, function (response) {
                console.log('error ' + response.status);
            }
        );

        return request;
    };

    $scope.setProfileSelected = function (value) {
        $scope.profileSelected = value;
        $scope.accountSelected = !value;

        $scope.getProfile().then(
            function () {
                persistentProfile = copyTo($scope.profile, persistentProfile);

                status.style.visibility = 'hidden';
            },
            function () {
                console.log("error in setProfileSelected");
            }
        );
    };

    $scope.resetForm = function () {
        $scope.profile = copyTo(persistentProfile, $scope.profile);

        status.style.visibility = 'hidden';
    };

    $scope.saveForm = function () {
        if ($scope.profile == null)
            console.log("can't save, profile = null");

        $scope.putProfile($scope.profile).then(
            function () {
                persistentProfile = copyTo($scope.profile, persistentProfile);

                status.style.visibility = 'visible';
                status.setAttribute("class", "bg-success");
                status.innerHTML = "success";

                console.log('form saved');
            },
            function () {
                status.style.visibility = 'visible';
                status.setAttribute("class", "bg-danger");
                status.innerHTML = "error";

                console.log("error in saveForm");
            }
        );
    };


    $scope.setProfileSelected(true);
});

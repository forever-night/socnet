app.controller('ProfileCtrl', function($scope, $http, $window) {
    $scope.profile = null;
    $scope.isOwner = false;
    

    $scope.getProfile = function() {
        return $http.get(restUrl.profile + "/" + login).then(
            function success(response) {
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
            function error(response) {
                $window.location.href = url.error + "?errorMessage=Error " + response.status;
            }
        );
    };


    $scope.getProfile();
});
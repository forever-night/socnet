app.controller('ProfileCtrl', function($scope, $http, $window) {
    $scope.profile = null;
    $scope.isOwner = false;
    

    $scope.getProfile = function() {
        var request = $http.get(restUrl.profile + "/" + login).then(
            function success(response) {
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
                else
                    $window.location.href = url.error + "?errorMessage=User not found";
            },
            function error(response) {
                $window.location.href = url.error + "?errorMessage=Internal Server Error";
            }
        );
        
        return request;
    };


    $scope.getProfile();
});

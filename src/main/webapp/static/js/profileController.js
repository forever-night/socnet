app.controller('ProfileCtrl', function($scope, $http) {
    $scope.profile = null;
    $scope.isOwner = false;
    

    $scope.getProfile = function() {
        var request = $http.get(restUrl.profile + profileId).then(
            function(response) {
                var data = response.data;

                if (data != null) {
                    $scope.profile = new Profile(
                        data.id,
                        data.name,
                        data.dateOfBirth,
                        data.phone,
                        data.country,
                        data.currentCity,
                        data.info
                    );
                    
                    profileId = $scope.profile.id;
                }
            }
        );
        
        return request;
    };


    $scope.getProfile();
});

app.controller('ProfileCtrl', function($scope, $http) {
    var profileUrl = '/socnet/api/profile/';
    
    $scope.profile = null;
    $scope.isEditing = false;
    $scope.isOwner = false;

    
    $scope.setEditing = function(value) {
        $scope.isEditing = value;
    };

    $scope.getProfile = function() {
        var request = $http.get(profileUrl + profileId).then(
            function(response) {
                var data = response.data;

                if (data != null || data !== undefined)
                    $scope.profile = new Profile(
                        data.id,
                        data.name,
                        data.dateOfBirth,
                        data.phone,
                        data.country,
                        data.city,
                        data.info
                    );
            },
            function(response) {
                console.log('error ' + response.status);
            }
        );
    };

    $scope.putProfile = function(data) {
        var request = $http.put(profileUrl + profile.id).then(
            function(response) {
            //    TODO update profile (rest put), display changes in view
                
                $scope.setEditing(false);
            }, function(response) {
                console.log('error ' + response.status);
            }
        );
    };


    $scope.getProfile();
});

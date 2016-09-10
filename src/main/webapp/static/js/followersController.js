app.controller('FollowersCtrl', function($scope, $window, ProfileService, StatusService) {
    $scope.followers = [];
    $scope.currentLogin = currentLogin;
    $scope.profileLogin = $scope.currentLogin;

    var followers = [];
    var status = document.getElementById('status');


    $scope.getFollowers = function(login) {
        ProfileService.getFollowers(login).then(
            function success(response) {
                console.log(response);

                if (response.length == 0)
                    StatusService.setStatus(status, true, login + ' has no followers.');
                else {
                    followers = response;

                    // TODO make multiple pages for big results
                    followers.forEach(function(item, i, followers) {
                        if (i >= 10)
                            return;

                        $scope.followers.push(item);
                    });
                }
            },
            function error(response) {
                console.log(response);

                var errorMessage = response.status == 400 ? message.error.queryEmpty : 'Error ' + response.status;
                StatusService.setStatus(status, false, errorMessage);
            }
        );
    };


    if (typeof ownerLogin !== 'undefined' && ownerLogin != null) {
        $scope.profileLogin = ownerLogin;
    }

    console.log('current login ' + $scope.currentLogin);
    console.log('followers owner ' + $scope.profileLogin);

    $scope.getFollowers($scope.profileLogin);
});

app.controller('FollowersCtrl', function($scope, $window, ProfileService, StatusService) {
    $scope.followersSelected = true;
    $scope.followingSelected = false;
    $scope.followers = [];
    $scope.following = [];
    $scope.currentLogin = currentLogin;
    $scope.profileLogin = $scope.currentLogin;

    var statusElement = document.getElementById('status');

    var followers = [],
        following = [];

    var tab = {
        followers : document.getElementById('tabFollowers'),
        following : document.getElementById('tabFollowing')
    };


    $scope.getFollowers = function(login) {
        ProfileService.getFollowers(login).then(
            function success(response) {
                if (response.length == 0)
                    StatusService.setStatus(statusElement, true, login + ' has no followers.');
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
                var errorMessage = response.status == 400 ? message.error.queryEmpty : 'Error ' + response.status;
                StatusService.setStatus(statusElement, false, errorMessage);
            }
        );
    };

    $scope.getFollowing = function(login) {
        ProfileService.getFollowing(login).then(
            function success(response) {
                if (response.length == 0)
                    StatusService.setStatus(statusElement, true, login + ' follows no one.');
                else {
                    following = response;

                    // TODO make multiple pages for big results
                    following.forEach(function(item, i, following) {
                        if (i >= 10)
                            return;

                        $scope.following.push(item);
                    });
                }
            },
            function error(response) {
                var errorMessage = response.status == 400 ? message.error.queryEmpty : 'Error ' + response.status;
                StatusService.setStatus(statusElement, false, errorMessage);
            }
        );
    };

    $scope.setFollowersSelected = function (value){
        $scope.followersSelected = value;
        $scope.followingSelected = !value;

        if (value) {
            tab.followers.setAttribute('class', 'active');
            tab.following.removeAttribute('class');

            statusElement.innerHTML = '';
            StatusService.hideStatus(statusElement);

            $scope.following = [];
            $scope.getFollowers($scope.profileLogin);
        }
    };

    $scope.setFollowingSelected = function(value) {
        $scope.followingSelected = value;
        $scope.followersSelected = !value;

        if (value) {
            tab.following.setAttribute('class', 'active');
            tab.followers.removeAttribute('class');

            statusElement.innerHTML = '';
            StatusService.hideStatus(statusElement);

            $scope.followers = [];
            $scope.getFollowing($scope.profileLogin);
        }
    };


    if (typeof ownerLogin !== 'undefined' && ownerLogin != null) {
        $scope.profileLogin = ownerLogin;
    }

    $scope.setFollowersSelected(true);
});

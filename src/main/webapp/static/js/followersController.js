app.controller('FollowersCtrl', function($scope, $window, ProfileService, StatusService, PageService) {
    $scope.followersSelected = true;
    $scope.followingSelected = false;
    $scope.followers = [];
    $scope.following = [];
    $scope.currentLogin = currentLogin;
    $scope.profileLogin = $scope.currentLogin;
    $scope.currentPage = 0;
    $scope.pageCount = 0;

    var statusElement = document.getElementById('status');
    var csrfToken = document.getElementsByName('_csrf')[0].content;

    var followers = [],
        following = [];

    var tab = {
        followers : document.getElementById('tabFollowers'),
        following : document.getElementById('tabFollowing')
    };

    var resultsPerPage = 20;


    $scope.getFollowers = function (login) {
        ProfileService.getFollowers(login).then(
            function success(response) {
                if (response.length == 0)
                    StatusService.setStatus(statusElement, true, login + ' has no followers.');
                else {
                    followers = response;
                    $scope.pageCount = Math.ceil(followers.length / resultsPerPage);

                    // populates the first page with results
                    followers.forEach(function(item, i, followers) {
                        if (i >= resultsPerPage)
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

    $scope.getFollowing = function (login) {
        ProfileService.getFollowing(login).then(
            function success(response) {
                if (response.length == 0)
                    StatusService.setStatus(statusElement, true, login + ' follows no one.');
                else {
                    following = response;
                    $scope.pageCount = Math.ceil(following.length / resultsPerPage);

                    // populates the first page with results
                    following.forEach(function(item, i, following) {
                        if (i >= resultsPerPage)
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

    $scope.follow = function (profileToFollow) {
        ProfileService.follow(profileToFollow.login, true, csrfToken).then(
            function success(response) {
                if (response == 200) {
                    if ($scope.followers != null && $scope.followers.length > 0)
                        setIsFollowing($scope.followers, profileToFollow, true);

                    if ($scope.following != null)
                    setIsFollowing($scope.following, profileToFollow, true);
                }
            }
        );
    };

    $scope.unfollow = function (profileToUnfollow) {
        ProfileService.follow(profileToUnfollow.login, false, csrfToken).then(
            function success(response) {
                if (response == 200) {
                    if ($scope.followers != null && $scope.followers.length > 0)
                        setIsFollowing($scope.followers, profileToUnfollow, false);

                    if ($scope.following != null)
                        setIsFollowing($scope.following, profileToUnfollow, false);
                }
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

    $scope.setFollowingSelected = function (value) {
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

    $scope.prevPage = function () {
        if ($scope.followersSelected)
            $scope.followers = PageService.prevPage($scope.currentPage, resultsPerPage, followers);
        else if ($scope.followingSelected)
            $scope.following = PageService.prevPage($scope.currentPage, resultsPerPage, following);

        if ($scope.currentPage > 0)
            $scope.currentPage--;
    };

    $scope.nextPage = function () {
        if ($scope.followersSelected)
            $scope.followers = PageService.nextPage($scope.currentPage, resultsPerPage, followers);
        else if ($scope.followingSelected)
            $scope.following = PageService.nextPage($scope.currentPage, resultsPerPage, following);

        if ($scope.currentPage < $scope.pageCount - 1)
            $scope.currentPage++;
    };


    if (typeof ownerLogin !== 'undefined' && ownerLogin != null) {
        $scope.profileLogin = ownerLogin;
    }

    $scope.setFollowersSelected(true);
});


function setIsFollowing(array, profile, value) {
    array.forEach(function(item, i, array){
       if (item.login == profile.login)
           item.profile.isFollowing = value;
    });
}

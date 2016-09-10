app.controller('ProfileCtrl', function($scope, $window, ProfileService) {
    $scope.profile = null;
    $scope.profileLogin = null;

    var csrfToken = document.getElementsByName('_csrf')[0].content;
    var isFollowing;


    $scope.getProfile = function(login) {
        return ProfileService.get(login).then(
            function success(response) {
                if (response == null)
                    $window.location.href = url.errorWithMessage + message.error.profileNotFound;
                else
                    $scope.profile = response;
            },
            function error(response) {
                $window.location.href = url.errorWithMessage + 'Error ' + response.status;
            }
        );
    };

    $scope.follow = function(toFollow) {
        return ProfileService.follow(toFollow, csrfToken).then(
            function success(response) {
                if (response == 200) {
                    isFollowing = true;
                    //TODO display 'unfollow' button
                }
            }
        );
    };


    if (typeof ownerLogin !== 'undefined' && ownerLogin != null) {
        $scope.getProfile(ownerLogin);
        $scope.profileLogin = ownerLogin;
    } else {
        $scope.getProfile(currentLogin);
        $scope.profileLogin = currentLogin;
    }
});


app.service('ProfileService', function($http, $window) {
    this.get = function(login) {
        var profile = null;

        return $http.get(restUrl.profile + '/' + login).then(
            function success(response) {
                if (response.status == 204)
                    return profile;

                var data = response.data;

                if (data != null)
                    profile = new Profile(
                        data.name,
                        data.dateOfBirth,
                        data.phone,
                        data.country,
                        data.city,
                        data.info
                    );

                return profile;
            }
        );
    };

    this.put = function(login, profile, csrfToken) {
        var config = {
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN' : csrfToken
            }
        };

        return $http.put(restUrl.profile + '/' + login, profile, config);
    };

    this.follow = function(toFollow, csrfToken) {
        var config = {
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN' : csrfToken
            }
        };

        return $http.put(restUrl.profile + '/' + toFollow + '?follow', '', config).then(
            function success(response) {
                console.log(response);
                switch (response.status) {
                    case 200:
                        return response.status;
                        break;
                    case 204:
                        $window.location.href = url.errorWithMessage + message.error.profileNotFound;
                        break;
                    default:
                        $window.location.href = url.errorWithMessage + 'Error ' + response.status;
                }
            },
            function error(response) {
                console.log(response);
                $window.location.href = url.errorWithMessage + 'Error ' + response.status;
            }
        );
    };

    this.getFollowers = function(login) {
        var followers = [];

        return $http.get(restUrl.profile + '/' + login + '/followers').then(
            function success(response) {
                if (response.status == 204)
                    $window.location.href = url.errorWithMessage + message.error.profileNotFound;
                else if (response.data.length == 0)
                    return followers;

                var data = response.data;

                data.forEach(function(item, i, data){
                    var profile = new Profile(
                        item.name,
                        item.dateOfBirth,
                        item.phone,
                        item.country,
                        item.city,
                        item.info
                    );

                    followers.push({
                        login: item.login,
                        profile: profile
                    });
                });

                return followers;
            }
        );
    };

    this.isFollowing = function(toFollow) {

    };
});

app.service('AccountService', function($http) {
    this.get = function(login) {
        var account = null;

        return $http.get(restUrl.account + '/' + login).then(
            function (response) {
                if (response.status == 204)
                    return account;

                var data = response.data;

                if (data != null)
                    account = new Account(
                        data.login,
                        data.email);

                return account;
            }
        );
    };

    this.putEmail = function(login, account, csrfToken) {
        var config = {
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN' : csrfToken
            }
        };

        return $http.put(restUrl.account + '/' + login + '/email', account, config);
    };

    this.putPassword = function(login, account, csrfToken) {
        var config = {
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN' : csrfToken
            }
        };

        return $http.put(restUrl.account + '/' + login + '/password', account, config);
    };

    this.remove = function(login, csrfToken) {
        var config = {
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN' : csrfToken
            }
        };

        return $http.delete(restUrl.account + '/' + login, config);
    }
});
app.controller('ProfileCtrl', function($scope, $window, ProfileService, MessageService) {
    $scope.profile = null;
    $scope.profileLogin = null;
    $scope.isFollowing = null;
    $scope.messages = [];
    $scope.newMessage = new Message();

    var csrfToken = document.getElementsByName('_csrf')[0].content;


    $scope.getProfile = function (login) {
        return ProfileService.get(login).then(
            function success(response) {
                if (response == null)
                    $window.location.href = url.errorWithMessage + message.error.profileNotFound;
                else {
                    $scope.profile = response;
                    $scope.isFollowing = $scope.profile.isFollowing;
                }
            },
            function error(response) {
                $window.location.href = url.errorWithMessage + 'Error ' + response.status;
            }
        );
    };

    $scope.follow = function (toFollow) {
        return ProfileService.follow(toFollow, true, csrfToken).then(
            function success (response) {
                if (response == 200)
                    $scope.isFollowing = true;
            }
        );
    };

    $scope.unfollow = function( toUnfollow) {
        return ProfileService.follow(toUnfollow, false, csrfToken).then(
            function success(response) {
                if (response == 200)
                    $scope.isFollowing = false;
            }
        );
    };

    // TODO pagination
    $scope.getMessages = function() {
        MessageService.get($scope.profileLogin).then(
            function(response){
                response.forEach(function(item, i, response){
                    var timestamp = new Date(item.createdAt);
                    var readableDate = timestamp.toLocaleTimeString() + ' ' + timestamp.toLocaleDateString();

                    item.createdAt = readableDate;
                });

                $scope.messages = response;
            }
        );
    };

    $scope.sendMessage = function() {
        $scope.newMessage.senderLogin = currentLogin;
        $scope.newMessage.createdAt = new Date();

        MessageService.post($scope.newMessage, csrfToken).then(
            function (response) {
                if (response.status == 200) {
                    //    TODO status 'message sent'
                    $scope.newMessage.textContent = '';
                    $scope.getMessages();
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

    $scope.getMessages();
});


app.service('ProfileService', function ($http, $window) {
    this.get = function(login) {
        var profile = null;

        return $http.get(restUrl.profile + '/' + login).then(
            function success(response) {
                if (response.status == 204)
                    return profile;

                var data = response.data;

                if (data != null) {
                    profile = new Profile(
                        data.name,
                        data.dateOfBirth,
                        data.phone,
                        data.country,
                        data.city,
                        data.info
                    );

                    if (data.following !== 'undefined')
                        profile.isFollowing = data.following;
                }

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

    /**
     * @param {Profile} otherProfile - login of profile to (un)follow
     * @param {boolean} isFollow - true if follow, false if unfollow
     * */
    this.follow = function (otherProfile, isFollow, csrfToken) {
        var config = {
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN' : csrfToken
            }
        };

        var requestParam = isFollow ? '?follow' : '?unfollow';

        return $http.put(restUrl.profile + '/' + otherProfile + requestParam, '', config).then(
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

    this.getFollowers = function (login) {
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

                    if (item.following !== 'undefined')
                        profile.isFollowing = item.following;

                    followers.push({
                        login: item.login,
                        profile: profile
                    });
                });

                return followers;
            }
        );
    };

    this.getFollowing = function (login) {
        var following = [];

        return $http.get(restUrl.profile + '/' + login + '/following').then(
            function success(response) {
                if (response.status == 204)
                    $window.location.href = url.errorWithMessage + message.error.profileNotFound;
                else if (response.data.length == 0)
                    return following;

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

                    if (item.following !== 'undefined')
                        profile.isFollowing = item.following;

                    following.push({
                        login: item.login,
                        profile: profile
                    });
                });

                return following;
            }
        );
    };
});

app.service('AccountService', function ($http, $window) {
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

    this.putEmail = function (login, account, csrfToken) {
        var config = {
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN' : csrfToken
            }
        };

        return $http.put(restUrl.account + '/' + login + '/email', account, config);
    };

    this.putPassword = function (login, account, csrfToken) {
        var config = {
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN' : csrfToken
            }
        };

        return $http.put(restUrl.account + '/' + login + '/password', account, config);
    };

    this.remove = function (login, csrfToken) {
        var config = {
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN' : csrfToken
            }
        };

        return $http.delete(restUrl.account + '/' + login, config);
    };

    this.logout = function (csrfToken) {
        var config = {
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN': csrfToken
            }
        };

        $http.post(url.logout, '', config).then(
            function () {
                $window.location.href = url.login + '?delete';
            });
    }
});
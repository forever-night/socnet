var paramSearchQuery;

app.controller('SearchResultCtrl', function($scope, SearchService, StatusService, ProfileService, PageService){
    $scope.searchQuery = paramSearchQuery != null ? paramSearchQuery : '';
    $scope.searchResult = [];
    $scope.currentLogin = currentLogin;
    $scope.currentPage = 0;
    $scope.pageCount = 0;

    var searchResult = [];
    var status = document.getElementById('status');
    var csrfToken = document.getElementsByName('_csrf')[0].content;

    var resultsPerPage = 20;


    $scope.search = function (query) {
        StatusService.hideStatus(status);

        if ($scope.searchQuery.length == 0 || $scope.searchQuery == '')
            StatusService.setStatus(status, false, message.error.queryEmpty);


        SearchService.search(query).then(
            function success(response) {
                searchResult = response;
                $scope.pageCount = Math.ceil(searchResult.length / resultsPerPage);

                if (searchResult.length == 0)
                    StatusService.setStatus(status, true, message.error.profileNotFound);

                searchResult.forEach(function(item, i, searchResult) {
                    if (i >= resultsPerPage)
                        return;

                    $scope.searchResult.push(searchResult[i]);
                });
            },
            function error(response) {
                var errorMessage = response.status == 400 ? message.error.queryEmpty : 'Error ' + response.status;
                StatusService.setStatus(status, false, errorMessage);
            }
        );
    };

    $scope.follow = function (profileToFollow) {
        return ProfileService.follow(profileToFollow.login, true, csrfToken).then(
            function success(response) {
                if (response == 200)
                    profileToFollow.profile.isFollowing = true;
            }
        )
    };

    $scope.unfollow = function (profileToUnfollow) {
        return ProfileService.follow(profileToUnfollow.login, false, csrfToken).then(
            function success(response) {
                if (response == 200)
                    profileToUnfollow.profile.isFollowing = false;
            }
        );
    };

    $scope.prevPage = function() {
        $scope.searchResult = PageService.prevPage($scope.currentPage, resultsPerPage, searchResult);

        if ($scope.currentPage > 0)
            $scope.currentPage--;
    };

    $scope.nextPage = function() {
        $scope.searchResult = PageService.nextPage($scope.currentPage, resultsPerPage, searchResult);

        if ($scope.currentPage < $scope.pageCount - 1)
            $scope.currentPage++;
    };


    if ($scope.searchQuery != '')
        $scope.search($scope.searchQuery);
});

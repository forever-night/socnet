var paramSearchQuery;

app.controller('SearchResultCtrl', function($scope, SearchService, StatusService, ProfileService){
    $scope.searchQuery = paramSearchQuery != null ? paramSearchQuery : '';
    $scope.searchResult = [];
    $scope.currentLogin = currentLogin;

    var searchResult = [];
    var status = document.getElementById('status');
    var csrfToken = document.getElementsByName('_csrf')[0].content;


    $scope.search = function(query) {
        StatusService.hideStatus(status);

        if ($scope.searchQuery.length == 0 || $scope.searchQuery == '')
            StatusService.setStatus(status, false, message.error.queryEmpty);


        SearchService.search(query).then(
            function success(response) {
                searchResult = response;

                if (searchResult.length == 0)
                    StatusService.setStatus(status, true, message.error.profileNotFound);

                // TODO make multiple pages for big results
                searchResult.forEach(function(item, i, searchResult) {
                    if (i >= 10)
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

    $scope.follow = function (toFollow, searchResultIndex) {
        return ProfileService.follow(toFollow, csrfToken).then(
            function success(response) {
                if (response == 200) {
                    console.log('following');
                    $scope.searchResult[searchResultIndex].isFollowing = true;
                //    TODO display 'unfollow' button
                }
            }
        )
    };


    if ($scope.searchQuery != '')
        $scope.search($scope.searchQuery);
});

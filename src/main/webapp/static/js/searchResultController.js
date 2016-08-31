var paramSearchQuery;

app.controller('SearchResultCtrl', function($scope, SearchService, StatusService){
    $scope.searchQuery = paramSearchQuery != null ? paramSearchQuery : '';
    $scope.searchResult = [];

    var searchResult = [];
    var status = document.getElementById('status');


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
                console.log(response);

                var errorMessage = response.status == 400 ? message.error.queryEmpty : 'Error ' + response.status;
                StatusService.setStatus(status, false, errorMessage);
            }
        );
    };


    if ($scope.searchQuery != '')
        $scope.search($scope.searchQuery);
});

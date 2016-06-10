app.controller('SignUpCtrl', function ($scope, $http, $window) {
    $scope.postAccount = function(data) {
        var config = {
            headers: {'Content-Type': 'application/json'}
        };

        var request = $http.post('/socnet/api/account', data, config).then(
            function success(response) {
                console.log('success');
                $window.location.href = response.headers('Location');
            },
            function error(response) {
                console.log('error');
                console.log('status ' + response.status);
            }
        );
    };
});
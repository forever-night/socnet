app = angular.module('app', []);


app.controller('MainCtrl', function ($scope) {
    $scope.id = 0;
    $scope.authorized= false;

    $scope.setId = function(id) {
        $scope.id = id;
    };

    $scope.setAuthorized = function(value) {
        $scope.authorized = value;
    };
});

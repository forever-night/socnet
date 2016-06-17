app.controller('SignInCtrl', function($scope, $http, $window, StatusService) {
    var status = document.getElementById('status');


    $scope.signIn = function(data) {
        StatusService.setStatus(status, true, 'signing up...');

        $http.post(restUrl.account + "signin", data, configJson).then(
            function (response) {
                if (response.data > 0)
                    $window.location.href = response.headers('Location');
                else
                    StatusService.setStatus(status, false, 'Login or password is incorrect');
            }
        );
    };
});
app = angular.module('app', []);

url = {
    login: context + '/login',
    profile: context + '/profile',
    settings: context + '/settings',
    error: context + '/error',
    errorWithMessage: context + '/error?errorMessage=',
    logout: context + '/logout',
    accessDenied: context + '/403'
};

restUrl = {
    profile: context + '/api/profile',
    search: context + '/api/profile?search=',
    account: context + '/api/account',
    publicMessage: context + '/api/public'
};

configJson = {
    headers: {
        'Content-Type': 'application/json'
    }
};

message = {
    success: {
        success: 'Success!',
        logout: 'Logout successful!',
        accountCreated: 'Account created successfully!',
        accountDeleted: 'Account deleted successfully!'
    },
    error: {
        invalidCredentials: 'Invalid login or password',
        signup: 'Unable to sign up',
        fieldEmpty: 'One of the fields is empty',
        queryEmpty: 'Search query is empty',
        emailFormat: 'Unacceptable e-mail format',
        emailNotModified: 'e-mail is not modified',
        loginTaken: 'Login is taken, try a different one',
        passwordMatch: 'Passwords should match',
        wrongPassword: 'Invalid old password',
        profileNotFound: 'Profile not found',
        accountNotFound: 'Account not found',
        internalError: 'Internal server error'
    }
};


app.controller('MainCtrl', function ($scope) {
    $scope.searchQuery = '';
});


app.service('ValidateService', function () {
    this.validateEmail = function (email) {
        var pattern = /^\w[\w\.]*\w\@[a-zA-Z0-9]+\.[a-zA-Z]{2,}$/;      // match: aa@aa.aa
        var regex = new RegExp(pattern);

        return regex.test(email);
    };

    this.validateConfirmPassword = function (newPassword, confirmPassword) {
        return (newPassword == confirmPassword);
    };
});

app.service('StatusService', function () {
    this.setStatus = function (statusElement, isSuccessful, message) {
        var elementClass = 'alert';
        elementClass += isSuccessful ? ' alert-success' : ' alert-danger';

        statusElement.style.visibility = 'visible';
        statusElement.innerHTML = message;
        statusElement.setAttribute('class', elementClass);
    };

    this.hideStatus = function(statusElement) {
        statusElement.style.visibility = 'hidden';
    };
});

app.service('SearchService', function ($http) {
   this.search = function (query) {
       var searchResult = [];

       return $http.get(restUrl.search + query).then(
           function success(response) {
               if (response.status == 204 || response.data.length == 0)
                   return searchResult;

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

                   searchResult.push({
                       login: item.login,
                       profile: profile
                   });
               });

               return searchResult;
           }
       );
   }
});

app.service('PageService', function() {
    this.prevPage = function(currentPage, resultsPerPage, resultArray) {
        var prevPage = currentPage - 1;

        if (prevPage >= 0) {
            var from = resultsPerPage * prevPage;
            var to = resultsPerPage * currentPage;

            var resultArrayToLoad = [];

            for (var i = from; i < to; i++)
                resultArrayToLoad.push(resultArray[i]);
        }

        return resultArrayToLoad;
    };

    this.nextPage = function(currentPage, resultsPerPage, resultArray) {
        var nextPage = currentPage + 1;

        if (resultArray.length > resultsPerPage * nextPage) {
            var from = resultsPerPage * nextPage;
            var to = resultsPerPage * (nextPage + 1);

            // if actual array is smaller than calculated 'to' variable
            to = resultArray.length >= to ? to : resultArray.length;

            var resultArrayToLoad = [];

            for (var i = from; i < to; i++)
                resultArrayToLoad.push(resultArray[i]);
        }

        return resultArrayToLoad;
    };
});

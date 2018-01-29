(function() {
    'use strict';

    angular.module('employeeManagerApp')
        .controller('LoginController', ['$scope', '$rootScope', 'loginService', '$http', 'store', '$location', 'jwtHelper', function ($scope, $rootScope, loginService, $http, store, $location, jwtHelper) {
            $scope.user = {
                username: null,
                password: null
            };

            $scope.login = function () {
                loginService.login($scope.user.username, $scope.user.password).then(function (success){
                    // Saves token to local storage and redirects to "employees" page
                    var date = jwtHelper.getTokenExpirationDate(success.data.access_token);

                    store.set('access_token', success.data.access_token);
                    $location.path('/employees');

                },function (error){
                    // called asynchronously if an error occurs
                    // or server returns response with an error status.
                    console.log(" error: "+error);
                });

            };

    } ]);

})();

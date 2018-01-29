(function() {

    'use strict';

    angular.module('employeeManagerApp').factory('loginService', ['$http', '$httpParamSerializer', 'API_BASE_AUTH',
    function ($http, $httpParamSerializer, API_BASE_AUTH) {
        return {
            login : function(uname, pwd) {
                var data = {
                    grant_type:"password",
                    username: uname,
                    password: pwd,
                    client_id: "rajithapp"
                };
                var request = {
                    method: 'POST',
                    url: API_BASE_AUTH,
                    headers: {
                        "Authorization": "Basic " + btoa("rajithapp:secret"),
                        "Content-type": "application/x-www-form-urlencoded; charset=utf-8"
                    },
                    data: $httpParamSerializer(data)
                };
                return $http(request);
            }
        };
    }]);

})();

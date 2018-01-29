(function() {
    'use strict';

    angular.module('employeeManagerApp').constant('API_BASE', 'http://localhost:8080/api');
    angular.module('employeeManagerApp').constant('API_BASE_AUTH', 'http://localhost:8765//api/auth-service/oauth/token');
    //angular.module('employeeManagerApp').constant('API_BASE_AUTH', 'http://localhost:8280/oauth/token');
})();

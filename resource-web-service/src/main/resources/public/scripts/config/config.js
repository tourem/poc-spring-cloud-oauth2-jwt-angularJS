(function() {
    'use strict';

    angular.module('employeeManagerApp').constant('API_BASE', 'http://web.docker.localhost/api');
   // angular.module('employeeManagerApp').constant('API_BASE', 'http://localhost:9191/api');
    angular.module('employeeManagerApp').constant('API_BASE_AUTH', 'http://auth.docker.localhost/oauth/token');
    //angular.module('employeeManagerApp').constant('API_BASE_AUTH', 'http://localhost:8280/oauth/token');
})();

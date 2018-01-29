(function() {

    'use strict';

    angular.module('employeeManagerApp').factory('employeeService', ['$http', 'API_BASE', function($http, API_BASE) {
        return {
            findAll: function(callback) {
                    return $http.get(API_BASE + '/admin/employees').then(function (success){
                        console.log("========success============"+success);
                        callback(success);
                    },function (error){
                        console.log("========errorCallback============"+error);
                    });
            },
            save: function (employee) {
                    return $http.post(API_BASE + '/admin/employees', { firstName: employee.firstName, lastName: employee.lastName, age: employee.age });
            }
        };
    }]);

})();

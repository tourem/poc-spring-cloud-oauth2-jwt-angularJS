(function() {

    'use strict';

    angular.module('employeeManagerApp').controller('EmployeeController', ['$scope','employeeService', function ($scope, employeeService) {
        initEmployee();

        console.log("=========================");
        employeeService.findAll(function (data) {
            $scope.employees = data.data;
            console.log(data);
        });

        $scope.saveEmployee = function () {
            employeeService.save($scope.employee)
                .then(function(response) {
                    if(response.status === 200) {
                      employeeService.findAll(function (data) {
                          console.log(data);
                          console.log("ddddd : "+data);
                          $scope.employees = data.data;
                      });
                    }
                }, function(response) {
                    // Handle error
                    console.log(response);
            });
            initEmployee();
        };

        function initEmployee() {
            $scope.employee = {
                firstName: null,
                lastName: null,
                age: null
            };
        }
    }]);
})();

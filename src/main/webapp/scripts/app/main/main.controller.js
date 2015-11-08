'use strict';

angular.module('primApp')
    .controller('MainController', function ($scope, Principal) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;

            if(!$scope.isAuthenticated()){
                window.location="/#/login";
            }
        });
    });

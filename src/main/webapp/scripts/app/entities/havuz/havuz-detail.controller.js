'use strict';

angular.module('primApp')
    .controller('HavuzDetailController', function ($scope, $rootScope, $stateParams, entity, Havuz) {
        $scope.havuz = entity;
        $scope.load = function (id) {
            Havuz.get({id: id}, function(result) {
                $scope.havuz = result;
            });
        };
        $rootScope.$on('primApp:havuzUpdate', function(event, result) {
            $scope.havuz = result;
        });
    });

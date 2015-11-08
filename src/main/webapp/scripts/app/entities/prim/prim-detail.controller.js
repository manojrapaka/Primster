'use strict';

angular.module('primApp')
    .controller('PrimDetailController', function ($scope, $rootScope, $stateParams, entity, Prim) {
        $scope.prim = entity;
        $scope.load = function (id) {
            Prim.get({id: id}, function(result) {
                $scope.prim = result;
            });
        };
        $rootScope.$on('primApp:primUpdate', function(event, result) {
            $scope.prim = result;
        });
    });

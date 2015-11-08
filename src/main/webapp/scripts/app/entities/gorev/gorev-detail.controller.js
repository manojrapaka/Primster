'use strict';

angular.module('primApp')
    .controller('GorevDetailController', function ($scope, $rootScope, $stateParams, entity, Gorev, Prim) {
        $scope.gorev = entity;
        $scope.load = function (id) {
            Gorev.get({id: id}, function(result) {
                $scope.gorev = result;
            });
        };
        $rootScope.$on('primApp:gorevUpdate', function(event, result) {
            $scope.gorev = result;
        });
    });

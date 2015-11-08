'use strict';

angular.module('primApp')
    .controller('HakedisDetailController', function ($scope, $rootScope, $stateParams, entity, Hakedis, Calisan) {
        $scope.hakedis = entity;
        $scope.load = function (id) {
            Hakedis.get({id: id}, function(result) {
                $scope.hakedis = result;
            });
        };
        $rootScope.$on('primApp:hakedisUpdate', function(event, result) {
            $scope.hakedis = result;
        });
    });

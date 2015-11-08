'use strict';

angular.module('primApp')
    .controller('CalisanDetailController', function ($scope, $rootScope, $stateParams, entity, Calisan, Gorev) {
        $scope.calisan = entity;
        $scope.load = function (id) {
            Calisan.get({id: id}, function(result) {
                $scope.calisan = result;
            });
        };
        $rootScope.$on('primApp:calisanUpdate', function(event, result) {
            $scope.calisan = result;
        });
    });

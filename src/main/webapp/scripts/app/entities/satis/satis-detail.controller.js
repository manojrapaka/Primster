'use strict';

angular.module('primApp')
    .controller('SatisDetailController', function ($scope, $rootScope, $stateParams, entity, Satis, Calisan, Ulke) {
        $scope.satis = entity;
        $scope.load = function (id) {
            Satis.get({id: id}, function(result) {
                $scope.satis = result;
            });
        };
        $rootScope.$on('primApp:satisUpdate', function(event, result) {
            $scope.satis = result;
        });
    });

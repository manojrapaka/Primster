'use strict';

angular.module('primApp')
    .controller('GrupDetailController', function ($scope, $rootScope, $stateParams, entity, Grup) {
        $scope.grup = entity;
        $scope.load = function (id) {
            Grup.get({id: id}, function(result) {
                $scope.grup = result;
            });
        };
        $rootScope.$on('primApp:grupUpdate', function(event, result) {
            $scope.grup = result;
        });
    });

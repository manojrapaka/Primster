'use strict';

angular.module('primApp')
    .controller('UlkeDetailController', function ($scope, $rootScope, $stateParams, entity, Ulke, Grup) {
        $scope.ulke = entity;
        $scope.load = function (id) {
            Ulke.get({id: id}, function(result) {
                $scope.ulke = result;
            });
        };
        $rootScope.$on('primApp:ulkeUpdate', function(event, result) {
            $scope.ulke = result;
        });
    });

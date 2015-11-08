'use strict';

angular.module('primApp')
    .controller('HistoryDetailController', function ($scope, $rootScope, $stateParams, entity, History) {
        $scope.history = entity;
        $scope.load = function (id) {
            History.get({id: id}, function(result) {
                $scope.history = result;
            });
        };
        $rootScope.$on('primApp:historyUpdate', function(event, result) {
            $scope.history = result;
        });
    });

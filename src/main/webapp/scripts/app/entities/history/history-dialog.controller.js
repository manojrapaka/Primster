'use strict';

angular.module('primApp').controller('HistoryDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'History',
        function($scope, $stateParams, $modalInstance, entity, History) {

        $scope.history = entity;
        $scope.load = function(id) {
            History.get({id : id}, function(result) {
                $scope.history = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('primApp:historyUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.history.id != null) {
                History.update($scope.history, onSaveFinished);
            } else {
                History.save($scope.history, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

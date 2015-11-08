'use strict';

angular.module('primApp').controller('HavuzDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Havuz',
        function($scope, $stateParams, $modalInstance, entity, Havuz) {

        $scope.havuz = entity;
        $scope.load = function(id) {
            Havuz.get({id : id}, function(result) {
                $scope.havuz = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('primApp:havuzUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.havuz.id != null) {
                Havuz.update($scope.havuz, onSaveFinished);
            } else {
                Havuz.save($scope.havuz, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

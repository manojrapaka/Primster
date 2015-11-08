'use strict';

angular.module('primApp').controller('PrimDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Prim',
        function($scope, $stateParams, $modalInstance, entity, Prim) {

        $scope.prim = entity;
        $scope.load = function(id) {
            Prim.get({id : id}, function(result) {
                $scope.prim = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('primApp:primUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.prim.id != null) {
                Prim.update($scope.prim, onSaveFinished);
            } else {
                Prim.save($scope.prim, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

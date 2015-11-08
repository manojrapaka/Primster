'use strict';

angular.module('primApp').controller('GorevDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Gorev', 'Prim',
        function($scope, $stateParams, $modalInstance, entity, Gorev, Prim) {

        $scope.gorev = entity;
        $scope.prims = Prim.query();
        $scope.load = function(id) {
            Gorev.get({id : id}, function(result) {
                $scope.gorev = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('primApp:gorevUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.gorev.id != null) {
                Gorev.update($scope.gorev, onSaveFinished);
            } else {
                Gorev.save($scope.gorev, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

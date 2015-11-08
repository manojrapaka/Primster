'use strict';

angular.module('primApp').controller('UlkeDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Ulke', 'Grup',
        function($scope, $stateParams, $modalInstance, entity, Ulke, Grup) {

        $scope.ulke = entity;
        $scope.grups = Grup.query();
        $scope.load = function(id) {
            Ulke.get({id : id}, function(result) {
                $scope.ulke = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('primApp:ulkeUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.ulke.id != null) {
                Ulke.update($scope.ulke, onSaveFinished);
            } else {
                Ulke.save($scope.ulke, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

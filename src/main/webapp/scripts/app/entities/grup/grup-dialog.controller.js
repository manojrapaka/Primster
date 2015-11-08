'use strict';

angular.module('primApp').controller('GrupDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Grup',
        function($scope, $stateParams, $modalInstance, entity, Grup) {

        $scope.grup = entity;
        $scope.load = function(id) {
            Grup.get({id : id}, function(result) {
                $scope.grup = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('primApp:grupUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.grup.id != null) {
                Grup.update($scope.grup, onSaveFinished);
            } else {
                Grup.save($scope.grup, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

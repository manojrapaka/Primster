'use strict';

angular.module('primApp').controller('HakedisDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Hakedis', 'Calisan',
        function($scope, $stateParams, $modalInstance, entity, Hakedis, Calisan) {

        $scope.hakedis = entity;
        $scope.calisans = Calisan.query();
        $scope.load = function(id) {
            Hakedis.get({id : id}, function(result) {
                $scope.hakedis = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('primApp:hakedisUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.hakedis.id != null) {
                Hakedis.update($scope.hakedis, onSaveFinished);
            } else {
                Hakedis.save($scope.hakedis, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

'use strict';

angular.module('primApp').controller('CalisanDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Calisan', 'Gorev',
        function($scope, $stateParams, $modalInstance, entity, Calisan, Gorev) {

        $scope.calisan = entity;
        $scope.gorevs = Gorev.query();
        $scope.load = function(id) {
            Calisan.get({id : id}, function(result) {
                $scope.calisan = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('primApp:calisanUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.calisan.id != null) {
                Calisan.update($scope.calisan, onSaveFinished);
            } else {
                Calisan.save($scope.calisan, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

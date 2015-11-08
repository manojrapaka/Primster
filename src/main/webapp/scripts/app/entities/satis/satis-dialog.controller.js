'use strict';

angular.module('primApp').controller('SatisDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Satis', 'Calisan', 'Ulke',
        function($scope, $stateParams, $modalInstance, entity, Satis, Calisan, Ulke) {

        $scope.satis = entity;
        $scope.calisans = Calisan.query();
        $scope.ulkes = Ulke.query();
        $scope.load = function(id) {
            Satis.get({id : id}, function(result) {
                $scope.satis = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('primApp:satisUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.satis.id != null) {
                Satis.update($scope.satis, onSaveFinished);
            } else {
                Satis.save($scope.satis, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

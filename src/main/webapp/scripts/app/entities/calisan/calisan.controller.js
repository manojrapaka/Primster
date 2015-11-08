'use strict';

angular.module('primApp')
    .controller('CalisanController', function ($scope, Calisan, ParseLinks) {
        $scope.calisans = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Calisan.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.calisans.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.calisans = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Calisan.get({id: id}, function(result) {
                $scope.calisan = result;
                $('#deleteCalisanConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Calisan.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deleteCalisanConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.reset();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.calisan = {
                adi: null,
                id: null
            };
        };
    });

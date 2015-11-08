'use strict';

angular.module('primApp')
    .controller('UlkeController', function ($scope, Ulke, ParseLinks) {
        $scope.ulkes = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Ulke.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.ulkes.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.ulkes = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Ulke.get({id: id}, function(result) {
                $scope.ulke = result;
                $('#deleteUlkeConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Ulke.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deleteUlkeConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.reset();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.ulke = {
                adi: null,
                id: null
            };
        };
    });

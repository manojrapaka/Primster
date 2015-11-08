'use strict';

angular.module('primApp')
    .controller('GrupController', function ($scope, Grup, ParseLinks) {
        $scope.grups = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Grup.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.grups.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.grups = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Grup.get({id: id}, function(result) {
                $scope.grup = result;
                $('#deleteGrupConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Grup.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deleteGrupConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.reset();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.grup = {
                adi: null,
                katsayi: null,
                id: null
            };
        };
    });

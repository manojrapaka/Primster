'use strict';

angular.module('primApp')
    .controller('GorevController', function ($scope, Gorev, ParseLinks) {
        $scope.gorevs = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Gorev.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.gorevs.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.gorevs = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Gorev.get({id: id}, function(result) {
                $scope.gorev = result;
                $('#deleteGorevConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Gorev.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deleteGorevConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.reset();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.gorev = {
                adi: null,
                id: null
            };
        };
    });

'use strict';

angular.module('primApp')
    .controller('PrimController', function ($scope, Prim, ParseLinks) {
        $scope.prims = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Prim.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.prims.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.prims = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Prim.get({id: id}, function(result) {
                $scope.prim = result;
                $('#deletePrimConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Prim.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deletePrimConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.reset();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.prim = {
                adi: null,
                yuzde: null,
                id: null
            };
        };
    });

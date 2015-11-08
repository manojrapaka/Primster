'use strict';

angular.module('primApp')
    .controller('HakedisController', function ($scope, Hakedis, ParseLinks) {
        $scope.hakediss = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Hakedis.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.hakediss.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.hakediss = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Hakedis.get({id: id}, function(result) {
                $scope.hakedis = result;
                $('#deleteHakedisConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Hakedis.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deleteHakedisConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.reset();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.hakedis = {
                tarih: null,
                basTarih: null,
                bitTarih: null,
                prim: null,
                id: null
            };
        };
    });

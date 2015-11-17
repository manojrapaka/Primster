'use strict';

angular.module('primApp')
    .controller('SatisController', function ($scope, Satis, ParseLinks) {
        $scope.satiss = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Satis.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.satiss.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.satiss = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Satis.get({id: id}, function(result) {
                $scope.satis = result;
                $('#deleteSatisConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Satis.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deleteSatisConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.reset();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.satis = {
                faturaNo: null,
                tarih: null,
                tutar: null,
                faturaTarih: null,
                id: null
            };
        };
    });

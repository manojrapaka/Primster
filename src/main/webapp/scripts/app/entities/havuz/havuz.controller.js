'use strict';

angular.module('primApp')
    .controller('HavuzController', function ($scope, Havuz, ParseLinks) {
        $scope.havuzs = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Havuz.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.havuzs.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.havuzs = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Havuz.get({id: id}, function(result) {
                $scope.havuz = result;
                $('#deleteHavuzConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Havuz.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deleteHavuzConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.reset();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.havuz = {
                tarih: null,
                basTarih: null,
                bitTarih: null,
                tutar: null,
                id: null
            };
        };
    });

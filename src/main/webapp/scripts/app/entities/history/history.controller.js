'use strict';

angular.module('primApp')
    .controller('HistoryController', function ($scope, History, ParseLinks) {
        $scope.historys = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            History.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.historys.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.historys = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            History.get({id: id}, function(result) {
                $scope.history = result;
                $('#deleteHistoryConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            History.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deleteHistoryConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.reset();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.history = {
                kolonAdi: null,
                deger: null,
                tabloAdi: null,
                tarih: null,
                recId: null,
                id: null
            };
        };
    });

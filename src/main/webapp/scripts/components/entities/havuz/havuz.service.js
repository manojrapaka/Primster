'use strict';

angular.module('primApp')
    .factory('Havuz', function ($resource, DateUtils) {
        return $resource('api/havuzs/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.tarih = DateUtils.convertLocaleDateFromServer(data.tarih);
                    data.basTarih = DateUtils.convertLocaleDateFromServer(data.basTarih);
                    data.bitTarih = DateUtils.convertLocaleDateFromServer(data.bitTarih);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.tarih = DateUtils.convertLocaleDateToServer(data.tarih);
                    data.basTarih = DateUtils.convertLocaleDateToServer(data.basTarih);
                    data.bitTarih = DateUtils.convertLocaleDateToServer(data.bitTarih);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.tarih = DateUtils.convertLocaleDateToServer(data.tarih);
                    data.basTarih = DateUtils.convertLocaleDateToServer(data.basTarih);
                    data.bitTarih = DateUtils.convertLocaleDateToServer(data.bitTarih);
                    return angular.toJson(data);
                }
            }
        });
    });

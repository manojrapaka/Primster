'use strict';

angular.module('primApp')
    .factory('Satis', function ($resource, DateUtils) {
        return $resource('api/satiss/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.tarih = DateUtils.convertLocaleDateFromServer(data.tarih);
                    data.faturaTarih = DateUtils.convertLocaleDateFromServer(data.faturaTarih);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.tarih = DateUtils.convertLocaleDateToServer(data.tarih);
                    data.faturaTarih = DateUtils.convertLocaleDateToServer(data.faturaTarih);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.tarih = DateUtils.convertLocaleDateToServer(data.tarih);
                    data.faturaTarih = DateUtils.convertLocaleDateToServer(data.faturaTarih);
                    return angular.toJson(data);
                }
            }
        });
    });

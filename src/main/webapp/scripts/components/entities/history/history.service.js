'use strict';

angular.module('primApp')
    .factory('History', function ($resource, DateUtils) {
        return $resource('api/historys/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.tarih = DateUtils.convertLocaleDateFromServer(data.tarih);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.tarih = DateUtils.convertLocaleDateToServer(data.tarih);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.tarih = DateUtils.convertLocaleDateToServer(data.tarih);
                    return angular.toJson(data);
                }
            }
        });
    });

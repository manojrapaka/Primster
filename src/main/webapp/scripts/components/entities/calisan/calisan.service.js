'use strict';

angular.module('primApp')
    .factory('Calisan', function ($resource, DateUtils) {
        return $resource('api/calisans/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });

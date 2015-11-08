'use strict';

angular.module('primApp')
    .factory('Ulke', function ($resource, DateUtils) {
        return $resource('api/ulkes/:id', {}, {
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

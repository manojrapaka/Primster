'use strict';

angular.module('primApp')
    .factory('Prim', function ($resource, DateUtils) {
        return $resource('api/prims/:id', {}, {
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

'use strict';

angular.module('primApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });



'use strict';

angular.module('primApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('hakedis', {
                parent: 'entity',
                url: '/hakediss',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'primApp.hakedis.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/hakedis/hakediss.html',
                        controller: 'HakedisController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('hakedis');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('hakedis.detail', {
                parent: 'entity',
                url: '/hakedis/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'primApp.hakedis.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/hakedis/hakedis-detail.html',
                        controller: 'HakedisDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('hakedis');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Hakedis', function($stateParams, Hakedis) {
                        return Hakedis.get({id : $stateParams.id});
                    }]
                }
            })
            .state('hakedis.new', {
                parent: 'hakedis',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/hakedis/hakedis-dialog.html',
                        controller: 'HakedisDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    tarih: null,
                                    basTarih: null,
                                    bitTarih: null,
                                    prim: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('hakedis', null, { reload: true });
                    }, function() {
                        $state.go('hakedis');
                    })
                }]
            })
            .state('hakedis.edit', {
                parent: 'hakedis',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/hakedis/hakedis-dialog.html',
                        controller: 'HakedisDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Hakedis', function(Hakedis) {
                                return Hakedis.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('hakedis', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

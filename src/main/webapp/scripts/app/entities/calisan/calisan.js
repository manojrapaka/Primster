'use strict';

angular.module('primApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('calisan', {
                parent: 'entity',
                url: '/calisans',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'primApp.calisan.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/calisan/calisans.html',
                        controller: 'CalisanController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('calisan');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('calisan.detail', {
                parent: 'entity',
                url: '/calisan/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'primApp.calisan.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/calisan/calisan-detail.html',
                        controller: 'CalisanDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('calisan');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Calisan', function($stateParams, Calisan) {
                        return Calisan.get({id : $stateParams.id});
                    }]
                }
            })
            .state('calisan.new', {
                parent: 'calisan',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/calisan/calisan-dialog.html',
                        controller: 'CalisanDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    adi: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('calisan', null, { reload: true });
                    }, function() {
                        $state.go('calisan');
                    })
                }]
            })
            .state('calisan.edit', {
                parent: 'calisan',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/calisan/calisan-dialog.html',
                        controller: 'CalisanDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Calisan', function(Calisan) {
                                return Calisan.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('calisan', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

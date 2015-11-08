'use strict';

angular.module('primApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('havuz', {
                parent: 'entity',
                url: '/havuzs',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'primApp.havuz.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/havuz/havuzs.html',
                        controller: 'HavuzController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('havuz');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('havuz.detail', {
                parent: 'entity',
                url: '/havuz/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'primApp.havuz.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/havuz/havuz-detail.html',
                        controller: 'HavuzDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('havuz');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Havuz', function($stateParams, Havuz) {
                        return Havuz.get({id : $stateParams.id});
                    }]
                }
            })
            .state('havuz.new', {
                parent: 'havuz',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/havuz/havuz-dialog.html',
                        controller: 'HavuzDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    tarih: null,
                                    basTarih: null,
                                    bitTarih: null,
                                    tutar: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('havuz', null, { reload: true });
                    }, function() {
                        $state.go('havuz');
                    })
                }]
            })
            .state('havuz.edit', {
                parent: 'havuz',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/havuz/havuz-dialog.html',
                        controller: 'HavuzDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Havuz', function(Havuz) {
                                return Havuz.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('havuz', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

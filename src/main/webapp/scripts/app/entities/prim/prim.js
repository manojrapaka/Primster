'use strict';

angular.module('primApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('prim', {
                parent: 'entity',
                url: '/prims',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'primApp.prim.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/prim/prims.html',
                        controller: 'PrimController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('prim');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('prim.detail', {
                parent: 'entity',
                url: '/prim/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'primApp.prim.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/prim/prim-detail.html',
                        controller: 'PrimDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('prim');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Prim', function($stateParams, Prim) {
                        return Prim.get({id : $stateParams.id});
                    }]
                }
            })
            .state('prim.new', {
                parent: 'prim',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/prim/prim-dialog.html',
                        controller: 'PrimDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    adi: null,
                                    yuzde: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('prim', null, { reload: true });
                    }, function() {
                        $state.go('prim');
                    })
                }]
            })
            .state('prim.edit', {
                parent: 'prim',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/prim/prim-dialog.html',
                        controller: 'PrimDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Prim', function(Prim) {
                                return Prim.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('prim', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

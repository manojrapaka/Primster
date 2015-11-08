'use strict';

angular.module('primApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('ulke', {
                parent: 'entity',
                url: '/ulkes',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'primApp.ulke.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/ulke/ulkes.html',
                        controller: 'UlkeController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('ulke');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('ulke.detail', {
                parent: 'entity',
                url: '/ulke/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'primApp.ulke.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/ulke/ulke-detail.html',
                        controller: 'UlkeDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('ulke');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Ulke', function($stateParams, Ulke) {
                        return Ulke.get({id : $stateParams.id});
                    }]
                }
            })
            .state('ulke.new', {
                parent: 'ulke',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/ulke/ulke-dialog.html',
                        controller: 'UlkeDialogController',
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
                        $state.go('ulke', null, { reload: true });
                    }, function() {
                        $state.go('ulke');
                    })
                }]
            })
            .state('ulke.edit', {
                parent: 'ulke',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/ulke/ulke-dialog.html',
                        controller: 'UlkeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Ulke', function(Ulke) {
                                return Ulke.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('ulke', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

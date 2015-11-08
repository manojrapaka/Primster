'use strict';

angular.module('primApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('grup', {
                parent: 'entity',
                url: '/grups',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'primApp.grup.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/grup/grups.html',
                        controller: 'GrupController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('grup');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('grup.detail', {
                parent: 'entity',
                url: '/grup/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'primApp.grup.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/grup/grup-detail.html',
                        controller: 'GrupDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('grup');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Grup', function($stateParams, Grup) {
                        return Grup.get({id : $stateParams.id});
                    }]
                }
            })
            .state('grup.new', {
                parent: 'grup',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/grup/grup-dialog.html',
                        controller: 'GrupDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    adi: null,
                                    katsayi: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('grup', null, { reload: true });
                    }, function() {
                        $state.go('grup');
                    })
                }]
            })
            .state('grup.edit', {
                parent: 'grup',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/grup/grup-dialog.html',
                        controller: 'GrupDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Grup', function(Grup) {
                                return Grup.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('grup', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

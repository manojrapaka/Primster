'use strict';

angular.module('primApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('gorev', {
                parent: 'entity',
                url: '/gorevs',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'primApp.gorev.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/gorev/gorevs.html',
                        controller: 'GorevController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('gorev');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('gorev.detail', {
                parent: 'entity',
                url: '/gorev/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'primApp.gorev.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/gorev/gorev-detail.html',
                        controller: 'GorevDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('gorev');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Gorev', function($stateParams, Gorev) {
                        return Gorev.get({id : $stateParams.id});
                    }]
                }
            })
            .state('gorev.new', {
                parent: 'gorev',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/gorev/gorev-dialog.html',
                        controller: 'GorevDialogController',
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
                        $state.go('gorev', null, { reload: true });
                    }, function() {
                        $state.go('gorev');
                    })
                }]
            })
            .state('gorev.edit', {
                parent: 'gorev',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/gorev/gorev-dialog.html',
                        controller: 'GorevDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Gorev', function(Gorev) {
                                return Gorev.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('gorev', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

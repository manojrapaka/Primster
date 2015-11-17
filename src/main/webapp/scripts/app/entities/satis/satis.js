'use strict';

angular.module('primApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('satis', {
                parent: 'entity',
                url: '/satiss',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'primApp.satis.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/satis/satiss.html',
                        controller: 'SatisController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('satis');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('satis.detail', {
                parent: 'entity',
                url: '/satis/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'primApp.satis.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/satis/satis-detail.html',
                        controller: 'SatisDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('satis');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Satis', function($stateParams, Satis) {
                        return Satis.get({id : $stateParams.id});
                    }]
                }
            })
            .state('satis.new', {
                parent: 'satis',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/satis/satis-dialog.html',
                        controller: 'SatisDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    faturaNo: null,
                                    tarih: null,
                                    tutar: null,
                                    faturaTarih: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('satis', null, { reload: true });
                    }, function() {
                        $state.go('satis');
                    })
                }]
            })
            .state('satis.edit', {
                parent: 'satis',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/satis/satis-dialog.html',
                        controller: 'SatisDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Satis', function(Satis) {
                                return Satis.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('satis', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

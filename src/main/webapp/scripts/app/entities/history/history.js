'use strict';

angular.module('primApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('history', {
                parent: 'entity',
                url: '/historys',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'primApp.history.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/history/historys.html',
                        controller: 'HistoryController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('history');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('history.detail', {
                parent: 'entity',
                url: '/history/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'primApp.history.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/history/history-detail.html',
                        controller: 'HistoryDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('history');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'History', function($stateParams, History) {
                        return History.get({id : $stateParams.id});
                    }]
                }
            })
            .state('history.new', {
                parent: 'history',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/history/history-dialog.html',
                        controller: 'HistoryDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    kolonAdi: null,
                                    deger: null,
                                    tabloAdi: null,
                                    tarih: null,
                                    recId: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('history', null, { reload: true });
                    }, function() {
                        $state.go('history');
                    })
                }]
            })
            .state('history.edit', {
                parent: 'history',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/history/history-dialog.html',
                        controller: 'HistoryDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['History', function(History) {
                                return History.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('history', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

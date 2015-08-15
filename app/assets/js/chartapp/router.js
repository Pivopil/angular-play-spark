'use strict';

angular.module('chartApp').config([ '$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouterProvider) {

    $urlRouterProvider.otherwise('/');

    $stateProvider
        .state('chart', {
            url: '/',
            templateUrl: '/assets/angular/chart-template.html',
            controller: 'ChartController',
            resolve: {
                dataRange: ['ChartService', function (ChartService) {
                    return ChartService.getDataRange(null);
                }]
            },
            ncyBreadcrumb: {
                parent: 'main',
                label: 'Chart'
            }
        })
}]);
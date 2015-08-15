'use strict';

angular.module('chartApp').controller('ChartController', ['$scope', 'ChartService', '$filter', 'dataRange', 'cfg',
    function ($scope, ChartService, $filter, dataRange, cfg) {

        var chart = c3.generate({
            size: {
                height: 1024,
                width: 1480
            },
            data: {
                x: 'x',
                columns: dataRange
            },
            axis: {
                x: {
                    type: 'timeseries',
                    tick: {
                        format: '%Y-%m-%d'
                    }
                }
            }
        });

        
    }]);
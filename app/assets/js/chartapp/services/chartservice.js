'use strict';

angular.module('chartApp').service('ChartService', ['Restangular', '$q', '$filter', 'cfg', function (Restangular, $q, $filter, cfg) {

    var getDataRange = function (resolution) {
        return $q(function (resolve, reject) {
            Restangular.oneUrl('sparkRangeUrl', '/' + cfg.sparkRangeUrl + '/' + resolution).get().then(function (responce) {
                var resolved = false;

                if (!angular.isUndefined(responce)) {
                    resolved = true;
                    var rowData = Restangular.stripRestangular(responce);
                    var c3data = [];
                    c3data.push(['x'].concat(rowData['x'].reverse()));
                    for(var name in rowData['data']) {
                        c3data.push([name].concat(
                            Array.apply(null, Array(rowData['x'].length - rowData['data'][name].length)).map(Number.prototype.valueOf,0),
                            rowData['data'][name].reverse()
                        ));
                    }

                    resolve(c3data);
                }

                if (!resolved) {
                    reject();
                }
            }).catch(function () {
                reject();
            })
        });
    };

    return {
        getDataRange: function(resolution) {
            return getDataRange(resolution);
        }
    }


}]);
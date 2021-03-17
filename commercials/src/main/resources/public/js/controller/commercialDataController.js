app.controller('dataController', ['$scope', '$interval', '$state', '$http', '$stateParams', function($scope, $interval, $state, $http, $stateParams) {
    $scope.state = 'data';
    $scope.commercialId = $stateParams.commercialId;

    $http({
        "method": 'GET',
        "url": server + '/api/commercials/' + $scope.commercialId
    }).then(function(response) {
        $scope.commercial = response.data;
        $scope.commercial.dateStart = new Date($scope.commercial.dateStart);
        $scope.commercial.dateEnd = new Date($scope.commercial.dateEnd);
    }, function(response) {});

    $http({
        "method": 'GET',
        "url": server + '/api/tracking-records/' + $scope.commercialId
    }).then(function(response) {
        $scope.commercialData = response.data;
    }, function(response) {});
}]);
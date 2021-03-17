app.controller('deleteController', ['$scope', '$interval', '$state', '$http', '$stateParams', function($scope, $interval, $state, $http, $stateParams) {
    $scope.state = 'delete';
    $scope.commercialId = $stateParams.commercialId;

    $http({
        "method": 'GET',
        "url": server + '/api/commercials/' + $scope.commercialId
    }).then(function(response) {
        $scope.commercial = response.data;
        $scope.commercial.dateStart = new Date($scope.commercial.dateStart);
        $scope.commercial.dateEnd = new Date($scope.commercial.dateEnd);
    }, function(response) {});

    $scope.deleteCommercial = function() {
        $http({
            'method': 'DELETE',
            'url': server + '/api/commercials/' + $scope.commercialId
        }).then(function() {
            $state.go('list');
        }, function() {});
    }
}]);
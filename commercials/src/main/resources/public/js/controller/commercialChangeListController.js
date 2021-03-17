app.controller('changeListController', ['$scope', '$interval', '$state', '$http', '$stateParams', function($scope, $interval, $state, $http, $stateParams) {
    $scope.commercialId = $stateParams.commercialId;
    $scope.state = 'change-list';
    $http({
        "method": 'GET',
        "url": server + '/api/commercials/' + $scope.commercialId
    }).then(function(response) {
        $scope.commercial = response.data;
    }, function(response) {});

    $http({
        "method": 'GET',
        "url": server + '/api/commercials/' + $scope.commercialId + '/change-logs/'
    }).then(function(response) {
        $scope.changeLogs = response.data;
    }, function(response) {});
}]);
 app.controller('detailsController', ['$scope', '$interval', '$state', '$http', '$stateParams', function($scope, $interval, $state, $http, $stateParams) {
    $scope.commercialId = $stateParams.commercialId;
    $scope.state = 'details';
    $http({
        "method": 'GET',
        "url": server + '/api/commercials/' + $scope.commercialId
    }).then(function(response) {
        $scope.commercial = response.data;
    }, function(response) {});

    $scope.copyAndClose = function () {
        $http({
            'method': 'POST',
            'url': server + '/api/commercials/' + $scope.commercialId + '/copy'
        }).then(function (response) {
            console.log(response);
            $state.go("details", {'commercialId' : response.data.id});
        }, function (response) {
            console.log("error");
            console.log(response);
        })
    }
}]);
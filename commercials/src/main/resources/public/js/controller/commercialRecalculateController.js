app.controller('recalculateController', ['$scope', '$interval', '$state', '$http', '$stateParams', function($scope, $interval, $state, $http, $stateParams) {
    $scope.state = 'recalculate';
    $scope.commercialId = $stateParams.commercialId

    $http({
        "method": 'GET',
        "url": server + '/api/commercials/' + $scope.commercialId
    }).then(function(response) {
        $scope.commercial = response.data;
        $scope.commercial.dateStart = new Date($scope.commercial.dateStart);
        $scope.commercial.dateEnd = new Date($scope.commercial.dateEnd);
    }, function(response) {});



    $scope.reloadTasks = function() {
        $http({
            'method': 'GET',
            'url': server + '/api/commercials/' + $scope.commercialId + '/recalculation-tasks'
        }).then(function(response){
            $scope.tasks = response.data;
            for(var i = 0; i < $scope.tasks.length; i++) {
                $scope.tasks[i].createdOn = new Date($scope.tasks[i].createdOn);
                $scope.tasks[i].startedOn = new Date($scope.tasks[i].startedOn);
                $scope.tasks[i].finishedOn = new Date($scope.tasks[i].finishedOn);
            }
        }, function(response){});
    };

    $scope.reloadTasks();

    $scope.startRecalculation = function() {
        console.log("starting recalculation");
        $http({
            'method': 'POST',
            'url': server + '/api/commercials/' + $scope.commercialId + '/recalculation-tasks'
        }).then(function (response) {
            console.log(response.data);
            $scope.reloadTasks();
        }, function(response) {});

    };
}]);
app.controller('recalculateAllController', ['$scope', '$interval', '$state', '$http', '$stateParams', function($scope, $interval, $state, $http, $stateParams) {
    $scope.startGlobalRecalculation = function() {
        $http({
            'method': 'POST',
            'url': server + '/api/commercials/global-recalculation-task'
        }).then(function () {
            $scope.reloadTasks();
        });
    };

    $scope.reloadTasks = function() {
        $http({
            'method': 'GET',
            'url': server + '/api/commercials/global-recalculation-task'
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
}]);
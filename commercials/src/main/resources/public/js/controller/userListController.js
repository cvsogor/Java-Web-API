app.controller('userListController', ['$scope', '$interval', '$state', '$http', '$rootScope', function($scope, $interval, $state, $http, $rootScope) {
    $http({
        'url': server + '/api/users/',
        'method': 'GET'
    }).then(function(response){
        console.log(response.data);
        $scope.users = response.data;
    });
}]);
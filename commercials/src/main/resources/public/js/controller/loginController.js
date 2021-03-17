app.controller('loginController', ['$scope', '$interval', '$state', '$http', '$rootScope', function($scope, $interval, $state, $http, $rootScope) {
    $scope.logIn = function () {
        $http({
            'url': server + '/auth/login?username=' + $scope.username + '&password=' + $scope.password,
            'method': 'GET'
        }).then(function (response) {
            console.log(response);
            if(response.status == 200) {
                $rootScope.user = response.data;

                console.log($rootScope.user);
                $state.go('list');
            }
        }, function() {})
    };
}]);
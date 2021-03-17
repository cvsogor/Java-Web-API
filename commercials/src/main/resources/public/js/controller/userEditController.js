app.controller('userEditController', ['$scope', '$interval', '$state', '$http', '$rootScope', '$stateParams', function($scope, $interval, $state, $http, $rootScope, $stateParams) {
    $scope.userId = $stateParams.userId;
    $http({
        'url': server + '/api/users/' + $scope.userId + '/permissions',
        'method': 'GET'
    }).then(function (response) {
       $scope.editedUser = {'permissions': response.data}
    });

    $scope.submitForm = function () {
        $http({
            'url': server + '/api/users/' + $scope.userId +'/permissions',
            'method': 'PUT',
            'data': $scope.editedUser.permissions
        }).then(function (response) {
            $state.go("user-list");
        }, function (response) {
            $state.go("user-list");
        });
    }

    $scope.addPermission = function () {
        $scope.editedUser.permissions.push({
            'offerId': '*',
            'mediaSource': '*',
            'action': 'ALL'
        });
    };

    $scope.removePermission = function (permission) {
        $scope.editedUser.permissions = $scope.editedUser.permissions.filter(function(existing){
            return !(existing.offerId == permission.offerId && existing.mediaSource == permission.mediaSource && existing.action == permission.action);
        });
    };

}]);
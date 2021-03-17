app.controller('userCreateController', ['$scope', '$interval', '$state', '$http', '$rootScope', function($scope, $interval, $state, $http, $rootScope) {
    $scope.editedUser = {
        'username': '',
        'password': '',
        'permissions': [
            {
                'offerId': '*',
                'mediaSource': '*',
                'action': 'ALL'
            }
        ]
    };
    $scope.submitForm = function () {
        $http({
            'url': server + '/api/users/',
            'method': 'POST',
            'data': $scope.editedUser
        }).then(function (response) {
            $state.go("user-list");
        }, function (response) {
            $state.go("user-list");
        });
    };

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
app.controller('editController', ['$scope', '$interval', '$state', '$http', '$stateParams', function($scope, $interval, $state, $http, $stateParams) {
    $scope.commercialId = $stateParams.commercialId;
    $scope.state = 'edit';
    $scope.fieldNameList = FIELD_NAMES_LIST;

    $http({
        'method': 'GET',
        'url': server + '/api/tracking-records/media-sources'
    }).then(function(response) {
        $scope.mediaSources = response.data;
        console.log($scope.mediaSources);
    }, function(response){});

    $http({
        'method': 'GET',
        'url': server + '/api/tracking-records/offer-ids'
    }).then(function(response) {
        $scope.offerIds = response.data;
        console.log($scope.offerIds);
    }, function(response){});

    $http({
        'method': 'GET',
        'url': server + '/api/tracking-records/field-values'
    }).then(function(response) {
        console.log(response.data);
        $scope.fieldValues = response.data;
    }, function(response){});

    $http({
        "method": 'GET',
        "url": server + '/api/commercials/' + $scope.commercialId
    }).then(function(response) {
        $scope.commercial = response.data;
        $scope.commercial.dateStart = new Date($scope.commercial.dateStart);
        $scope.commercial.dateEnd = new Date($scope.commercial.dateEnd);
    }, function(response) {});

    $scope.submit = function() {
        console.log($scope.commercial);

        if($scope.commercial.amount == null || $scope.commercial.amount == 0) {
            return false;
        }

        $http({
            'method': 'PUT',
            'url': server + '/api/commercials/' + $scope.commercialId,
            'data': $scope.commercial
        }).then(function(response) {
            $state.go('details', {commercialId: $scope.commercialId});
        }, function(response){

        });
    };

    $scope.deleteCondition = function(condition) {
        $scope.commercial.conditions = $scope.commercial.conditions.filter(function (element) {
            return element.fieldName != condition.fieldName;
        });
    };

    $scope.addCondition = function() {
        $scope.commercial.conditions.push(new TargetCondition(Operator.EQUALS, 'ad_id', '', false));
    };
}]);
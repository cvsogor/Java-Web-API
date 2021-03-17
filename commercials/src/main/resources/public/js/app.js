var app = angular.module('commercialsApp', [
    'ui.router',
    'ui.bootstrap',
    'ui.grid',
    'ui.grid.pagination'
]);

var appPath = '/commercials';
var serverUrl = 'http://' + window.location.host;
var server = serverUrl + appPath;

app.config(['$stateProvider', '$urlRouterProvider', '$httpProvider', function($stateProvider, $urlRouterProvider, $httpProvider) {
    $httpProvider.interceptors.push(function($q, $rootScope) {
        return {
            'responseError': function(rejection){
                var defer = $q.defer();

                if(rejection.status == 401){
                    window.location = '#/login';
                    $rootScope.errorMessage = "Please LogIn";
                    return;
                }

                if(rejection.status == 400){
                    $rootScope.errorMessage = "Error occured: " + rejection.data;
                }

                if(rejection.status == 500){
                    $rootScope.errorMessage = "Internal server error, please contact developers:  " + rejection.data;
                }

                defer.reject(rejection);

                return defer.promise;
            }
        };
    });

    $urlRouterProvider.otherwise('list');

    $stateProvider.state('login', {
        url: '/login',
        templateUrl: appPath + '/template/login.html',
        controller: 'loginController'
    });

    $stateProvider.state('logout', {
        url: '/logout',
        controller: ['$state', '$http', '$rootScope', function($state, $http, $rootScope){
            $http({
                'url': server + '/auth/logout',
                'method': 'GET'
            }).then(function(response) {
                if(response.status == 200) {
                    $rootScope.user = null;
                    $state.go('login');
                } else {
                    console.log(response);
                }
            });
        }]
    });

    $stateProvider.state('profile',{
        url: '/profile',
        templateUrl: appPath + '/template/profile.html'
    });

    $stateProvider.state('list', {
        url: '/list',
        templateUrl: appPath + '/template/commercial_list.html',
        controller: 'listController'
    });

    $stateProvider.state('create', {
        url: '/create',
        templateUrl: appPath + '/template/commercial_create.html',
        controller: 'createController'
    });

    $stateProvider.state('details', {
        url: '/details/:commercialId',
        templateUrl: appPath + '/template/commercial_details.html',
        controller: 'detailsController'
    });

    $stateProvider.state('edit', {
        url: '/edit/:commercialId',
        templateUrl: appPath + '/template/commercial_edit.html',
        controller: 'editController'
    });

    $stateProvider.state('data', {
        url: '/data/:commercialId',
        templateUrl: appPath + '/template/commercial_data.html',
        controller: 'dataController'
    });

    $stateProvider.state('recalculate', {
        url: '/recalculate/:commercialId',
        templateUrl: appPath + '/template/commercial_recalculate.html',
        controller: 'recalculateController'
    });

    $stateProvider.state('change-list', {
        url: '/change-list/:commercialId',
        templateUrl: appPath + '/template/commercial_changelist.html',
        controller: 'changeListController'
    });

    $stateProvider.state('delete', {
        url: '/delete/:commercialId',
        templateUrl: appPath + '/template/commercial_delete.html',
        controller: 'deleteController'
    });

    $stateProvider.state('recalculate-all', {
        url: '/recalculate-all',
        templateUrl: appPath + '/template/commercial_recalculate_all.html',
        controller: 'recalculateAllController'
    });

    $stateProvider.state('user-list', {
        url: '/user-list',
        templateUrl: appPath + '/template/user_list.html',
        controller: 'userListController'
    });

    $stateProvider.state('user-create', {
        url: '/user-create',
        templateUrl: appPath + '/template/user_create.html',
        controller: 'userCreateController'
    });

    $stateProvider.state('user-edit', {
        url: '/user/:userId',
        templateUrl: appPath + '/template/user_edit.html',
        controller: 'userEditController'
    });
    
}]).run(function($http, $rootScope){
    $http({
        'method' : 'GET',
        'url': server + '/api/users/current'
    }).then(function(response){
        $rootScope.user = response.data;
    });

    $rootScope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams){
        $rootScope.errorMessage = null;
    });
});




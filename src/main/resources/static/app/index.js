(() => {
    'use strict';

    function config($routeProvider) {
        $routeProvider
            .when('/', {
                template: '<app-welcome></app-welcome>',
            })
            .when('/store', {
                template: '<app-store></app-store>',
            })
            .when('/store/:id/comments', {
                template: '<app-book-comments></app-book-comments>',
            })
            .when('/store/:id', {
                template: '<app-book-edit></app-book-edit>',
            })
            .when('/store/create', {
                template: '<app-book-create></app-book-create>',
            })
            .otherwise({
                redirectTo: '/',
            });
    }

    config.$inject = ['$routeProvider'];

    function run() {
        // tbd...
    }

    angular
        .module('market', [
            // external packages
            'ngRoute',
            'ngStorage',
            // internal packages
            'market.welcome',
            'market.store',
            'market.books',
        ])
        .config(config)
        .run(run);

    // function IndexController($rootScope, $scope, $http, $location, $localStorage) {
    //     $scope.tryToAuth = function () {
    //         $http.post('http://localhost:5555/auth/auth', $scope.user)
    //             .then(function successCallback(response) {
    //                 if (response.data.token) {
    //                     $http.defaults.headers.common.Authorization = 'Bearer ' + response.data.token;
    //
    //                     $localStorage.winterMarketUser = {
    //                         username: $scope.user.username,
    //                         token: response.data.token,
    //                     };
    //
    //                     $location.path('/');
    //                 }
    //             }, function errorCallback(response) {
    //             });
    //     };
    //
    //     $rootScope.isUserLoggedIn = () => {
    //         return !!$localStorage.winterMarketUser;
    //     };
    // }
    //
    // angular
    //     .module('market')
    //     .controller('indexController', IndexController);
})();
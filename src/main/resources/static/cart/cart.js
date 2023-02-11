angular.module('market').controller('cartController', function ($rootScope, $http, $scope, $location, $localStorage) {
    const contextPath = 'http://localhost:8189/';
    const coreContextPath = 'http://localhost:8080/';

    $rootScope.loadCart = function () {
        $http.get(contextPath + 'api/v1/book/'+ $localStorage.productHtml + '/comment').then(function (response) {
            $rootScope.cart = response.data;
        });
    }

    $rootScope.loadCart();

});
(() => {
    'use strict';

    function BookCreateController(ApiService, $rootScope, $http, $scope, $location, $localStorage) {
        const $ctrl = this;

        const contextPath = 'http://localhost:8189/';
        const coreContextPath = 'http://localhost:8080/';

        $rootScope.create = function () {
            $http.get(contextPath + 'api/v1/book/' + $localStorage.productHtml + '/comment').then(function (response) {
                $rootScope.cart = response.data;
            });
        };

        ApiService.loadCart();
    }

    angular
        .module('market.books')
        .component('appBookCreate', {
            templateUrl: 'app/features/books/components/book-create/book-create.component.html',
            controller: BookCreateController,
        });
})();
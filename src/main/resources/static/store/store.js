angular.module('market').controller('storeController', function ($rootScope, $scope, $http, $location, $localStorage) {
    const contextPath = 'http://localhost:8189/';
    const cartContextPath = 'http://localhost:8080/cart/';

    $scope.loadProducts = function () {
        $http.get(contextPath + 'api/v1/book').then(function (response) {
            $scope.bookList = response.data;
        });
    }

    $scope.updateBook = function (id) {
        $localStorage.productHtml = id;
        $rootScope.updateBook(id);
        $location.url('/update');
    }


    $scope.deleteBook = function (id) {
        $http.delete(contextPath + 'api/v1/book/' + id).then(function (response) {
            $scope.loadProducts();
        });
    }
    $scope.loadProducts();


    $scope.addToCart = function (id) {
        $localStorage.productHtml = id;
        $rootScope.loadCart(id);
        $location.url('/cart');
    }


});
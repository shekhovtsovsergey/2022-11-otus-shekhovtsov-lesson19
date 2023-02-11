angular.module('app', []).controller('indexController', function ($scope, $http) {

    $scope.loadBooks = function () {
        $http.get('http://localhost:8080/api/v1/book/').then(function (response) {
            $scope.bookList = response.data;
        });
    }

    $scope.getBookInfoById = function (id) {
        $http.get('http://localhost:8080/api/v1/book/'+id+'info').then(function (response) {
        });
    }

    $scope.deleteProductById = function (productId) {
        $http.delete('http://localhost:8080/api/v1/products/' + productId).then(function (response) {
            $scope.loadProducts();
        });
    }

    $scope.loadBooks();

});
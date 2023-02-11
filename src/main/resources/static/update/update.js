angular.module('market').controller('updateController', function ($rootScope, $http, $scope, $location, $localStorage) {
    const contextPath = 'http://localhost:8189/';
    const coreContextPath = 'http://localhost:8080/';

    $rootScope.updateBook = function () {
        $http.get(contextPath + 'api/v1/book/'+ $localStorage.productHtml).then(function (response) {
            $rootScope.updateForm = response.data;
        });
    }

    $rootScope.loadCart();

});
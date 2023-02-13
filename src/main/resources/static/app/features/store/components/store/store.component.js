(() => {
    'use strict';

    function StoreController($rootScope, ApiService, $location, $localStorage) {
        console.log('ApiService', ApiService);

        const $ctrl = this;

        $ctrl.bookList = [];

        $ctrl.loadBooks = () => {
            ApiService.loadBooks().then((bookList) => {
                $ctrl.bookList = bookList;
            });
        };

        $ctrl.deleteBook = (id) => {
            ApiService.deleteBook(id).then(() => {
                $ctrl.loadBooks();
            });
        };

        $ctrl.$onInit = () => {
            $ctrl.loadBooks();
        };
    }

    StoreController.$inject = ['$rootScope', 'ApiService', '$location', '$localStorage'];

    angular
        .module('market.store')
        .component('appStore', {
            templateUrl: 'app/features/store/components/store/store.component.html',
            controller: StoreController,
        });
})();
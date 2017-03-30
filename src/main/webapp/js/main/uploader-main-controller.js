/**
 */
(function () {
    'use strict';

    angular
        .module('starter')
        .controller('UploaderMainController', UploaderMainController);

    UploaderMainController.$inject = ['$http', '$scope', 'commonUtil', 'logger', 'qnUtil'];
    function UploaderMainController($http, $scope, commonUtil, logger, qnUtil) {
        var vm = this;
        vm.accessKey='NE2fKwHVh2SxP9L21Q1Xu7GTUn9nIO3d0CT1R9tW';
        vm.secretKey='sNw4N0njajw6EYEygDZ1LIqE3BxpJ917jVStChP5';
        vm.bucketName='tc-image2';
        vm.domainUrl='http://tc-image2.mobisoftwarestudio.com';
        vm.directory='test';
    }
})();


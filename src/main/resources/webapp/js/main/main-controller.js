/**
 */
(function () {
    'use strict';

    angular
        .module('starter')
        .controller('MainController', MainController);

    MainController.$inject = ['$http', '$scope', 'commonUtil', 'logger', 'qnUtil'];
    function MainController($http, $scope, commonUtil, logger, qnUtil) {
        var vm = this;
        vm.localUrl="/mfefilemgr/file/qiniu/zone0/tc-image2/";
        vm.component="";
        vm.requestBody="";
        vm.result="";
        vm.onJsoneditorLoad = function (instance) {
            //instance.expandAll();
        };
        vm.requestBodyOptions={mode: 'code'};
        vm.resultOptions={mode: 'code'};

        vm.requestBodyModeSwith=function(){
            vm.requestBodyOptions.mode=vm.requestBodyOptions.mode=='code' ? 'tree' : 'code';
        };

        vm.resultModeSwith=function(){
            vm.resultOptions.mode=vm.resultOptions.mode=='code' ? 'tree' : 'code';
        };

        function request(_method, _data){
            $http({
                method: _method,
                url: vm.localUrl+encodeURIComponent(vm.component),
                data: _data,
                headers: {
                    "Content-Type": "application/json;charset=utf-8"
                }
            })
                .then(function(rsp){
                    //var rlt=JSON.stringify(rsp, null, 2);
                    //logger.debug("rsp:"+rlt);
                    vm.result=rsp.data;
                })
                .catch(function(rsp){
                    //var rlt=JSON.stringify(rsp, null, 2);
                    //logger.error("rsp:"+rlt);
                    vm.result=rsp;
                });
        }

        vm.get=function(){
            request('get', '');
        };
        vm.delete=function(){
            request('delete', vm.requestBody);
        }
    }
})();


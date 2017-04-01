// Ionic Starter App

// angular.module is a global place for creating, registering and retrieving Angular modules
// 'starter' is the name of this angular module example (also set in a <body> attribute in index.html)
// the 2nd parameter is an array of 'requires'
// 'starter.controllers' is found in controllers.js
angular.module('starter', ['ng.jsoneditor', 'ui.bootstrap', 'zeroclipboard'])

    .config(function ($httpProvider) {
        $httpProvider.defaults.headers.common['Access-Control-Allow-Origin'] = '*';

    })
    .config(['uiZeroclipConfigProvider', function(uiZeroclipConfigProvider) {

    // config ZeroClipboard
    uiZeroclipConfigProvider.setZcConf({
        swfPath: 'bower_components/zeroclipboard/dist/ZeroClipboard.swf'
    });

}]);
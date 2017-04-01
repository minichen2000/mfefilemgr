(function () {
    'use strict';

    angular
        .module('starter')
        .factory('logger', logger);

    logger.$inject = ['$log'];

    /**
     * @namespace Logger
     * @desc Application wide logger
     * @memberOf Factories
     */
    function logger($log) {
        var service = {
            error: error,
            log: log,
            debug: debug
        };
        return service;

        ////////////

        /**
         * @name error
         * @desc Logs errors
         * @param {String} msg Message to log
         * @returns {String}
         * @memberOf Factories.Logger
         */
        function error(msg) {
            var loggedMsg = 'Error: ' + msg;
            $log.error(loggedMsg);
            return loggedMsg;
        };

        /**
         * @name log
         * @desc Logs logs
         * @param {String} msg Message to log
         * @returns {String}
         * @memberOf Factories.Logger
         */
        function log(msg) {
            var loggedMsg = 'Log: ' + msg;
            $log.log(loggedMsg);
            return loggedMsg;
        };

        function debug(msg) {
          var loggedMsg = 'Debug: ' + msg;
          $log.debug(loggedMsg);
          return loggedMsg;
        };
    }
})();

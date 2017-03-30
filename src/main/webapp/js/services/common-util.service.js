(function () {
  'use strict';

  angular
    .module('starter')
    .factory('commonUtil', commonUtil);

  commonUtil.$inject = ['logger'];


  function commonUtil(logger) {

      var service = {
          formatNowDateTime: formatNowDateTime,
          formatDateTime: formatDateTime,
          formatDate: formatDate,
          genFileSizeString: genFileSizeString
      };
    return service;

      function formatDateTime(date) {
          var year = date.getFullYear();
          var month = date.getMonth() + 1;
          var day = date.getDate();
          var hours = date.getHours();
          var minutes = date.getMinutes();
          var seconds = date.getSeconds();
          var milliseconds = date.getMilliseconds();
          return ''+year+'/'+month+'/'+day+' '+hours+':'+minutes+':'+seconds+':'+milliseconds;
      }
      function formatDate(date) {
          var year = date.getFullYear();
          var month = date.getMonth() + 1;
          var day = date.getDate();
          return ''+year+'/'+month+'/'+day;
      }
      function formatNowDateTime() {
          var date=new Date();
          return formatDateTime(date);
      }
      function genFileSizeString(size){
          if(1024>size){
              return size+'B';
          }else if(1024*1024>size){
              return Math.round(size/1024)+'K';
          }else if(1024*1024*1024>size){
              return Math.round(size/(1024*1024))+'M';
          }else{
              return Math.round(size/(1024*1024*1024))+'G';
          }
      }
  }


})();

(function () {
    'use strict';

    angular
        .module('starter')
        .directive('qnUploadFileStatus', qnUploadFileStatus);
    qnUploadFileStatus.$inject = ['commonUtil', 'logger', 'qnUtil'];
    function qnUploadFileStatus(commonUtil, logger, qnUtil) {
        return {
            // can be used as attribute or element
            restrict: 'AE',
            scope: {
                fileEntity: '='
            },
            // which markup this directive generates
            templateUrl: 'js/directives/qn-upload-file-status.html',
            replace: true,
            link: function (scope, iElement, iAttrs) {

                scope.genSizeStr=function(size){
                    return commonUtil.genFileSizeString(size);
                };
                scope.getPercentStyle=function(){
                  return {
                      'background-color': 'done'==scope.fileEntity.status ? 'lightgreen' : 'transparent'
                  }
                };
                scope.getBig=function(){
                    return qnUtil.encodeQnDirectLink(scope.fileEntity.domainUrl, scope.fileEntity.directory, scope.fileEntity.fname);
                };
                scope.directLink=scope.getBig();

                scope.getThumb=function(){
                    var imgsrc=scope.getBig()+'?'+Qiniu.imageView2({
                            mode: 1,  // 缩略模式，共6种[0-5]
                            w: 100,   // 具体含义由缩略模式决定
                            h: 100,   // 具体含义由缩略模式决定
                            q: 70,   // 新图的图像质量，取值范围：1-100
                            format: 'png'  // 新图的输出格式，取值范围：jpg，gif，png，webp等
                        });
                    //logger.debug("imgsrc:"+imgsrc);
                    return imgsrc;
                };
            }
        };
    }
})();

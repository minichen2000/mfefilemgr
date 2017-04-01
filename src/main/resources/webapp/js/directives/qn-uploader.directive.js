(function () {
    'use strict';

    angular
        .module('starter')
        .directive('qnUploader', qnUploader);
    qnUploader.$inject = ['commonUtil', 'logger', 'qnUtil'];
    function qnUploader(commonUtil, logger, qnUtil) {
        return {
            // can be used as attribute or element
            restrict: 'AE',
            scope: {
                accessKey: '=?',
                secretKey: '=?',
                bucketName: '=?',
                domainUrl: '=?',
                directory: '=?'
            },
            // which markup this directive generates
            templateUrl: 'js/directives/qn-uploader.html',
            replace: true,
            link: function (scope, iElement, iAttrs) {
                function genKey(file){
                    return scope.directory ? scope.directory+'/'+file.name : file.name;
                }
                scope.onClear=function(){
                    scope.fileEntities=[];
                };

                scope.fileEntities=[];
                var Qiniu = new QiniuJsSDK();
                var uploader=Qiniu.uploader(genOption());
                function genOption(){
                    var option=qnUtil.getUploadOption('pickfiles', 'container', 'container', scope.domainUrl);
                    option.url='http://up-z2.qiniu.com';
                    option.uptoken_func=function(file){
                        return qnUtil.genUpToken(scope.accessKey, scope.secretKey, scope.bucketName);
                    };
                    option.init.FilesAdded=function(up, files) {
                        //logger.debug('FilesAdded: files:\n'+JSON.stringify(files, null,2));
                        plupload.each(files, function (file) {
                            // 文件添加进队列后，处理相关的事情
                            scope.fileEntities.push({
                                id: file.id,
                                fname: file.name,
                                directory: scope.directory,
                                key: genKey(file),
                                domainUrl: scope.domainUrl,
                                fsize: file.size,
                                percent: 0,
                                loaded: 0,
                                speed: 0,
                                bucketName: scope.bucketName,
                                error: '',
                                status:'added'
                            })
                        });
                        scope.$apply();
                        //logger.debug('FilesAdded: fileEntities:\n'+JSON.stringify(scope.fileEntities, null,2));
                        //uploader=Qiniu.uploader(genOption());
                        uploader.start();
                    };
                    option.init.UploadProgress=function(up, file) {
                        //logger.debug('UploadProgress: file:\n'+JSON.stringify(file, null,2));
                        var theEntity=scope.fileEntities.find(function(entity){return entity.id==file.id});
                        if(theEntity){
                            //logger.debug('UploadProgress: matched:'+theEntity.fname);
                            theEntity.percent=file.percent;
                            theEntity.loaded=file.loaded;
                            theEntity.speed=file.speed;
                            theEntity.status='inProgress';
                            scope.$apply();
                        }
                    };
                    option.init.FileUploaded=function(up, file, info) {
                        //logger.debug('FileUploaded: file:\n'+JSON.stringify(file, null,2));
                        //logger.debug('FileUploaded: info:\n'+JSON.stringify(info, null,2));
                        var theEntity=scope.fileEntities.find(function(entity){return entity.id==file.id});
                        if(theEntity){
                            theEntity.percent=file.percent;
                            theEntity.loaded=file.loaded;
                            theEntity.speed=file.speed;
                            theEntity.status='done';
                            theEntity.error='';
                            scope.$apply();
                        }
                    };
                    option.init.Error=function(up, err, errTip) {
                        logger.error('Error: err:\n'+JSON.stringify(err, null,2));
                        logger.error('Error: errTip:\n'+JSON.stringify(errTip, null,2));
                        var theEntity=scope.fileEntities.find(function(entity){return entity.id==err.file.id});
                        if(theEntity){
                            theEntity.error=errTip;
                            theEntity.status='error';
                            theEntity.percent=0;
                            theEntity.loaded=0;
                            theEntity.speed=0;
                            scope.$apply();
                        }
                    };
                    option.init.Key=function(up, file) {
                        return genKey(file);
                    };
                    return option;
                }


            }
        };
    }
})();

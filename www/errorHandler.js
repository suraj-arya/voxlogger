/**
* Created by surya on 12/24/14.
* overrides default javascript error handler
* passes the error object to native hander
*/

var consoleVar = require("./console.js");
console.log("consoleVar is ", consoleVar);

document.addEventListener("deviceready", onDeviceReady, false);

var checkPoints = ['home', 'survey'];

function onDeviceReady() {
    console.log("cordova is loaded now.");
    consoleVar.setCheckPoints(checkPoints);
    window.console = consoleVar;
}
console.log("overriding console.");
if(typeof angular != 'undefined') {
    console.log("angular defined.");
    angular.module('voxErrorHandler', [])
        .config(['$provide', function($provide){
            console.log("module defined: VoxErrorHandler");
            $provide.decorator("$exceptionHandler", ['$delegate',
                function($delegate) {
                    return function(exception, cause) {
                        $delegate(exception, cause);
                        console.error(exception);
                    };
                }]);
            }
        ])
        .run(['$rootScope', '$window',
            function($rootScope, $window, constant, asyncutils, voxsession){
                console.log("inside voxErrorHandler run block");
                $rootScope.initialize = function(){};
                $window.onerror = function (message, file, line, column, errorObj) {
                    console.log("inside voxErrorHandler run block $window.onError");
                    if(!errorObj){
                        if(!!file && !!line){
                            message= String.format("Message: {0}, " +
                                "File:{1}, Line: {2}", message, file, line);
                            errorObj = new Error(message);
                        }
                    }
                    console.error(errorObj);
                    return true;
                };
                //$window.console = consoleVar;
        }]);
} else {
    console.log("angular not available");
    window.onerror = function (message, file, line, column, errorObj) {
        console.log("overriding console.");
        if(!errorObj){
            if(!!file && !!line)
                message= String.format("Message: {0}, " +
                    "File:{1}, Line: {2}", message, file, line);
                errorObj = new Error(message);
            }
            consoleVar.error(errorObj);
            return true;
    };
}
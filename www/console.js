/**
* Created by surya on 12/24/14.
* defines a console class to wrap
* console object defined natively
*/

function Console() {
    /**
    * Wrapper class for console
    */
    var baseConsole = window.console; //handle for native console
    var service = "JavaScriptConsoleLogger";
    var action = {
            log: 'log',
            debug: 'debug',
            info: 'info',
            error: 'error',
            checkPoint: 'setCheckPoint',
            next: 'nextCheckPoint'
        };

    this.getBaseConsole = function () {
        return baseConsole;
    };

    this.setCheckPoints = function (checkPointArray) {
        /**
        * set checkpoint for the logger
        */
        var args = [{checkpoint: checkPointArray}];
        this.callNative(service, action.checkPoint, args);
    };

    this.nextCheckPoint = function () {
        /**
        * moves the checkPoint to next from current one
        */
        var args = [{}];
        this.callNative(service, action.next, args);
    }

    this.log = function () {
        /**
        * overrides console.log
        */
        var args = [].slice.apply(arguments);
        var data = formatString(args);
        var message = [{message: data}];
        this.callNative(service, action.log, message);
        baseConsole.log.apply(baseConsole, args);
    };

    this.debug = function () {
        /**
        * overrides console.debug
        */
        var args = [].slice.apply(arguments);
        var data = formatString(args);
        var message = [{message: data}];
        this.callNative(service, action.debug, message);
        baseConsole.debug.apply(baseConsole, args);
    };

    this.error = function () {
        /**
        * overrides console.error
        */
        var args = [].slice.apply(arguments);
        baseConsole.log("error caught in baseConsole");
        var data = formatString(args);
        var message = [{message: data}];
        this.callNative(service, action.error, message);
        baseConsole.log.apply(baseConsole, args);
    };

    this.info = function () {
        /**
        * overrides console.info
        */
        var args = [].slice.apply(arguments);
        var data = formatString(args);
        var message = [{message: data}];
        this.callNative(service, action.info, message);
        baseConsole.info.apply(baseConsole, args);
    };

    /**
    * rest of the console api functions unaltered
    */
    this.assert = function () {
        baseConsole.assert.apply(baseConsole, [].slice.apply(arguments));
    };

    this.clear = function () {
        baseConsole.clear.apply(baseConsole, [].slice.apply(arguments));
    }

    this.count = function () {
        baseConsole.count.apply(baseConsole, [].slice.apply(arguments));
    };

    this.dir = function () {
        baseConsole.dir.apply(baseConsole, [].slice.apply(arguments));
    }

    this.dirxml = function () {
        baseConsole.dirxml.apply(baseConsole, [].slice.apply(arguments));
    };

    this.group = function () {
        baseConsole.group.apply(baseConsole, [].slice.apply(arguments));
    };

    this.groupCollapsed = function () {
        baseConsole.groupCollapsed.apply(baseConsole, [].slice.apply(arguments));
    };

    this.groupEnd = function () {
        baseConsole.groupEnd.apply(baseConsole, [].slice.apply(arguments));
    };

    this.profile = function () {
        baseConsole.profile.apply(baseConsole, [].slice.apply(arguments));
    };

    this.profileEnd = function () {
        baseConsole.profileEnd.apply(baseConsole, [].slice.apply(arguments));
    };

    this.time = function () {
        baseConsole.time.apply(baseConsole, [].slice.apply(arguments));
    };

    this.timeEnd = function () {
        baseConsole.timeEnd.apply(baseConsole, [].slice.apply(arguments));
    };

    this.timeline = function () {
        baseConsole.timeline.apply(baseConsole, [].slice.apply(arguments));
    };

    this.timelineEnd = function () {
        baseConsole.timelineEnd.apply(baseConsole, [].slice.apply(arguments));
    };

    this.timeStamp = function () {
        baseConsole.timeStamp.apply(baseConsole, [].slice.apply(arguments));
    };

    this.trace = function () {
        baseConsole.trace.apply(baseConsole, [].slice.apply(arguments));
    };

    this.warn = function () {
        baseConsole.warn.apply(baseConsole, [].slice.apply(arguments));
    };

    this.callNative = function (service, action, arguments) {
        var successCallback = function (winParams) {
        };
        var errorCallback = function (err) {
        };
        cordova.exec(successCallback, errorCallback,
                     service, action, arguments);
    };

    function formatString(args) {
        if (args.length < 2) {
            return;
        }
        var data = args[0];
        for (var k=1; k < args.length; ++k) {
            switch (typeof args[k])
            {
                case 'string':
                    //data = data.replace( /%s/, args[k] );
                break;
                case 'number':
                    //data = data.replace( /%d/, args[k] );
                break;
                case 'boolean':
                    //data = data.replace( /%b/, args[k] ? 'true' : 'false' );
                break;
                default:
                    //function | object | undefined
                break;
            }
        }
        return data;
    }
}

module.exports = new Console();

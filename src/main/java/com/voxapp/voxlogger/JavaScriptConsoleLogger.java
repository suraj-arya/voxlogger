package com.voxapp.voxlogger;

import com.voxapp.voxlogger.logback.CheckPointBasedRollingPolicy;
import com.voxapp.voxlogger.logback.CheckPointBasedTriggeringPolicy;
import com.voxapp.voxlogger.exceptions.NotInitializedException;

import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;


/**
 * Created by surya on 12/26/14.
 * This is the main logger class
 * This class provides the definition to the abstract methods
 * in BaseVoxappLogger
 *
 */
public class JavaScriptConsoleLogger extends BaseVoxappLogger {
    private boolean initialized = false;
    private String baseLogfileName = "voxapp.log";
    private Logger fileLogger;
    private CheckPointBasedTriggeringPolicy<ILoggingEvent> triggeringPolicy;

    @Override
    public void log(JSONObject params, CallbackContext cbContext){
        info(params, cbContext);
    }

    @Override
    public void info(JSONObject params, CallbackContext cbContext){
        if (!initialized) {
            try {
                checkAndInitializeLogger();
            }catch (NotInitializedException e) {
                cbContext.error(e.getMessage());
            }
        }
        try{
            String message = params.getString(Constants.JSONKeys.KEY_MESSAGE);
            fileLogger.info(message);
        }catch(JSONException ex){
            fileLogger.error(ex.getMessage());
        }
    }

    @Override
    public void error(JSONObject params, CallbackContext cbContext){
        if (!initialized) {
            try {
                checkAndInitializeLogger();
            }catch (NotInitializedException e) {
                cbContext.error(e.getMessage());
            }
        }
        try{
            String message = params.getString(Constants.JSONKeys.KEY_MESSAGE);
            fileLogger.error(message);
            //reportError();
        }catch(JSONException ex){
            fileLogger.error(ex.getMessage());
        }
    }

    @Override
    public void debug(JSONObject params, CallbackContext cbContext){
        if (!initialized) {
            try {
                checkAndInitializeLogger();
            }catch (NotInitializedException e) {
                cbContext.error(e.getMessage());
            }
        }
        try{
            String message = params.getString(Constants.JSONKeys.KEY_MESSAGE);
            fileLogger.debug(message);
        }catch(JSONException ex){
            fileLogger.error(ex.getMessage());
        }
    }

    @Override
    public void setCheckPoint(JSONObject params, CallbackContext cbContext){
        /**
         * the json would look something like this
         * {
         *     ...
         *     checkPoints : ["checkPoint1", "checkPoint2", "checkPoint3"]
         * }
         */
        try{
            JSONArray checkPointsArray = params.getJSONArray(Constants.JSONKeys.KEY_CHECKPOINTS);
            for (int i=0; i < checkPointsArray.length(); i++){
                String checkPoint = checkPointsArray.getString(i);
                this.checkPoint.add(checkPoint);
            }
        }catch(JSONException ex){
            fileLogger.error(ex.getMessage());
        }
    }

    @Override
    public void nextCheckPoint(JSONObject params, CallbackContext cbContext){
        triggeringPolicy.triggerNextCheckPoint();
    }

    private void checkAndInitializeLogger() throws NotInitializedException{
        if (null == checkPoint) {
            throw new NotInitializedException("checkPoint is not initialized");
        }
        fileLogger = createLogger(JavaScriptConsoleLogger.class.getCanonicalName());
    }

    private Logger createLogger(String loggerName){
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        lc.reset();

        PatternLayoutEncoder ple = new PatternLayoutEncoder();
        ple.setPattern("%date %level %logger{10} %msg%n");
        ple.setContext(lc);
        ple.start();

        RollingFileAppender<ILoggingEvent> fileAppender = new RollingFileAppender<ILoggingEvent>();
        fileAppender.setFile(baseLogfileName);
        fileAppender.setEncoder(ple);
        fileAppender.setContext(lc);

        CheckPointBasedRollingPolicy rollingPolicy = new CheckPointBasedRollingPolicy();
        rollingPolicy.setContext(lc);
        rollingPolicy.setCheckPoint(checkPoint);
        rollingPolicy.setcompressionMode(Constants.CompressionModes.NO_COMPRESSION);
        rollingPolicy.setParent(fileAppender);

        fileAppender.setRollingPolicy(rollingPolicy);

        triggeringPolicy = new CheckPointBasedTriggeringPolicy<ILoggingEvent>();
        fileAppender.setTriggeringPolicy(triggeringPolicy);


        fileAppender.start();

        Logger logger = (Logger) LoggerFactory.getLogger(loggerName);
        logger.addAppender(fileAppender);
        logger.setLevel(Level.INFO);
        logger.setAdditive(false); /* set to true if root should log too */

        initialized = true;
        return logger;
    }
}

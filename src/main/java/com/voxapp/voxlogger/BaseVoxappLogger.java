package com.voxapp.voxlogger;

import android.support.v4.app.FragmentActivity;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;

/**
 * Created by surya on 12/24/14.
 * this is native code for the logger plugin
 * this class defines handler for the logger functions
 */
public abstract class BaseVoxappLogger extends CordovaPlugin {
    private static final String TAG = BaseVoxappLogger.class.getCanonicalName();

    //checkPoints which will be used for rolling the files
    protected CheckPoint checkPoint;

    protected FragmentActivity cordovaActivity;

    //handles console.log
    public abstract  void log(JSONObject params, CallbackContext cbContext);

    //handles console.info
    public abstract  void info(JSONObject params, CallbackContext cbContext);

    //handles console.error
    public abstract  void error(JSONObject params, CallbackContext cbContext);

    //handles console.debug
    public abstract  void debug(JSONObject params, CallbackContext cbContext);

    public abstract void setCheckPoint(JSONObject params, CallbackContext cbContext);

    public abstract void nextCheckPoint(JSONObject params, CallbackContext cbContext);

    @Override
    public boolean execute(final String action, final JSONArray args,
                           final CallbackContext callbackContext)
                            throws JSONException{
        cordovaActivity = (FragmentActivity) cordova.getActivity();

        cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final Method method = getMethod(action);
                    final JSONObject params = args.length() > 0 ?
                            args.getJSONObject(0) : null;

                    execute(method, params, callbackContext);
                } catch (NoSuchMethodException e) {
                    String error = "Unable to find action " + action + " in plugin: " +
                            BaseVoxappLogger.this.getClass().getName();
                    Log.e(TAG, error, e);
                    callbackContext.error(Constants.UNKNOWN_ERROR);

                } catch (JSONException e) {
                    String error = "Unable to parse incoming argumaents";
                    Log.e(TAG, error, e);
                    callbackContext.error(Constants.UNKNOWN_ERROR);
                }
            }
        });

        return true;
    }

    protected void execute(final Method method, final JSONObject params,
                           final CallbackContext callbackContext){
        try {
            method.invoke(BaseVoxappLogger.this, params,
                    callbackContext);
        }catch (Exception e){
            String err = "Error while exceuting plugin: "
                    + BaseVoxappLogger.this.getClass().getName()
                    + " :" + method.getName();

            Log.e(TAG, e.getMessage() + "  " + err, e);
            callbackContext.error(err);
        }

    }

    protected Method getMethod(String methodName) throws NoSuchMethodException {
        return this.getClass().getMethod(methodName, JSONObject.class,
                CallbackContext.class);
    }
}

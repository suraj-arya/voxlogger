package com.voxapp.voxlogger.logback;

import java.io.File;

import ch.qos.logback.core.rolling.TriggeringPolicyBase;
import ch.qos.logback.core.util.InvocationGate;

/**
 * Created by surya on 1/5/15.
 * this class provides a custom triggering policy
 * for rollingFileAppender to use
 */
public class CheckPointBasedTriggeringPolicy<E> extends TriggeringPolicyBase<E> {
    private boolean checkPointTrigger = false;
    private InvocationGate invocationGate = new InvocationGate();
    public CheckPointBasedTriggeringPolicy(){
        // create a default TriggeringPolicy
    }

    @Override
    public boolean isTriggeringEvent(final File activeFile, final E event){
        boolean res = checkPointTrigger;
        checkPointTrigger = false;
        if (invocationGate.skipFurtherWork()) {
            return false;
        }
        return  res;
    }

    public void triggerNextCheckPoint(){
        checkPointTrigger = true;
    }
}

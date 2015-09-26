package com.voxapp.voxlogger.logback;

import com.voxapp.voxlogger.CheckPoint;

import java.io.File;

import ch.qos.logback.core.rolling.RollingPolicyBase;
import ch.qos.logback.core.rolling.RolloverFailure;
import ch.qos.logback.core.rolling.helper.CompressionMode;
import ch.qos.logback.core.rolling.helper.Compressor;
import ch.qos.logback.core.rolling.helper.RenameUtil;

/**
 * Created by surya on 1/5/15.
 * this class creates a custom rolling policy
 * to be used with RollingFileAppender
 */
public class CheckPointBasedRollingPolicy extends RollingPolicyBase {
    static final String PRUDENT_MODE_UNSUPPORTED = "See also http://logback.qos.ch/codes.html#tbr_fnp_prudent_unsupported";
    static final String SEE_PARENT_FN_NOT_SET = "Please refer to http://logback.qos.ch/codes.html#fwrp_parentFileName_not_set";
    private String currentFileName;
    private CheckPoint checkPoint;
    RenameUtil util = new RenameUtil();
    Compressor compressor;
    public static final String ZIP_ENTRY_DATE_PATTERN = "yyyy-MM-dd_HHmm";

    @Override
    public void start(){
        util.setContext(this.context);

        if(isParentPrudent()) {
            addError("Prudent mode is not supported with FixedWindowRollingPolicy.");
            addError(PRUDENT_MODE_UNSUPPORTED);
            throw new IllegalStateException("Prudent mode is not supported.");
        }

        if (getParentsRawFileProperty() == null) {
            addError("The File name property must be set before using this rolling policy.");
            addError(SEE_PARENT_FN_NOT_SET);
            throw new IllegalStateException("The File must be set.");
        }
        compressor = new Compressor(compressionMode);
        compressor.setContext(this.context);
        super.start();
    }

    @Override
    public void rollover() throws RolloverFailure {
        String nextFileName = nextFile();
        if (null != nextFileName) {
            File file = new File(nextFileName);

            if (file.exists()) {
                file.delete();
            }

            switch (compressionMode) {
                case NONE:
                    util.rename(getActiveFileName(), nextFileName);
                    break;
                //todo: add compressions
                case GZ:
                    //compressor.compress(getActiveFileName(), fileNamePattern.convertInt(minIndex), null);
                    break;
                case ZIP:
                    //compressor.compress(getActiveFileName(), fileNamePattern.convertInt(minIndex), zipEntryFileNamePattern.convert(new Date()));
                    break;
            }
        }
    }

    @Override
    public String getActiveFileName(){
        return getParentsRawFileProperty();
    }

    public void setcompressionMode(String mode){
        if (mode.equals("gz")) {
            addInfo("Will use gz compression");
            compressionMode = CompressionMode.GZ;
        } else if (mode.equals("zip")){
            addInfo("Will use zip compression");
            compressionMode = CompressionMode.ZIP;
        } else {
            addInfo("No Compression will be used.");
            compressionMode = CompressionMode.NONE;
        }
    }

    public void setCheckPoint(CheckPoint checkPoint){
        this.checkPoint = checkPoint;
        //this.currentFileName = this.checkPoint.next();
    }

    private String nextFile(){
        this.currentFileName = this.checkPoint.next();
        return this.currentFileName;
    }

    private String getCurrentFileName(){
        return this.currentFileName;
    }

    private void setCurrentFileName(String fileName){
        this.currentFileName = fileName;
    }
}

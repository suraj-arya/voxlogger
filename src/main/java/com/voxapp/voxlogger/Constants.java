package com.voxapp.voxlogger;

/**
 * Created by surya on 1/2/15.
 * defines constants which will be used throughout
 * the library
 */
public class Constants {
    public static final int UNKNOWN_ERROR = 100001;

    //JSON keys
    public static class JSONKeys{
        public static final String KEY_MESSAGE = "message";
        public static final String KEY_ERROR = "error";
        public static final  String KEY_CHECKPOINTS = "checkPoints";
    }

    public static class CompressionModes{
        /**
         * defines compression modes to be set
         * in CheckPointBasedRollingPolicy
         */
        public static final String GZ_COMPRESSION = "gz";
        public static final String ZIP_COMPRESSION = "zip";
        public static final String NO_COMPRESSION = "none";
    }
}

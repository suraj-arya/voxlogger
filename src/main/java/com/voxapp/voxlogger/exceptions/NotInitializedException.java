package com.voxapp.voxlogger.exceptions;

/**
 * Created by surya on 1/6/15.
 * exception class for unitialized variable case
 */
public class NotInitializedException extends Exception{

    public NotInitializedException(String s) {
        super(s);
    }
}

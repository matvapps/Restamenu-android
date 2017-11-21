package com.restamenu.util;

import android.util.Log;

import com.restamenu.BuildConfig;

/**
 * @author Roodie
 */

public class Logger {

    /**
     * Can be changed in range from [0..3] to get right value
     */
    private static final int STACKTRACE_ELEMENT = 3;

    public static void log(String text) {
        if (BuildConfig.DEBUG) {
            String fullClassName = Thread.currentThread().getStackTrace()[STACKTRACE_ELEMENT].getClassName();
            String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            String methodName = Thread.currentThread().getStackTrace()[STACKTRACE_ELEMENT].getMethodName();
            int lineNumber = Thread.currentThread().getStackTrace()[STACKTRACE_ELEMENT].getLineNumber();

            Log.d(className + "." + methodName + "():" + lineNumber, text);
        }
    }

    public static void log(String text, Throwable t) {
        if (BuildConfig.DEBUG) {
            String fullClassName = Thread.currentThread().getStackTrace()[STACKTRACE_ELEMENT].getClassName();
            String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            String methodName = Thread.currentThread().getStackTrace()[STACKTRACE_ELEMENT].getMethodName();
            int lineNumber = Thread.currentThread().getStackTrace()[STACKTRACE_ELEMENT].getLineNumber();

            Log.d(className + "." + methodName + "():" + lineNumber, text, t);
        }
    }
}

package com.zhenhui.demo.sparklers.uic.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import org.springframework.dao.DuplicateKeyException;

public class ExceptionUtils {

    private static final String DUPLICATION_ENTRY = "Duplicate entry";
    private static final String UNIQUE_KEYWORD = "Unique index or primary key";

    public static String getStackTrace(Throwable aThrowable) {
        if (null != aThrowable) {
            final Writer result = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(result);
            aThrowable.printStackTrace(printWriter);
            return result.toString().replaceAll("\\n", "-");
        }

        return "";
    }

    public static boolean hasDuplicateEntryException(Throwable e) {

        if (e instanceof DuplicateKeyException && (e.getMessage().contains(DUPLICATION_ENTRY) || e.getMessage().contains(UNIQUE_KEYWORD))) {
            return true;
        }

        if (e.getCause() != null && (e.getCause().getMessage().contains(DUPLICATION_ENTRY) || e.getCause().getMessage().contains(UNIQUE_KEYWORD))) {
            return true;
        }

        return e.getCause() != null
            && (e.getCause().getMessage().contains(DUPLICATION_ENTRY) || e.getCause().getMessage().contains(UNIQUE_KEYWORD));

    }

}

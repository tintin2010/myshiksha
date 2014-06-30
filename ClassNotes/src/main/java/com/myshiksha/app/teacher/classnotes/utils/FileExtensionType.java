package com.myshiksha.app.teacher.classnotes.utils;

import android.util.Log;

import java.io.File;

/**
 * Created by seinjutichatterjee on 5/23/14.
 */
public enum FileExtensionType {
    PNG,
    JPEG,
    DOC,
    TXT,
    XLS,
    PDF;

    private static final String PNG_TYPE = "png";
    private static final String JPEG_TYPE = "jpeg";
    private static final String JPG_TYPE = "jpg";
    private static final String TXT_TYPE = "txt";
    private static final String DOC_TYPE = "doc";
    private static final String PDF_TYPE = "pdf";
    private static final String XLS_TYPE = "xls";


    public static FileExtensionType getType(File file) {
        String extension = file.getName().split("\\.")[1];

        Log.i(CONSTANTS.FILE_EXTENSION_TYPE_TAG, "Got file=" + file +" extensionStr=" + extension);

        if (extension.equalsIgnoreCase(PNG_TYPE)) {
            return PNG;
        }

        if (extension.equalsIgnoreCase(JPEG_TYPE) || extension.equalsIgnoreCase(JPG_TYPE)) {
            return JPEG;
        }

        if (extension.equalsIgnoreCase(DOC_TYPE)) {
            return DOC;
        }

        if (extension.equalsIgnoreCase(TXT_TYPE)) {
            return TXT;
        }

        if (extension.equalsIgnoreCase(PDF_TYPE)) {
            return PDF;
        }

        if (extension.equalsIgnoreCase(XLS_TYPE)) {
            return XLS;
        }

        return null;

    }
}

package com.github.alFReDNSH.imageViewer;

import java.io.File;
import java.io.FilenameFilter;

public class ImageFilter implements FilenameFilter {
    private static ImageFilter instance;
    private static final String SUPPORTED_FORMATS = "bmp|gif|jpeg|jpg|png";
    public static ImageFilter getInstance() {
        if (instance == null) {
            instance = new ImageFilter();
        }
        return instance;
    }

    @Override
    public boolean accept(File dir, String name) {
        return name.toLowerCase().matches(".*\\.(" + SUPPORTED_FORMATS + ")$");
    }
}

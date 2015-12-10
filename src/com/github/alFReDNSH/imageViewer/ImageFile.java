package com.github.alFReDNSH.imageViewer;

import javafx.scene.image.Image;
import java.io.File;
import java.nio.file.Path;

public class ImageFile extends File {
    private Image image;

    public ImageFile(String path) {
        super(path);
    }
    public ImageFile(File file) {
        this(file.getAbsolutePath());
    }
    public ImageFile(Path path) {
        this(path.toString());
    }
    public Image getImage() {
        if (image == null) {
            image = new Image("file:" + getAbsolutePath());
        }
        if (isDirectory()) {
            return null;
        }
        return image;
    }

    public double getRatio() {
        return getImage().getHeight() / getImage().getWidth();
    }
    public String toString() {
        return getName();
    }
}

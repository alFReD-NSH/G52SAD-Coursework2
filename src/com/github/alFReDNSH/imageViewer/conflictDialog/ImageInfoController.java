package com.github.alFReDNSH.imageViewer.conflictDialog;
import com.github.alFReDNSH.imageViewer.ImageFile;
import com.github.alFReDNSH.imageViewer.Util;
import com.github.alFReDNSH.imageViewer.ImageViewPane;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.Observable;


public class ImageInfoController {
    private ImageFile imageFile;
    private String title;
    private Boolean showImageVertical;

    public void setImageFile(ImageFile imageFile) {
        this.imageFile = imageFile;
    }
    public void setType(String title) {
        this.title = title;
    }
    public void setShowImageVertical(Boolean showImageVertical) {
        this.showImageVertical = showImageVertical;
    }

    public Parent rerender(Parent root) {
        ((Label) root.lookup("#titleLabel")).setText(title);
        ((Label) root.lookup("#sizeLabel")).setText("Size: " +
                Util.humanReadableByteCount(imageFile.length(), true));
        ((Label) root.lookup("#dateLabel")).setText("Last Modified: " +
                Util.formatDate(imageFile.lastModified()));
        Pane newRoot = showImageVertical ? new HBox() : new VBox();
        ImageView imageView = new ImageView();
        imageView.setImage(imageFile.getImage());
        ObservableList children = newRoot.getChildren();
        children.add(new ImageViewPane(imageView));
        children.add(root);
        return newRoot;

    }
}
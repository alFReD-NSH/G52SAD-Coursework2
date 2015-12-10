package com.github.alFReDNSH.imageViewer.conflictDialog;

import com.github.alFReDNSH.imageViewer.ImageFile;
import com.github.alFReDNSH.imageViewer.Util;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Dialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class ConflictDialog extends Dialog {
    private boolean remember;
    private String error;
    private boolean replaced;
    private boolean skipped;
    private boolean cancelled;
    private boolean done;
    private static Pane root;
    private static Parent sourceImageInfo;
    private static ImageInfoController sourceImageInfoController = new ImageInfoController();
    private static Parent destImageInfo;
    private static ImageInfoController destImageInfoController = new
            ImageInfoController();

    public ConflictDialog(Path source, Path destination) {
        URL imageInfoUrl =
                ConflictDialog.class.getResource("ImageInfo.fxml");
        try {
            root = FXMLLoader.load(
                    ConflictDialog.class.getResource("ConflictDialog.fxml"));
            sourceImageInfo = FXMLLoader.load(imageInfoUrl);
            destImageInfo = FXMLLoader.load(imageInfoUrl);
        } catch (Exception e) {
            throw new RuntimeException("Something wrong with the FXML", e);
        }

        setTitle("File Conflict");
        setHeaderText("Replace image " + source.getFileName() + "?");
        ImageFile sourceFile = new ImageFile(source);
        ImageFile destFile = new ImageFile(destination);
        double ratio = (sourceFile.getRatio() + destFile.getRatio()) / 2;
        boolean showVertical = ratio > 1;

        sourceImageInfoController.setImageFile(sourceFile);
        sourceImageInfoController.setShowImageVertical(showVertical);
        sourceImageInfoController.setType("Source File");
        destImageInfoController.setImageFile(destFile);
        destImageInfoController.setShowImageVertical(showVertical);
        destImageInfoController.setType("Replace With");

        ButtonType replace = new ButtonType("Replace");
        ButtonType skip = new ButtonType("Skip", ButtonBar.ButtonData.CANCEL_CLOSE);

        ObservableList imagesPaneChildren = ((Pane) root.lookup("#images")).getChildren();
        imagesPaneChildren.add(sourceImageInfoController.rerender(sourceImageInfo));
        imagesPaneChildren.add(destImageInfoController.rerender(destImageInfo));
        TextField nameField = (TextField) root.lookup("#nameField");
        CheckBox renameCheckbox = (CheckBox) root.lookup("#renameCheckbox");
        CheckBox rememberCheckbox = (CheckBox) root.lookup("#rememberCheckbox");
        nameField.visibleProperty().bind(renameCheckbox.selectedProperty());
        rememberCheckbox.disableProperty().bind(renameCheckbox.selectedProperty());
        nameField.setText(sourceFile.getName().replaceAll("\\.(.*)$", "(1).$1"));

        DialogPane dialogPane = getDialogPane();
        dialogPane.getButtonTypes().addAll(replace, skip, ButtonType.CANCEL);
        dialogPane.setContent(root);
        setResultConverter(button -> {
            done = true;
            if (button == replace) {
                replaced = true;
                System.out.printf("%s %s\n", source, destination.getParent());
                Path dest = renameCheckbox.isSelected() ?
                        destination.resolveSibling(nameField.getText()) :
                        destination;

                try {
                    Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception e) {
                    Util.handleException(e, "replacing the file");
                }
            } else if (button == skip) {
                skipped = true;
            }
            if (rememberCheckbox.isSelected()) {
                remember = true;
            }
            return null;
        });
    }

    public boolean isCancelled() {
        if (done) {
            return !skipped && !replaced;
        } else {
            throw new RuntimeException("Dialog is not closed yet. Call showAndWait " +
                "method");
        }
    }

    public boolean isAllSkipped() {
        if (done) {
            return skipped && remember;
        } else {
            throw new RuntimeException("Dialog is not closed yet. Call showAndWait " +
                "method");
        }
    }
    public boolean isAllReplaced() {
        if (done) {
            return replaced && remember;
        } else {
            throw new RuntimeException("Dialog is not closed yet. Call showAndWait " +
                "method");
        }
    }
}

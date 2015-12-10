package com.github.alFReDNSH.imageViewer;

import com.github.alFReDNSH.imageViewer.conflictDialog.ConflictDialog;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.CopyOption;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.StandardCopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class MainViewController
{
    // Private fields for components 
    @FXML
    private ImageView mainImage;

    private FolderAlbum mainAlbum = new FolderAlbum(".");

    public void initialize()
    {
        refreshImage();
    }

    public void previousButtonListener() {
        mainAlbum.previous();
        refreshImage();
    }
    public void nextButtonListener() {
        mainAlbum.next();
        refreshImage();
    }

    public void addButtonListener() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open files to be copied to the album");
        List<File> files = fileChooser.showOpenMultipleDialog(mainImage.getScene()
                .getWindow());
        CopyOption copyOption = StandardCopyOption.COPY_ATTRIBUTES;
        if (files == null) {
            return;
        }
        boolean skip = false;
        for (File file : files) {
            Path source = file.toPath();
            Path dest = mainAlbum.toPath().resolve(file.getName());
            try {
                Files.copy(source, dest, copyOption);
            } catch (FileAlreadyExistsException e) {
                if (skip) {
                    continue;
                }
                ConflictDialog dialog = new ConflictDialog(source, dest);
                dialog.showAndWait();
                skip = dialog.isAllSkipped();
                if (dialog.isCancelled()) {
                    return;
                } else if (dialog.isAllReplaced()) {
                    copyOption = StandardCopyOption.REPLACE_EXISTING;
                }
            } catch (Exception e) {
                Util.handleException(e, "copying the file");
            }
        }
    }

    public void removeButtonListener() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Are you sure you want to remove this file from the album?");
        alert.setContentText("You cannot undo this action.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            mainAlbum.removeImage();
            refreshImage();
        }
    }

    private void refreshImage() {
        mainImage.setImage(new Image("File:" + mainAlbum.getCurrentImage()
                .getAbsolutePath()));
    }
}

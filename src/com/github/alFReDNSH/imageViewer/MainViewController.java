package com.github.alFReDNSH.imageViewer;

import com.github.alFReDNSH.imageViewer.conflictDialog.ConflictDialog;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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
        // Because scene is not initialized yet completely.
        Platform.runLater(() -> refreshImage());
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
        mainImage.setImage(mainAlbum.getCurrentImage().getImage());
        if (mainImage.getScene()!= null) {
            ((Stage)(mainImage.getScene().getWindow())).setTitle(
                    mainAlbum.getCurrentImage().getName());
        }
    }

    public void quit() {
        System.exit(0);
    }

    public void showAbout() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("This was developed by Farid Nouri Neshat.");
        alert.setContentText("Version 0.1");
        alert.show();
    }

    public void openAlbum() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Open the directory where you want it to be the album");
        File file = directoryChooser.showDialog(mainImage.getScene().getWindow());
        if (file == null) {
            return;
        }
        mainAlbum = new FolderAlbum(file);
        refreshImage();
    }
}

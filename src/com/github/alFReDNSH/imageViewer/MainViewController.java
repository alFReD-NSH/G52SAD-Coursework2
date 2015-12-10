package com.github.alFReDNSH.imageViewer;

import com.github.alFReDNSH.imageViewer.conflictDialog.ConflictDialog;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    @FXML
    private TreeView albumTree;

    private FolderAlbum mainAlbum = new FolderAlbum(".");

    public void initialize()
    {
        // Because scene is not initialized yet completely.
        Platform.runLater(() -> refreshImage());
        albumTree.setRoot(createNode(new ImageFile(System.getProperty("user.home") +
                "/Pictures")));
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


    // This method creates a TreeItem to represent the given File. It does this
    // by overriding the TreeItem.getChildren() and TreeItem.isLeaf() methods
    // anonymously, but this could be better abstracted by creating a
    // 'FileTreeItem' subclass of TreeItem. However, this is left as an exercise
    // for the reader.
    private TreeItem<ImageFile> createNode(final ImageFile f) {
        return new TreeItem<ImageFile>(f) {
            // We cache whether the ImageFile is a leaf or not. A ImageFile is a leaf if
            // it is not a directory and does not have any files contained within
            // it. We cache this as isLeaf() is called often, and doing the
            // actual check on ImageFile is expensive.
            private boolean isLeaf;

            // We do the children and leaf testing only once, and then set these
            // booleans to false so that we do not check again during this
            // run. A more complete implementation may need to handle more
            // dynamic file system situations (such as where a folder has files
            // added after the TreeView is shown). Again, this is left as an
            // exercise for the reader.
            private boolean isFirstTimeChildren = true;
            private boolean isFirstTimeLeaf = true;

            @Override public ObservableList<TreeItem<ImageFile>> getChildren() {
                if (isFirstTimeChildren) {
                    isFirstTimeChildren = false;

                    // First getChildren() call, so we actually go off and
                    // determine the children of the ImageFile contained in this TreeItem.
                    super.getChildren().setAll(buildChildren(this));
                }
                return super.getChildren();
            }

            @Override public boolean isLeaf() {
                if (isFirstTimeLeaf) {
                    isFirstTimeLeaf = false;
                    ImageFile f = (ImageFile) getValue();
                    isLeaf = f.isFile();
                }

                return isLeaf;
            }

            private ObservableList<TreeItem<ImageFile>> buildChildren(TreeItem<ImageFile> TreeItem) {
                ImageFile f = TreeItem.getValue();
                if (f != null && f.isDirectory()) {
                    File[] files = f.listFiles();
                    if (files != null) {
                        ObservableList<TreeItem<ImageFile>> children = FXCollections.observableArrayList();

                        for (File childImageFile : files) {
                            children.add(createNode(new ImageFile(childImageFile)));
                        }

                        return children;
                    }
                }

                return FXCollections.emptyObservableList();
            }
        };
    }

    public void treeViewOnClick() {
        TreeItem t = ((TreeItem) albumTree.getSelectionModel().getSelectedItem());
        if (t == null) {
            return;
        }
        File f = (File) t.getValue();
        if (f.isDirectory()) {
            mainAlbum = new FolderAlbum(f);
        } else {
            mainAlbum = new FolderAlbum(f.getParent());
            mainAlbum.setCurrentImage(f.getName());
        }
        refreshImage();
    }
}

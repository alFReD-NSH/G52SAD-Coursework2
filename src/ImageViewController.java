import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class ImageViewController 
{
    // Private fields for components 
    @FXML
    private ImageView mainImage;
    @FXML
    private Button previousButton;
    @FXML
    private Button nextButton;

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

    private void refreshImage() {
        System.out.println(mainAlbum.getCurrentImage().getAbsolutePath());
        mainImage.setImage(new Image("File:" + mainAlbum.getCurrentImage()
                .getAbsolutePath()));
    }
}

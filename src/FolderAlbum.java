import java.io.File;
import java.util.ArrayList;

public class FolderAlbum extends File {
    private ArrayList<ImageFile> images = new ArrayList<>();
    private ImageFile currentImage; // Maybe make this a javafx Image

    public FolderAlbum(String path) {
        super(path);
        String[] imagePaths = list(ImageFilter.getInstance());
        for (String p : imagePaths) {
            images.add(new ImageFile(path + "/" + p));
        }
        currentImage = images.get(0);
    }

    public void next() {
        int index = images.indexOf(currentImage) + 1;
        if (index >= images.size()) {
            index = 0;
        }
        currentImage = images.get(index);
    }

    public void previous() {
        int index = images.indexOf(currentImage) - 1;
        if (index < 0) {
            index = images.size() - 1;
        }
        currentImage = images.get(index);
    }

    public ArrayList<ImageFile> getImages() {
        return images;
    }

    public ImageFile getCurrentImage() {
        return currentImage;
    }

    public void setCurrentImage(ImageFile currentImage) {
        this.currentImage = currentImage;
    }
}

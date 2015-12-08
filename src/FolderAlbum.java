import java.io.File;
import java.util.ArrayList;

public class FolderAlbum extends File {
    private ArrayList<ImageFile> images = new ArrayList<>();
    private ImageFile currentImage; // Maybe make this a javafx Image

    public FolderAlbum(String path) {
        super(path);
        addImagesFromDirectory(this);
        currentImage = images.get(0);
    }

    private void addImagesFromDirectory(File dir) {
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                addImagesFromDirectory(file);
            } else if (ImageFilter.getInstance().accept(dir, file.getName())){
                images.add(new ImageFile(file.getPath()));
            }
        }
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

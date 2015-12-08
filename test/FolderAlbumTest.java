import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class FolderAlbumTest {
    FolderAlbum folderAlbumOne = new FolderAlbum(getResourcePath("one"));
    FolderAlbum folderAlbumTwo = new FolderAlbum(getResourcePath("two"));

    @Test
    public void testGetCurrentImage() throws Exception {
        testGetCurrentImage(folderAlbumOne, "one/a");
    }

    private void testGetCurrentImage(FolderAlbum folderAlbum, String path) throws
            Exception {
        assertEquals(folderAlbum.getCurrentImage().getPath(),
                getResourcePath(path + ".jpg"));
    }

    @Test
    public void testNext() throws Exception {
        folderAlbumOne.next();
        testGetCurrentImage();
        folderAlbumTwo.next();
        testGetCurrentImage(folderAlbumTwo, "two/b");
    }

    @Test
    public void testPrevious() throws Exception {
        folderAlbumOne.previous();
        testGetCurrentImage();
        folderAlbumTwo.previous();
        testGetCurrentImage(folderAlbumTwo, "two/a");
    }

    @Test
    public void testMulti() throws Exception {
        FolderAlbum folderAlbumMulti = new FolderAlbum(getResourcePath("multi"));
        assertEquals(folderAlbumMulti.getImages().size(), 2);
    }

    private String getResourcePath(String p) {
       return getClass().getResource(p).getPath();
    }
}
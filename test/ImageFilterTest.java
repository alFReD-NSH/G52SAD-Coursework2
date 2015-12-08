import org.testng.annotations.Test;
import java.io.File;
import static org.testng.Assert.*;

public class ImageFilterTest {
    ImageFilter instance = ImageFilter.getInstance();

    @Test
    public void testAccept() throws Exception {
        File parent = new File(".");
        assertEquals(instance.accept(parent,"asdasd.something"), false);
        assertEquals(instance.accept(parent,"asdasd.jpg"), true);
        assertEquals(instance.accept(parent,"asdasd.jPg"), true);
        assertEquals(instance.accept(parent,"jPg"), false);
    }
}
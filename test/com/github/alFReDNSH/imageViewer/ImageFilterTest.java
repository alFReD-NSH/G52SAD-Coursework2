package com.github.alFReDNSH.imageViewer;

import com.github.alFReDNSH.imageViewer.ImageFilter;
import org.testng.annotations.Test;
import java.io.File;
import static org.testng.Assert.*;

public class ImageFilterTest {
    ImageFilter instance = ImageFilter.getInstance();

    @Test
    public void testAccept() throws Exception {
        File parent = new File(".");
        assertEquals(instance.accept(parent,"a.something"), false);
        assertEquals(instance.accept(parent,"a.jpg"), true);
        assertEquals(instance.accept(parent,"a.jPg"), true);
        assertEquals(instance.accept(parent,"jPg"), false);
    }
}
package com.github.alFReDNSH.imageViewer;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class UtilTest {
    @Test
    public void testFormatDate() throws Exception {
        assertEquals(Util.formatDate(0), "Jan 1 1970");
    }
}
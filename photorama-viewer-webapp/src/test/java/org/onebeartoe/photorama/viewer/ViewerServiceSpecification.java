
package org.onebeartoe.photorama.viewer;

import java.io.File;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author Roberto Marquez
 */
public class ViewerServiceSpecification
{
    ViewerService implementation;
    
    @BeforeMethod
    public void setUpMethod() throws Exception
    {
        implementation = new ViewerService();
    }

    /**
     * Given the files under src/test/resources/viewer/ this test expects
     * 2 images and 1 directory.
     */
    @Test
    public void loadDirectoryView() throws Exception
    {
        String capturePath = "src/test/resources/";
        
        File captureDirectory = new File(capturePath);
        
        String subpath = "viewer";
        
        DirectoryView view = implementation.loadDirectoryView(captureDirectory, subpath);
        
        int actualDirectoryCount  = view.directories.size();
        
        int expectedDirCount = 1;
        
        assertEquals(expectedDirCount, actualDirectoryCount);
        
        int actualImages = view.images.size();
        
        int expectedImages = 2;
        
        assertEquals(expectedImages, actualImages);
    }
}

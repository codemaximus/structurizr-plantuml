package name.codemax.structurizr.plantuml.docs;

import com.structurizr.documentation.Image;
import com.structurizr.util.ImageUtils;

import java.io.File;
import java.io.IOException;

/**
 * Imports raster static images.
 * Implementation is similar to the default one.
 *
 * @author Maksim Osipov
 */
public class StaticImageImporter extends AbstractImageImporter {
    @Override
    protected boolean canImport(String name) {
        return name.endsWith(".png")
                || name.endsWith(".jpg")
                || name.endsWith(".jpeg")
                || name.endsWith(".gif");
    }

    @Override
    protected Image importImage(String path, File file) throws IOException {
        String contentType = ImageUtils.getContentType(file);
        String base64Content = ImageUtils.getImageAsBase64(file);

        String name = getFileName(path, file);

        return new Image(name, contentType, base64Content);
    }
}

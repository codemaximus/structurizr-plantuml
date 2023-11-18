package name.codemax.structurizr.plantuml.docs;

import com.structurizr.documentation.Image;
import com.structurizr.util.ImageUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

/**
 * Imports arbitrary SVG images.
 * This feature is missing in the default implementation.
 *
 * @author Maksim Osipov
 */
public class SVGImageImporter extends AbstractImageImporter {
    @Override
    protected boolean canImport(String name) {
        return name.endsWith(".svg");
    }

    @Override
    protected Image importImage(String path, File file) throws IOException {
        String contentType = ImageUtils.getContentType(file);
        String base64Content = Base64.getEncoder().encodeToString(Files.readAllBytes(file.toPath()));

        String name = getFileName(path, file);

        return new Image(name, contentType, base64Content);
    }
}

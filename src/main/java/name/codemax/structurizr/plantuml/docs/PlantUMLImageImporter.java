package name.codemax.structurizr.plantuml.docs;

import com.structurizr.documentation.Image;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;

public class PlantUMLImageImporter extends AbstractImageImporter {
    @Override
    protected boolean canImport(String name) {
        return name.endsWith(".puml");
    }

    @Override
    protected Image importImage(String path, File file) throws IOException {
        SourceStringReader reader = new SourceStringReader(Files.readString(file.toPath(), StandardCharsets.UTF_8));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        reader.outputImage(output, new FileFormatOption(FileFormat.SVG));

        String base64Content = Base64.getEncoder().encodeToString(output.toByteArray());
        String name = getFileName(path, file) + ".svg";

        return new Image(name, "image/svg+xml", base64Content);
    }
}

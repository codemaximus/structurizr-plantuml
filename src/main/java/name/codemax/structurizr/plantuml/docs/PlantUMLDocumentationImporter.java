package name.codemax.structurizr.plantuml.docs;

import com.structurizr.documentation.Documentable;
import com.structurizr.importer.documentation.DefaultDocumentationImporter;
import com.structurizr.importer.documentation.DocumentationImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.stream.Stream;

/**
 * Root documentation importer.
 * May be used as a standalone custom importer in DSL.
 *
 * @author Maksim Osipov
 */
public class PlantUMLDocumentationImporter implements DocumentationImporter {
    private static final Logger LOG = LoggerFactory.getLogger(PlantUMLDocumentationImporter.class);

    @Override
    public void importDocumentation(Documentable documentable, File path) {
        try {
            Stream.of(new DefaultDocumentationImporter(),
                            new StaticImageImporter(),
                            new SVGImageImporter(),
                            new PlantUMLImageImporter())
                    .forEach(importer -> importer.importDocumentation(documentable, path));
        } catch (Exception e) {
            LOG.error("Documentation import exception: " + e.getMessage(), e);
            throw e;
        }
    }
}

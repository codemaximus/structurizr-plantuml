package name.codemax.structurizr.plantuml;

import com.structurizr.Workspace;
import com.structurizr.documentation.Documentable;
import com.structurizr.importer.documentation.DocumentationImporter;
import com.structurizr.model.Element;
import name.codemax.structurizr.plantuml.docs.PlantUMLDocumentationImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;

/**
 * Generates documentation with processing PlantUML diagrams in it.
 * Documentation is generated for elements with special tags.
 * These tags are started from specified prefix.
 *
 * @author Maksim Osipov
 */
public class PlantUMLDocumentationProcessor implements WorkspaceProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(PlantUMLDocumentationProcessor.class);
    private static final String PATH_DELIMITER = "/";

    private final String tagPrefix;
    private final Path documentationRootPath;

    public PlantUMLDocumentationProcessor(String tagPrefix, Path documentationRootPath) {
        this.tagPrefix = tagPrefix;
        this.documentationRootPath = documentationRootPath;
    }

    @Override
    public void processWorkspace(Workspace workspace) {
        for (Element element : workspace.getModel().getElements()) {
            if (!(element instanceof Documentable documentableElement)) {
                continue;
            }

            element.getTagsAsSet().stream()
                    .filter(tag -> tag.startsWith(tagPrefix))
                    .map(tag -> tag.substring(tagPrefix.length()))
                    .forEach(tag -> importDocumentation(buildDocumentationPath(tag).toFile(), documentableElement));
        }
    }

    private Path buildDocumentationPath(String tag) {
        Path path = documentationRootPath;
        for (String part : tag.split(PATH_DELIMITER)) {
            if (part.isEmpty()) {
                continue;
            }
            path = path.resolve(part);
        }
        return path;
    }

    private static void importDocumentation(File docDir, Documentable element) {
        if (!docDir.exists() || !docDir.isDirectory()) {
            LOG.warn("Directory '{}' does not exists. Documentation import will be skipped.", docDir.getName());
            return;
        }

        DocumentationImporter documentationImporter = new PlantUMLDocumentationImporter();
        documentationImporter.importDocumentation(element, docDir);
    }
}

package name.codemax.structurizr.plantuml;

import com.structurizr.Workspace;
import com.structurizr.documentation.Documentable;
import com.structurizr.dsl.StructurizrDslPlugin;
import com.structurizr.dsl.StructurizrDslPluginContext;
import com.structurizr.importer.documentation.DocumentationImporter;
import com.structurizr.model.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import name.codemax.structurizr.plantuml.docs.PlantUMLDocumentationImporter;

import java.io.File;
import java.nio.file.Path;

public class StructurizrDocumentationGenerator implements StructurizrDslPlugin {
    private static final Logger LOG = LoggerFactory.getLogger(StructurizrDocumentationGenerator.class);
    private static final String TAG_PREFIX_PROPERTY = "tagPrefix";
    private static final String DOC_ROOT_PATH_PROPERTY = "docRootPath";
    private static final String DEFAULT_PREFIX = "doc";
    private static final String DEFAULT_ROOT_PATH = "docs";
    private static final char DELIMITER = ':';

    @Override
    public void run(StructurizrDslPluginContext context) {
        String prefix = context.getParameter(TAG_PREFIX_PROPERTY, DEFAULT_PREFIX) + DELIMITER;
        if (prefix.length() == 1) {
            LOG.warn("Tag prefix is empty. Plugin is disabled.");
            return;
        }

        String rootPath = context.getParameter(DOC_ROOT_PATH_PROPERTY, DEFAULT_ROOT_PATH);
        if (rootPath.isEmpty()) {
            LOG.warn("Documentation root path is empty. Plugin is disabled.");
            return;
        }

        Workspace workspace = context.getWorkspace();
        for (Element element : workspace.getModel().getElements()) {
            if (!(element instanceof Documentable documentableElement)) {
                continue;
            }

            element.getTagsAsSet().stream()
                    .filter(tag -> tag.startsWith(prefix))
                    .map(tag -> tag.substring(prefix.length()))
                    .forEach(tag -> importDocumentation(context, Path.of(rootPath, tag), documentableElement));
        }
    }

    private static void importDocumentation(StructurizrDslPluginContext context, Path docPath, Documentable element)
    {
        File file = Path.of(context.getDslFile().getParent(), docPath.toString()).toFile();
        if (!file.exists() || !file.isDirectory()) {
            LOG.warn("Directory '{}' does not exists. Documentation import will be skipped.", file.getName());
            return;
        }

        DocumentationImporter documentationImporter = new PlantUMLDocumentationImporter();
        documentationImporter.importDocumentation(element, file);
    }
}

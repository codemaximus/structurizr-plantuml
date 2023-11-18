package name.codemax.structurizr.plantuml;

import com.structurizr.Workspace;
import com.structurizr.dsl.StructurizrDslPlugin;
import com.structurizr.dsl.StructurizrDslPluginContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * <p>Structurizr PlantUML Integration Plugin.</p>
 * <p>Parameters:</p>
 * <ul>
 *   <li><code>tagPrefix</code>: special prefix for tags that are recognized as documentation tags.
 *   Default value is <code>doc</code>.</li>
 *   <li><code>docRootPath</code>: path to documentation in workspace.
 *   Default value is <code>docs</code>.</li>
 * </ul>
 *
 * @author Maksim Osipov
 */
public class StructurizrPlantUMLPlugin implements StructurizrDslPlugin {
    private static final Logger LOG = LoggerFactory.getLogger(StructurizrPlantUMLPlugin.class);
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
        Path documentationRoot = Path.of(context.getDslFile().getParent(), rootPath);
        Stream.of(new PlantUMLDocumentationProcessor(prefix, documentationRoot),
                        new PlantUMLImageViewGenerator())
                .forEach(processor -> processor.processWorkspace(workspace));
    }
}

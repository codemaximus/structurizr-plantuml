package name.codemax.structurizr.plantuml;

import com.structurizr.Workspace;
import com.structurizr.documentation.Documentable;
import com.structurizr.documentation.Image;
import com.structurizr.dsl.StructurizrDslPlugin;
import com.structurizr.dsl.StructurizrDslPluginContext;
import com.structurizr.model.Element;
import com.structurizr.view.ImageView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

public class StructurizrImageViewGenerator implements StructurizrDslPlugin {
    private static final Logger LOG = LoggerFactory.getLogger(StructurizrImageViewGenerator.class);

    private static final String SUFFIX_PROPERTY = "imageSuffix";
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\W+");

    @Override
    public void run(StructurizrDslPluginContext context) {
        String suffix = context.getParameter(SUFFIX_PROPERTY, "");
        if (suffix.isEmpty()) {
            LOG.warn("Suffix is empty. No image views will be extracted.");
            return;
        }

        Workspace workspace = context.getWorkspace();
        for (Element element : workspace.getModel().getElements()) {
            if (!(element instanceof Documentable documentableElement)) {
                continue;
            }

            documentableElement.getDocumentation().getImages().stream()
                    .filter(image -> image.getName().endsWith(suffix))
                    .forEach(image -> createView(workspace, element, image, suffix));
        }
    }

    private static void createView(Workspace workspace, Element element, Image image, String suffix) {
        String imageName = image.getName().substring(0, image.getName().length() - suffix.length());
        String elementName = WHITESPACE_PATTERN.matcher(element.getName()).replaceAll("");
        ImageView view = workspace.getViews().createImageView(element, elementName + '-' + imageName);
        view.setTitle(element.getName() + ": " + imageName);
        view.setContentType(image.getType());
        view.setContent("data:" + image.getType() + ";base64," + image.getContent());
    }
}

package name.codemax.structurizr.plantuml;

import com.structurizr.Workspace;
import com.structurizr.documentation.Documentable;
import com.structurizr.documentation.Image;
import com.structurizr.model.Element;
import com.structurizr.view.ImageView;

import java.util.Set;
import java.util.regex.Pattern;

/**
 * Generates image views from PlantUML diagrams in documentation.
 *
 * @author Maksim Osipov
 */
public class PlantUMLImageViewGenerator implements WorkspaceProcessor {
    private static final Set<String> SUFFIXES = Set.of(".puml.svg");
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\W+");

    public void processWorkspace(Workspace workspace) {
        for (Element element : workspace.getModel().getElements()) {
            if (!(element instanceof Documentable documentableElement)) {
                continue;
            }

            SUFFIXES.forEach(suffix ->
            documentableElement.getDocumentation().getImages().stream()
                    .filter(image -> image.getName().endsWith(suffix))
                    .forEach(image -> createView(workspace, element, image, suffix)));
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

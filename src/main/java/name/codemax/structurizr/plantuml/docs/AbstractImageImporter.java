package name.codemax.structurizr.plantuml.docs;

import com.structurizr.documentation.Documentable;
import com.structurizr.documentation.Image;
import com.structurizr.importer.documentation.DocumentationImportException;
import com.structurizr.importer.documentation.DocumentationImporter;
import com.structurizr.util.StringUtils;

import java.io.File;
import java.io.IOException;

/**
 * Abstract importer for images.
 *
 * @author Maksim Osipov
 */
public abstract class AbstractImageImporter implements DocumentationImporter {
    @Override
    public void importDocumentation(Documentable documentable, File path) {
        if (documentable == null) {
            throw new IllegalArgumentException("A workspace or software system must be specified.");
        }

        if (path == null) {
            throw new IllegalArgumentException("A path must be specified.");
        } else if (!path.exists()) {
            throw new IllegalArgumentException(path.getAbsolutePath() + " does not exist.");
        }

        try {
            if (path.isDirectory()) {
                importImages(documentable, "", path);
            } else {
                importImage(documentable, "", path);
            }
        } catch (Exception e) {
            throw new DocumentationImportException(e.getMessage(), e);
        }
    }

    private void importImages(Documentable documentable, String root, File path) throws IOException {
        File[] files = path.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            String name = file.getName().toLowerCase();
            if (file.isDirectory() && !file.isHidden()) {
                if (StringUtils.isNullOrEmpty(root)) {
                    importImages(documentable, file.getName(), file);
                } else {
                    importImages(documentable, root + "/" + file.getName(), file);
                }
            } else {
                if (canImport(name)) {
                    importImage(documentable, root, file);
                }
            }
        }
    }

    private void importImage(Documentable documentable, String path, File file) throws IOException {
        documentable.getDocumentation().addImage(importImage(path, file));
    }

    protected static String getFileName(String path, File file) {
        String name;
        if (StringUtils.isNullOrEmpty(path)) {
            name = file.getName();
        } else {
            name = path + "/" + file.getName();
        }
        return name;
    }

    protected abstract boolean canImport(String name);

    protected abstract Image importImage(String path, File file) throws IOException;
}

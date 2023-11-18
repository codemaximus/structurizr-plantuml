package name.codemax.structurizr.plantuml;

import com.structurizr.Workspace;

/**
 * @author Maksim Osipov
 */
@FunctionalInterface
public interface WorkspaceProcessor {
    void processWorkspace(Workspace workspace);
}

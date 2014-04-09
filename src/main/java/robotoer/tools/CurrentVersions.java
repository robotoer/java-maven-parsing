package robotoer.tools;

import robotoer.project.BuildDefinition;
import robotoer.project.Module;
import robotoer.project.Workspace;

/**
 * Tool for listing the current versions of checked out projects (assuming you are using kiji-build)
 */
public class CurrentVersions {
  public static void main(String[] args) {
    final Workspace workspace = Workspace.fromPath(args[0]);
    for (Module module : workspace.getModules()) {
      // Get the current version from the module
      final BuildDefinition buildDefinition = module.getBuildDefinition();

      final String moduleName = buildDefinition.getName();
      final String moduleVersion = buildDefinition.getVersion();

      System.out.println(String.format("%s: %s", moduleName, moduleVersion));
    }
  }
}

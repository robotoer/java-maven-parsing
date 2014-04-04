package robotoer.project;

import java.io.File;
import java.util.List;

/**
 * Represents a workspace of the entire kiji project.
 */
public class Workspace {
  private final List<Module> mModules;
  private final File mRoot;

  public Workspace(
      final List<Module> modules,
      final File path
  ) {
    this.mModules = modules;
    this.mRoot = path;
  }

  public File getPath() {
    return mRoot;
  }

  public List<Module> getModules() {
    return mModules;
  }


  public static Workspace fromPath(
      final String path
  ) {
    return null;
  }
}

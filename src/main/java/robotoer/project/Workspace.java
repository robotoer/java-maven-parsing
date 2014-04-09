package robotoer.project;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

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
    final File workspaceRoot = new File(path);
    Preconditions.checkArgument(workspaceRoot.isDirectory());

    final File[] moduleRoots = workspaceRoot.listFiles(new FileFilter() {
      @Override
      public boolean accept(File moduleRoot) {
        final boolean hasPom = moduleRoot
            .listFiles(new FileFilter() {
              @Override
              public boolean accept(File moduleFile) {
                return moduleFile.isFile() && moduleFile.getName().equals("pom.xml");
              }
            })
            .length == 0;
        return moduleRoot.isDirectory() && hasPom;
      }
    });
    final List<Module> modules = Lists.transform(
        Lists.newArrayList(moduleRoots),
        new Function<File, Module>() {
          @Override
          public Module apply(File input) {
            new Module()
          }
        }
    );

    return new Workspace(modules, workspaceRoot);
  }
}

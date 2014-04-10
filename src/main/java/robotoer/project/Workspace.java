package robotoer.project;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

/**
 * Represents a workspace of the entire kiji project.
 */
public class Workspace {
  private final List<Module> mModules;
  private final Path mRoot;

  public Workspace(
      final List<Module> modules,
      final Path path
  ) {
    this.mModules = modules;
    this.mRoot = path;
  }

  public Path getPath() {
    return mRoot;
  }

  public List<Module> getModules() {
    return mModules;
  }


  public static class PomVisitor extends SimpleFileVisitor<Path> {

    private final ImmutableList.Builder<Path> mDiscoveredPomFiles;
    private final PathMatcher mPomMatcher;

    public PomVisitor(final PathMatcher pomMatcher) {
      mDiscoveredPomFiles = ImmutableList.builder();
      mPomMatcher = pomMatcher;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
      if (mPomMatcher.matches(file)) {
        mDiscoveredPomFiles.add(file);
      }

      return FileVisitResult.CONTINUE;
    }

    public ImmutableList<Path> getModules() {
      return mDiscoveredPomFiles.build();
    }
  }


  public static Workspace fromPath(
      final String workspaceRoot
  ) throws IOException {
    return fromPath(Paths.get(workspaceRoot));
  }

  public static Workspace fromPath(
      final Path workspaceRoot
  ) throws IOException {
    Preconditions.checkArgument(Files.isDirectory(workspaceRoot));

    // Find all pom files 1 level deep.
    final PathMatcher pomMatcher;
    if (workspaceRoot.toString().endsWith("/")) {
      pomMatcher = FileSystems.getDefault()
          .getPathMatcher(String.format("glob:%s*/pom.xml", workspaceRoot.toString()));
    } else {
      pomMatcher = FileSystems.getDefault()
          .getPathMatcher(String.format("glob:%s/*/pom.xml", workspaceRoot.toString()));
    }
    final PomVisitor pomVisitor = new PomVisitor(pomMatcher);
    Files.walkFileTree(workspaceRoot, pomVisitor);
    final List<Path> pomFiles = pomVisitor.getModules();

    final List<Module> modules = Lists.transform(
        Lists.newArrayList(pomFiles),
        new Function<Path, Module>() {
          @Override
          public Module apply(Path input) {
            return new Module(input.getParent());
          }
        }
    );

    return new Workspace(modules, workspaceRoot);
  }
}

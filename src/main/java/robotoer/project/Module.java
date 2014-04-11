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
import java.util.Map;
import java.util.logging.Logger;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import robotoer.ast.java.JavaLexer;
import robotoer.ast.java.JavaParser;
import robotoer.util.GlobDirectoryMatcher;

public class Module {
  private final static Logger LOG = Logger.getLogger(Module.class.getName());
  public static final Path SOURCE_DIRECTORY = Paths.get("src/main/java");

  private final Path mModuleRoot;
  //private final Git mVersionControl;

  public Module(
      final Path path
  ) {
    mModuleRoot = path;
  }

  private static class JavaFileVisitor extends SimpleFileVisitor<Path> {
    public static final PathMatcher JAVA_FILE_MATCHER =
        FileSystems.getDefault().getPathMatcher("glob:**/*.java");

    private final ImmutableList.Builder<Path> mJavaFiles;

    protected JavaFileVisitor() {
      super();

      mJavaFiles = ImmutableList.builder();
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
      if ((attrs.isRegularFile() || attrs.isSymbolicLink()) && JAVA_FILE_MATCHER.matches(file)) {
        mJavaFiles.add(file);
      }

      return FileVisitResult.CONTINUE;
    }

    public List<Path> getJavaFiles() {
      return mJavaFiles.build();
    }
  }


  public Map<Path, JavaParser> getParsedJava() throws IOException {
    final PathMatcher srcDirMatcher = FileSystems.getDefault()
        .getPathMatcher(String.format("glob:%s/src/main/java", mModuleRoot.toAbsolutePath()));
    final PathMatcher nestedSrcDirMatcher = FileSystems.getDefault()
        .getPathMatcher(String.format("glob:%s/*/src/main/java", mModuleRoot.toAbsolutePath()));
    final List<Path> srcDirs = ImmutableList.<Path>builder()
        .addAll(GlobDirectoryMatcher.find(mModuleRoot, srcDirMatcher))
        .addAll(GlobDirectoryMatcher.find(mModuleRoot, nestedSrcDirMatcher))
        .build();

    if (srcDirs.isEmpty()) {
      LOG.warning(String.format("Did not find any source directories in %s", mModuleRoot));
    }

    final ImmutableMap.Builder<Path, JavaParser> mapBuilder =
        ImmutableMap.builder();
    for (Path srcDir : srcDirs) {
      final JavaFileVisitor javaFileVisitor = new JavaFileVisitor();
      Files.walkFileTree(srcDir, javaFileVisitor);
      final List<Path> javaFiles = javaFileVisitor.getJavaFiles();

      for (Path javaFile : javaFiles) {
        final Lexer lexer = new JavaLexer(new ANTLRFileStream(javaFile.toAbsolutePath().toString()));
        final CommonTokenStream tokens = new CommonTokenStream(lexer);
        final JavaParser parser = new JavaParser(tokens);
        mapBuilder.put(javaFile, parser);
      }
    }
    return mapBuilder.build();
  }

  public BuildDefinition getBuildDefinition() {
    return null;
  }

  public Path getModuleRoot() {
    return mModuleRoot;
  }
}

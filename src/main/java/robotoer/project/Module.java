package robotoer.project;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import robotoer.ast.java.JavaLexer;
import robotoer.ast.java.JavaParser;
import robotoer.util.FileUtils;

public class Module {
  public static final Path SOURCE_DIRECTORY = Paths.get("src/main/java");
  public static final PathMatcher JAVA_FILE_MATCHER =
      FileSystems.getDefault().getPathMatcher("glob:**/*.java");

  private final Path mModuleRoot;
  //private final Git mVersionControl;

  public Module(
      final Path path
  ) {
    mModuleRoot = path;
  }

  private static class JavaFileVisitor extends SimpleFileVisitor<Path> {
    protected JavaFileVisitor() {
      // Accept a builder/list to populate?
      super();
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
      // Accept/store 'glob:**/*.java' files.
      return FileVisitResult.CONTINUE;
    }
  }

  public List<JavaParser.CompilationUnitContext> getCompilationUnits() throws IOException {
    final Path sourceRoot = mModuleRoot.resolve(SOURCE_DIRECTORY);
    Preconditions.checkState(
        Files.isDirectory(sourceRoot),
        "Source root (%s) is not a directory.",
        SOURCE_DIRECTORY.toAbsolutePath().toString()
    );

    final List<File> javaFiles =
        FileUtils.findFiles(sourceRoot, Pattern.compile(".*\\.java"), true, false, true);

    final List<JavaParser.CompilationUnitContext> parsed = Lists.newArrayList();
    for (File javaFile : javaFiles) {
      final Lexer lexer = new JavaLexer(new ANTLRFileStream(javaFile.getAbsolutePath()));
      final CommonTokenStream tokens = new CommonTokenStream(lexer);
      final JavaParser parser = new JavaParser(tokens);
      parsed.add(parser.compilationUnit());
    }
    return parsed;
  }

  public BuildDefinition getBuildDefinition() {
    return null;
  }

  public Path getModuleRoot() {
    return mModuleRoot;
  }
}

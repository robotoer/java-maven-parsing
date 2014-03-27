package robotoer.project;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import com.google.common.collect.Lists;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import robotoer.ast.java.JavaLexer;
import robotoer.ast.java.JavaParser;
import robotoer.util.FileUtils;

public class Module {
  public static final String SOURCE_DIRECTORY = "src/main/java";

  private final String mPath;
  //private final Git mVersionControl;

  public Module(
      final String path
  ) {
    mPath = path;
  }

  public List<JavaParser.CompilationUnitContext> getCompilationUnits() throws IOException {
    final File rootDirectory = new File(String.format("%s/%s", mPath, SOURCE_DIRECTORY));
    final List<File> javaFiles =
        FileUtils.findFiles(rootDirectory, Pattern.compile(".*\\.java"), true, false, true);

    final List<JavaParser.CompilationUnitContext> parsed = Lists.newArrayList();
    for (File javaFile : javaFiles) {
      final Lexer lexer = new JavaLexer(new ANTLRFileStream(javaFile.getAbsolutePath()));
      final CommonTokenStream tokens = new CommonTokenStream(lexer);
      final JavaParser parser = new JavaParser(tokens);
      parsed.add(parser.compilationUnit());
    }
    return parsed;
  }
}

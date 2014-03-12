package robotoer.tools;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import com.google.common.collect.Lists;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import robotoer.Model;
import robotoer.ast.java.JavaLexer;
import robotoer.ast.java.JavaParser;

public class Module {
  public static final String SOURCE_DIRECTORY = "src/main/java";

  private final String mPath;
  private final String mName;
  private final Model mBuildScript;
  //private final Git mVersionControl;

  public Module(
      final String path,
      final String name
  ) throws JAXBException {
    mPath = path;
    mName = name;

    // Load the pom file.
    {
      final JAXBContext context = JAXBContext.newInstance(Model.class);

      mBuildScript = (Model) context
          .createUnmarshaller()
          .unmarshal(new File(String.format("%s/%s", path, name)));
    }
  }

  public List<File> findFiles(
      final File directory,
      final Pattern pattern,
      final boolean matchFiles,
      final boolean matchDirectories,
      final boolean recursive
  ) {
    final List<File> matches = Lists.newArrayList();
    for (File file : directory.listFiles()) {
      final Matcher matcher = pattern.matcher(file.getName());

      if (file.isDirectory()) {
        if (matchDirectories && matcher.matches()) {
          matches.add(file);
        }

        // Recurse into subdirectories.
        if (recursive) {
          final List<File> subdirectoryMatches = findFiles(
              file,
              pattern,
              matchFiles,
              matchDirectories,
              true
          );
          matches.addAll(subdirectoryMatches);
        }
      } else {
        if (matchFiles && matcher.matches()) {
          matches.add(file);
        }
      }
    }
    return matches;
  }

  public List<JavaParser.CompilationUnitContext> getCompilationUnits() throws IOException {
    final File rootDirectory = new File(String.format("%s/%s", mPath, SOURCE_DIRECTORY));
    final List<File> javaFiles =
        findFiles(rootDirectory, Pattern.compile(".*\\.java"), true, false, true);

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

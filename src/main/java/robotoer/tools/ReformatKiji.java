package robotoer.tools;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;
import robotoer.ast.java.JavaParser;
import robotoer.project.Module;
import robotoer.project.Workspace;
import robotoer.util.JavaPrettyPrinter;

public class ReformatKiji {
  public static void main(String[] args) throws IOException {
    // Fetch all arguments and sanitize them.
    final Path outputDirectory = Paths.get("/home/robert/reformatted-kiji");
    final Path kijiDirectory = Paths.get("/home/robert/kiji");

    // Get all java files from Kiji.
    final Workspace workspace = Workspace.fromPath(kijiDirectory);
    for (Module module : workspace.getModules()) {
      for (JavaParser.CompilationUnitContext compilationUnit : module.getCompilationUnits()) {
        final List<String> before = null;
        final List<String> after = null;

        // Reformat all java file.
        final String reformatted = JavaPrettyPrinter.format(compilationUnit);

        // Diff the before and after.
        final Patch<String> diff = DiffUtils.diff(before, after);

        // Output diff to a specified folder.
        writeDiff(outputDirectory, diff);
      }
    }
  }

  private static void writeDiff(Path outputDirectory, Patch<String> diff) throws IOException {
    if (Files.exists(outputDirectory)) {
      Files.createDirectory(outputDirectory);
    }
    Preconditions.checkArgument(
        Files.isDirectory(outputDirectory),
        String.format("Output path must be a directory: %s", outputDirectory)
    );

    final List<String> diffLines = Lists.transform(
        diff.getDeltas(),
        new Function<Delta<String>, String>() {
          @Override
          public String apply(Delta<String> input) {
            return null;
          }
        }
    );

    Files.write(outputDirectory, diffLines, Charset.forName("UTF-8"), StandardOpenOption.CREATE);
  }
}

package robotoer.tools;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
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
      for (Map.Entry<Path, JavaParser.CompilationUnitContext> entry
          : module.getCompilationUnits().entrySet()) {
        final Path javaFile = entry.getKey();
        final JavaParser.CompilationUnitContext compilationUnit = entry.getValue();

        // Reformat all java file.
        final List<String> before = Files.readAllLines(javaFile, Charset.forName("UTF-8"));
        final List<String> after =
            ImmutableList.copyOf(JavaPrettyPrinter.format(compilationUnit).split("\\r?\\n"));

        // Diff the before and after.
        final Patch<String> diff = DiffUtils.diff(before, after);

        // Output diff to a specified folder.
        writeDiff(outputDirectory, diff);
      }
    }
  }

  private static void writeDiff(
      final Path outputDirectory,
      final Patch<String> diff
  ) throws IOException {
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
            return input.toString();
          }
        }
    );

    Files.write(outputDirectory, diffLines, Charset.forName("UTF-8"), StandardOpenOption.CREATE);
  }
}

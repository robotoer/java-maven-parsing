package robotoer.tools;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

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
import robotoer.util.JavaPrettyPrinter2;

public class ReformatKiji {
  private final static Logger LOG = Logger.getLogger(ReformatKiji.class.getName());

  public static void main(String[] args) throws IOException {
    // Fetch all arguments and sanitize them.
    final Path outputDirectory = Paths.get("/home/robert/reformatted-kiji");
    final Path kijiDirectory = Paths.get("/home/robert/kiji");

    if (!Files.exists(outputDirectory)) {
      Files.createDirectory(outputDirectory);
    }

    // Get all java files from Kiji.
    final Workspace workspace = Workspace.fromPath(kijiDirectory);
    for (Module module : workspace.getModules()) {
      for (Map.Entry<Path, JavaParser> entry
          : module.getParsedJava().entrySet()) {
        final Path javaFile = entry.getKey();
        final JavaParser parser = entry.getValue();

        // Reformat all java file.
        final List<String> before = Files.readAllLines(javaFile, Charset.forName("UTF-8"));
        final List<String> after =
            ImmutableList.copyOf(JavaPrettyPrinter2.prettyPrint(parser).split("\\r?\\n"));

        // Diff the before and after.
        final Patch<String> diff = DiffUtils.diff(before, after);

        // Output diff to a specified folder.
        final String outputFileName = javaFile
            .toAbsolutePath()
            .toString()
            .replace("/", "_");
        final Path outputFile = outputDirectory.resolve(outputFileName);
        writeDiff(outputFile, diff);

        LOG.info(String.format("Writing diff: %s to %s", diff, outputFile.toString()));
      }
    }
  }

  private static void writeDiff(
      final Path outputFile,
      final Patch<String> diff
  ) throws IOException {

    final List<String> diffLines = Lists.transform(
        diff.getDeltas(),
        new Function<Delta<String>, String>() {
          @Override
          public String apply(Delta<String> input) {
            return input.toString();
          }
        }
    );

    Files.write(outputFile, diffLines, Charset.forName("UTF-8"), StandardOpenOption.CREATE);
  }
}

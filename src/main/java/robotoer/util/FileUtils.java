package robotoer.util;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Lists;

public class FileUtils {
  // Disabled constructor.
  private FileUtils() { }

  public static List<File> findFiles(
      final File directory,
      final Pattern pattern,
      final boolean matchFiles,
      final boolean matchDirectories,
      final boolean recursive
  ) {
    final List<File> matches = Lists.newArrayList();
    System.out.println("finding files in: " + directory.toString());
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
}

package robotoer.util;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

import com.google.common.collect.ImmutableList;

public class GlobDirectoryMatcher extends SimpleFileVisitor<Path> {
  private final ImmutableList.Builder<Path> mMatchingDirs;
  private final PathMatcher mGlobMatcher;

  protected GlobDirectoryMatcher(final PathMatcher globMatcher) {
    super();
    mGlobMatcher = globMatcher;
    mMatchingDirs = ImmutableList.builder();
  }

  @Override
  public FileVisitResult preVisitDirectory(
      final Path dir,
      final BasicFileAttributes attrs
  ) throws IOException {
    if (mGlobMatcher.matches(dir)) {
      mMatchingDirs.add(dir);
    }

    return FileVisitResult.CONTINUE;
  }

  public PathMatcher getGlobMatcher() {
    return mGlobMatcher;
  }

  public List<Path> getMatchingDirs() {
    return mMatchingDirs.build();
  }

  public static List<Path> find(
      final Path root,
      final PathMatcher srcDirMatcher
  ) throws IOException {
    final GlobDirectoryMatcher matcher = new GlobDirectoryMatcher(srcDirMatcher);
    Files.walkFileTree(root, matcher);
    return matcher.getMatchingDirs();
  }
}

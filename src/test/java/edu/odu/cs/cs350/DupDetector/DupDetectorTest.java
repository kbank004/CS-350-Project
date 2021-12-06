package edu.odu.cs.cs350.DupDetector;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.nio.file.Path;

public class DupDetectorTest {
  private static final Path dataPath = Path.of("src/test/data");

  private final Path getDataPath(String path) {
    return dataPath.resolve(path);
  }

  @Test
  public final void testMaxSuggestions() {
    try {
      List<Path> paths = new ArrayList<Path>(Arrays.asList(getDataPath("")));
      DupDetector dup = new DupDetector(50, paths);
      assertThat(dup.getMaxSuggestions(), is(50));
    } catch (Exception e) {
      fail("Test threw unexpected exception! " + e);
    }
  }

  @Test
  public final void testPropertiesFile() {
    try {
      List<Path> paths = new ArrayList<Path>(Arrays.asList(getDataPath("")));
      DupDetector dup = new DupDetector(1, paths);
      List<String> cppExtensions = new ArrayList<String>(Arrays.asList("cpp", "h"));
      assertThat(dup.getCppExtensions(), is(cppExtensions));
      assertThat(dup.getMaxSubstitutions(), is(10));
      assertThat(dup.getMinSequenceLength(), is(8));

      paths.clear();
      paths.add(getDataPath("p.ini"));
      paths.add(getDataPath("a.cpp"));
      paths.add(getDataPath("b"));
      DupDetector dup1 = new DupDetector(1, paths);
      cppExtensions = new ArrayList<String>(Arrays.asList("C", "cpp", "h", "hpp", "H"));
      assertThat(dup1.getCppExtensions(), is(cppExtensions));
      assertThat(dup1.getMaxSubstitutions(), is(20));
      assertThat(dup1.getMinSequenceLength(), is(10));
    } catch (Exception e) {
      fail("Test threw unexpected exception! " + e);
    }
  }

  @Test
  public final void testFilePaths() {
    try {
      List<Path> paths = new ArrayList<Path>() {{
        add(getDataPath("a.cpp"));
        add(getDataPath("b"));
      }};
      DupDetector dup = new DupDetector(1, paths);
      Path[] expectedPaths = {
        getDataPath("a.cpp"),
        getDataPath("b/c.h"),
        getDataPath("b/c.cpp"),
      };
      assertThat(dup.getFilePaths(), containsInAnyOrder(expectedPaths));
    } catch (Exception e) {
      fail("Test threw unexpected exception! " + e);
    }
  }

  @Test
  public final void testSystem() {
    try {
      String[] args = {"5", getDataPath("a.cpp").toString(), getDataPath("b").toString()};
      int nSuggestions = Integer.parseInt(args[0]);
      List<Path> filePaths = DupDetector.toPaths(Arrays.copyOfRange(args, 1, args.length));
      
      DupDetector dup = new DupDetector(nSuggestions, filePaths);

      StringBuffer expectedOutpuBuffer = new StringBuffer("Files scanned:\n");
      expectedOutpuBuffer.append("    " + getDataPath("a.cpp").toAbsolutePath().toString() + ", 15\n");
      expectedOutpuBuffer.append("    " + getDataPath("b/c.cpp").toAbsolutePath().toString() + ", 0\n");
      expectedOutpuBuffer.append("    " + getDataPath("b/c.h").toAbsolutePath().toString() + ", 0\n");

      assertThat(dup.getOutput(), is(expectedOutpuBuffer.toString()));
    } catch (Exception e) {
      fail("Test threw unexpected exception! " + e);
    }
  }
}
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

  @Test
  public void testMaxSuggestions() {
    try {
      List<Path> paths = new ArrayList<Path>(Arrays.asList(Path.of(dataPath + "")));
      DupDetector dup = new DupDetector(50, paths);
      assertThat("dup.getMaxSuggestions() expected 50, saw " + dup.getMaxSuggestions(), dup.getMaxSuggestions(), is(50));
    } catch (Exception e) {
      fail("Test threw unexpected exception! " + e);
    }
  }

  @Test
  public void testPropertiesFile() {
    try {
      List<Path> paths = new ArrayList<Path>(Arrays.asList(Path.of(dataPath + "")));
      DupDetector dup = new DupDetector(1, paths);
      List<String> cppExtensions = new ArrayList<String>(Arrays.asList("cpp", "h"));
      assertThat("dup.getCppExtensions() expected " + cppExtensions + ", saw " + dup.getCppExtensions(), dup.getCppExtensions(), is(cppExtensions));
      assertThat("dup.getMaxSubstitutions() expected 10, saw " + dup.getMaxSubstitutions(), dup.getMaxSubstitutions(), is(10));
      assertThat("dup.getMinSequenceLength() expected 8, saw " + dup.getMinSequenceLength(), dup.getMinSequenceLength(), is(8));

      paths.clear();
      paths.add(Path.of(dataPath + "/p.ini"));
      paths.add(Path.of(dataPath + "/a.h"));
      paths.add(Path.of(dataPath + "/a.cpp"));
      paths.add(Path.of(dataPath + "/b"));
      DupDetector dup1 = new DupDetector(1, paths);
      cppExtensions = new ArrayList<String>(Arrays.asList("C", "cpp", "h", "hpp", "H"));
      assertThat("dup.getCppExtensions() expected " + cppExtensions + ", saw " + dup1.getCppExtensions(), dup1.getCppExtensions(), is(cppExtensions));
      assertThat("dup.getMaxSubstitutions() expected 20, saw " + dup1.getMaxSubstitutions(), dup1.getMaxSubstitutions(), is(20));
      assertThat("dup.getMinSequenceLength() expected 10, saw " + dup1.getMinSequenceLength(), dup1.getMinSequenceLength(), is(10));
    } catch (Exception e) {
      fail("Test threw unexpected exception! " + e);
    }
  }

  @Test
  public void testFilePaths() {
    try {
      List<Path> paths = new ArrayList<Path>() {{
        add(Path.of(dataPath + "/a.h"));
        add(Path.of(dataPath + "/a.cpp"));
        add(Path.of(dataPath + "/b"));
      }};
      DupDetector dup = new DupDetector(1, paths);
      Path[] expectedPaths = {
        Path.of(dataPath + "/a.h"),
        Path.of(dataPath + "/a.cpp"),
        Path.of(dataPath + "/b/c.h"),
        Path.of(dataPath + "/b/c.cpp"),
      };
      assertThat("dup.getFilePaths() expected " + expectedPaths + ", saw " + dup.getFilePaths(), dup.getFilePaths(), containsInAnyOrder(expectedPaths));
    } catch (Exception e) {
      fail("Test threw unexpected exception! " + e);
    }
  }

  @Test
  public void testSystem() {
    try {
      String[] args = {"5", dataPath + "/a.h", dataPath + "/a.cpp", dataPath + "/b"};
      int nSuggestions = Integer.parseInt(args[0]);
      List<Path> filePaths = DupDetector.toPaths(Arrays.copyOfRange(args, 1, args.length));
      
      DupDetector dup = new DupDetector(nSuggestions, filePaths);
      //Path[] expectedPaths = {
      //  Path.of(dataPath + "/a.h"),
      //  Path.of(dataPath + "/a.cpp"),
      //  Path.of(dataPath + "/b/c.h"),
      //  Path.of(dataPath + "/b/c.cpp"),
      //};
      
      //String separator = System.getProperty("file.separator");
      assertThat("dup.toString() expected " + dup.getFilePaths().toString() + ", saw " + dup.toString(), dup.toString(), is(dup.getFilePaths().toString()));
    } catch (Exception e) {
      fail("Test threw unexpected exception! " + e);
    }
  }
}
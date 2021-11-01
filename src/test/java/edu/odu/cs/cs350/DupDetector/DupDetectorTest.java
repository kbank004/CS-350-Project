package edu.odu.cs.cs350.DupDetector;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.*;

import java.sql.Array;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.nio.file.Path;

public class DupDetectorTest {
  @Test
  public void testEndsWithExtensions() {
    String[] fakeFiles = new String[]{"", "a", "b.", "c.ini"};
    List<String> realFiles = new ArrayList<String>();
    DupDetector dupDetector = new DupDetector();
    for (String ext : dupDetector.getCppExtensions()) {
      realFiles.add("file." + ext);
    }
    for (String file : fakeFiles) {
      assertFalse(dupDetector.endsWithExtensions(file));
    }
    for (String file : realFiles) {
      assertTrue(dupDetector.endsWithExtensions(file));
    }
  }

  @Test
  public void testMaxSuggestions() {
    DupDetector dupDetector = new DupDetector();

    dupDetector.setMaxSuggestions(0);
    assertThat(dupDetector.getMaxSuggestions(), is(1));

    dupDetector.setMaxSuggestions(1);
    assertThat(dupDetector.getMaxSuggestions(), is(1));

    dupDetector.setMaxSuggestions(11);
    assertThat(dupDetector.getMaxSuggestions(), is(11));
    
    dupDetector.setMaxSuggestions(-1);
    assertThat(dupDetector.getMaxSuggestions(), is(1));
  }

  @Test
  public void testFindFiles() {
    DupDetector dupDetector = new DupDetector();
    assertThat(dupDetector.getFilePaths(), is(empty()));

    String[] paths = new String[]{"src/test/data/a.h", "src/test/data/a.cpp", "src/test/data/b"};
    try {
      dupDetector.findFiles(paths);
    } catch (UnhandledException e) {
      System.err.println("Could not find files!");
      assertTrue(false);
    }

    assertThat(dupDetector.getFilePaths(), containsInAnyOrder(
      Path.of("src/test/data/a.h"), 
      Path.of("src/test/data/a.cpp"), 
      Path.of("src/test/data/b/c.h"), 
      Path.of("src/test/data/b/c.cpp")
    ));

    DupDetector dupDetector2 = new DupDetector();
    String[] paths2 = new String[]{"src/test/data/p.ini", "src/test/data/a.h", "src/test/data/a.cpp", "src/test/data/b/c.h", "src/test/data/b/c.cpp"};
    try {
      dupDetector2.findFiles(paths2);
    } catch (UnhandledException e) {
      System.err.println("Could not find files!");
      assertTrue(false);
    }
    assertThat(dupDetector.getFilePaths(), containsInAnyOrder(
      Path.of("src/test/data/a.h"), 
      Path.of("src/test/data/a.cpp"), 
      Path.of("src/test/data/b/c.h"), 
      Path.of("src/test/data/b/c.cpp")
    ));
  }

  @Test
  public void testPropertiesFile() {
    DupDetector dupDetector = new DupDetector();
    assertThat(dupDetector.getPropertiesFilePath().toFile().getName(), is(""));

    String[] paths = new String[]{"src/test/data/a.h", "src/test/data/a.cpp", "src/test/data/b"};
    try {
      dupDetector.findFiles(paths);
    } catch (UnhandledException e) {
      System.err.println("Could not find files!");
      assertTrue(false);
    }
    assertThat(dupDetector.getPropertiesFilePath().toFile().getName(), is(""));

    DupDetector dupDetector2 = new DupDetector();
    String[] paths2 = new String[]{"src/test/data/p.ini", "src/test/data/a.h", "src/test/data/a.cpp", "src/test/data/b"};
    try {
      dupDetector2.findFiles(paths2);
    } catch (UnhandledException e) {
      System.err.println("Could not find files!");
      assertTrue(false);
    }
    assertThat(dupDetector2.getPropertiesFilePath().toFile().getName(), is("p.ini"));
  }

  @Test
  public void testConstructor() {
    
  }
}

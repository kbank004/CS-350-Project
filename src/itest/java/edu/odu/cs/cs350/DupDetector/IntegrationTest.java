package edu.odu.cs.cs350.DupDetector;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.nio.file.Path;

public class IntegrationTest {
  private static final Path dataPath = Path.of("src/test/data");

  private final Path getDataPath(String path) {
    return dataPath.resolve(path);
  }
  
  @Test
  public final void testDupDetector() {
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
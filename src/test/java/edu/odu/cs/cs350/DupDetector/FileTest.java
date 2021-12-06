package edu.odu.cs.cs350.DupDetector;

import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileTest {
  @Test
  public final void testGetToken() {
    File f = new File(Path.of("/"), 20);
    assertThat(f.getNumTokens(), is(20));
  }

  @Test
  public final void testSetToken() {
    File f = new File(Path.of("/"), 21);
    f.setNumTokens(4444);
    assertThat(f.getNumTokens(), is(4444));
  }

  @Test
  public final void testGetFilePath() {
    Path dogPath = Path.of("/home/bob/Pictures/dog.jpg");
    File f = new File(dogPath, 1);
    assertThat(f.getFilePath(), is(dogPath));
  }

  @Test
  public final void testToString() {
    Path p = Path.of("src/test/data/a.cpp");

    File f = new File(p, 125);
    assertThat(f.toString(), is(p.toAbsolutePath() + ", 125"));
  }

  @Test
  public final void testCompareTo() {
    File p1 = new File(Path.of("/home/abby/billiards"), 1);
    File p2 = new File(Path.of("/home/abby/coconut"), 1);
    File p3 = new File(Path.of("/home/chris/alligator"), 1);
    File p4 = new File(Path.of("/home/chris/bounce"), 1);
    File p5 = new File(Path.of("/var/www/html"), 1);
    assertTrue(p1.compareTo(p2) < 0);
    assertTrue(p1.compareTo(p5) < 0);
    assertTrue(p2.compareTo(p3) < 0);
    assertTrue(p3.compareTo(p2) > 0);
    assertTrue(p4.compareTo(p2) > 0);
    assertTrue(p3.compareTo(p3) == 0);
  }
}

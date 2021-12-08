package edu.odu.cs.cs350.DupDetector;

import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.nio.file.Path;

/**
 * @author Banks
 */

public class OpportunityTest {
  @Test
  // WIP - Incomplete
  public final void test() {
    assertThat(true, is(true));
    File f = new File(Path.of("src/test/data/dupes.cpp"));
    Opportunity op = new Opportunity(f, 6);
    op.printPhrases();
  }
}
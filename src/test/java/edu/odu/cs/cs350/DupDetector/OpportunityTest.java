package edu.odu.cs.cs350.DupDetector;

import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Banks
 */

public class OpportunityTest {
  @Test
  public final void testConstructor(){
    Opportunity op = new Opportunity();
    assertThat(op.getNumOccurrances(), is(0));
  }
  
  @Test
  // WIP - Incomplete
  public final void testNumOccurrences() {
    File f = new File(Path.of("src/test/data/dupes.cpp"));
    List<File> fList = new ArrayList<>();
    fList.add(f);
    List<Opportunity> ops = Opportunity.findOpportunities(fList, 6);
    System.out.println(ops.get(0).toString());
    String str = "Opportunity 24, 12 tokens \\n /src/test/data/dupes.cpp";
    



  }
}
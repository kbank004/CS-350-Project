package edu.odu.cs.cs350.DupDetector;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;
import java.util.ArrayList;
import java.nio.file.Path;

public class DupDetectorTest {
  @Test
  public void testMaxSuggestions(){
    Path p1 = Path.of("./");
    ArrayList<Path> fList = new ArrayList<Path>();
    fList.add(p1);
        DupDetector dup = new DupDetector(50, fList);
        assertThat(dup.getMaxSuggestions(), is(50));

    }

    @Test
    public void testPropertiesFile(){
      ArrayList<Path> paths = new ArrayList<Path>();
      paths.add(Path.of("./properties.ini")); 

      DupDetector dup = new DupDetector(1, paths);
      assertThat(dup.getPropertiesPath().toString(), is(paths.get(0).toString()));

      paths.remove(0);
      paths.add(Path.of("/home/me/prop.ini"));
      paths.add(Path.of("C:\\Users\\Me\\test.c"));
      DupDetector dup1 = new DupDetector(1, paths);
      assertThat(dup1.getPropertiesPath(), is(paths.get(0)));
    }

    @Test
    public void testFilePaths(){
      ArrayList<Path> paths = new ArrayList<Path>();
      paths.add(Path.of("/home/me/mario.c"));
      paths.add(Path.of("/var/www/resources/mario.c"));
      paths.add(Path.of("C:\\Users\\me\\source\\unhelpfulprogram.cpp"));
      DupDetector dup = new DupDetector(1, paths);
      assertThat(dup.getFilePaths(), is(paths));
    }

    // Todo
    @Test
    public void testCppExtensions() {    
    }
    

  }
package edu.odu.cs.cs350.DupDetector;

import edu.odu.cs.cs350.DupDetector.UnhandledException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DupDetector {
  private int nSuggestions = 0;
  private Path propertiesFilePath = Path.of("");
  private List<Path> filePaths = new ArrayList<Path>();
  private List<String> cppExtensions = new ArrayList<String>(Arrays.asList("cpp","h"));
  public static void main(String[] args) {
    // If sufficient arguments are specified, then
    try {
      if (args.length >= 2) {
        DupDetector dupDetector = new DupDetector();
        dupDetector.run(args);
      } else {
        System.out.println("Usage: java -jar DupDetector.jar nSuggestions [ properties ] path1 [ path2 … ]");
      }
    } catch (UnhandledException e) {
      System.err.println(e.toString());
    }
  }

  public void run(String[] args) throws UnhandledException {
    // Get CLI args
    // Spit out file names
    try {
      nSuggestions = Integer.parseInt(args[0]);
    } catch (NumberFormatException e) {
      throw new UnhandledException(e.toString() + " (Invalid value for nSuggestions!)");
    }
    
    readFileArgs(args);
  }

  public void readFileArgs(String[] args) throws UnhandledException {
    Path arg1Path = Path.of(args[1]);
    // If 2nd arg is an ini, then set propertiesFile its value
    if (args[1].endsWith(".ini")) {
      propertiesFilePath = arg1Path;
      // Else it is a file or directory, so add to filePaths
    } else {
      filePaths.addAll(getPathsRecursively(arg1Path));
    }

    // Loop through each remaining arg and add the file paths from each one
    for (int i = 2; i < args.length; ++i) {
      filePaths.addAll(getPathsRecursively(Path.of(args[i])));
    }
  }

  public List<Path> getPathsRecursively(Path filePath) throws UnhandledException {
    if (!filePath.toFile().exists()) {
      throw new UnhandledException("File or directory does not exist: " + filePath.toString());
    }

    List<Path> paths = new ArrayList<Path>();
    try { // Source: https://stackoverflow.com/questions/2056221/recursively-list-files-in-java/69489309#69489309
      if (filePath.toFile().isDirectory()) {
        // Walk through file tree and collect all files with correct extensions into a list
        try (Stream<Path> stream = Files.walk(filePath)) {
          paths = stream.parallel().filter(Files::isRegularFile)
                        .filter(path -> endsWithExtensions(path.getFileName().toString()))
                        .collect(Collectors.toList());
        }

      } else if (endsWithExtensions(filePath.getFileName().toString())) {
        // if not directory and has proper extensions, just return filePath itself
        paths.add(filePath);
      }
    } catch (IOException e) {
      throw new UnhandledException(e.toString());
    }
    return paths;
  }

  public boolean endsWithExtensions(String str) {
    return cppExtensions.stream().anyMatch(e -> str.endsWith(e));
    //return ext.contains(str.substring(str.lastIndexOf(".") + 1));
  }

  public DupDetector() {} 
}
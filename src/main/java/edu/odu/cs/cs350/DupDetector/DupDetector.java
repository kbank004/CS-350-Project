package edu.odu.cs.cs350.DupDetector;

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
        try {
          dupDetector.setMaxSuggestions(Integer.parseInt(args[0]));
        } catch (NumberFormatException e) {
          throw new UnhandledException("Invalid value for nSuggestions!");
        }
        dupDetector.findFiles(Arrays.copyOfRange(args, 1, args.length));
        System.out.println(dupDetector.toString());
      } else {
        System.out.println("Usage: java -jar DupDetector.jar nSuggestions [ properties ] path1 [ path2 … ]");
      }
    } catch (UnhandledException e) {
      System.err.println(e.toString());
    }
  }

  public DupDetector() {
    // Get CLI args
    // Spit out file names
  }

  public void setMaxSuggestions(int n) {
    nSuggestions = Math.max(1, n);
  }
  

  public int getMaxSuggestions() {
    return nSuggestions;

  }

  public Path getPropertiesFilePath() {
    return propertiesFilePath;
  }
  
  public List<Path> getFilePaths() {
    return filePaths;
  }
  
  public void setCppExtensions(List<String> ext) {
    cppExtensions = ext;
  }
  
  public List<String> getCppExtensions() {
    return cppExtensions;
  }

  public void findFiles(String[] paths) throws UnhandledException {
    Path arg1Path = Path.of(paths[0]);
    // If 2nd arg is an ini, then set propertiesFile its value
    if (paths[0].endsWith(".ini")) {
      propertiesFilePath = arg1Path;
      // Else it is a file or directory, so add to filePaths
    } else {
      filePaths.addAll(getPathsRecursively(arg1Path));
    }

    // Loop through each remaining arg and add the file paths from each one
    for (int i = 1; i < paths.length; ++i) {
      filePaths.addAll(getPathsRecursively(Path.of(paths[i])));
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

  public String toString() {
    return filePaths.toString();
  }
}
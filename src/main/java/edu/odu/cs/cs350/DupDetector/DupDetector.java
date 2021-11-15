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
  // --- 
  public static void main(String[] args) {
    if (args.length < 2) {
      System.out.println("Usage: java -jar DupDetector.jar nSuggestions [properties filepath] path1 [path2 …]");
      System.exit(-1);
    }

    try {
      // Process input
      int nSuggestions = Integer.parseInt(args[0]);
      List<Path> filePaths = toPaths(Arrays.copyOfRange(args, 1, args.length));//findFiles(Arrays.copyOfRange(args, 1, args.length));
      
      DupDetector dupDetector = new DupDetector(nSuggestions, filePaths);
      System.out.println(dupDetector.toString());
  
      System.exit(0);
    } catch (Exception e) {
      System.err.println(e);

      System.exit(-1);
    }
  }

  public static List<Path> toPaths(String[] paths) {
    List<Path> files = new ArrayList<Path>();
    for (String pathStr : paths) {
      files.add(Path.of(pathStr));
    }
    return files;
  }

  private int maxSuggestions = 0;
  private Path propertiesPath = Path.of("");
  private List<Path> filePaths = new ArrayList<Path>();
  private List<String> cppExtensions = new ArrayList<String>(Arrays.asList("cpp", "h"));

  public DupDetector(int nSuggestions, List<Path> paths) throws Exception {
    maxSuggestions = Math.max(1, nSuggestions);
    
    if (tryParsePropertyFile(paths)) { // If parsed property file, skip first path in list for processing (it was the properties file)
      findFiles(paths.subList(1, paths.size()));
    } else {
      findFiles(paths);
    }
  }

  public int getMaxSuggestions() {
    return maxSuggestions;
  }

  public List<String> getCppExtensions() {
    return cppExtensions;
  }

  public Path getPropertiesPath() {
    return propertiesPath;
  }

  public List<Path> getFilePaths() {
    return filePaths;
  }

  public boolean tryParsePropertyFile(List<Path> paths) throws Exception {
    Path propertiesFilePath = paths.get(0);
    if (propertiesFilePath.toString().endsWith(".ini")) {
      propertiesPath = propertiesFilePath;
      //cppExtensions = ;
      return true;
    }
    return false;
  }

  public void findFiles(List<Path> paths) throws Exception {
    // Loop through each arg and add the file paths from each one
    for (Path path : paths) {
      filePaths.addAll(getPathsRecursively(path));
    }
  }

  public List<Path> getPathsRecursively(Path filePath) throws Exception {
    if (!filePath.toFile().exists()) {
      throw new Exception("File or directory does not exist: " + filePath.toString());
    }

    List<Path> paths = new ArrayList<Path>();
    try { // Source:
          // https://stackoverflow.com/questions/2056221/recursively-list-files-in-java/69489309#69489309
      // if (filePath.toFile().isDirectory()) {
      //   // Walk through file tree and collect all files with correct extensions into a
      //   // list
      //   try (Stream<Path> stream = Files.walk(filePath)) {
      //     paths = stream.parallel().filter(Files::isRegularFile)
      //         .filter(path -> endsWithExtensions(path.getFileName().toString())).collect(Collectors.toList());
      //   }

      // } else if (endsWithExtensions(filePath.getFileName().toString())) {
      //   // if not directory and has proper extensions, just return filePath itself
      //   paths.add(filePath);
      // }
      
      try (Stream<Path> stream = Files.walk(filePath)) {
        paths = stream.parallel().filter(Files::isRegularFile)
          .filter(path -> endsWithExtensions(path.getFileName().toString())).collect(Collectors.toList());
      }
    } catch (IOException e) {
      throw new Exception(e.toString());
    }
    return paths;
  }

  public boolean endsWithExtensions(String str) {
    return cppExtensions.stream().anyMatch(e -> str.endsWith(e));
  }

  public String toString() {
    return filePaths.toString();
  }
}
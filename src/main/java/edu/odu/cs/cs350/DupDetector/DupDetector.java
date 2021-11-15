package edu.odu.cs.cs350.DupDetector;

import java.io.IOException;
import java.util.Properties;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DupDetector {
  // --- 
  public static void main(String[] args) {
    if (args.length < 2) {
      System.out.println("Usage: java -jar DupDetector.jar nSuggestions [properties filepath] path1 [path2 …]");
      System.exit(-1);
    }

    // Process input
    int nSuggestions = Integer.parseInt(args[0]);
    // Creates an ArrayList of Files given the file paths
    List<File> filesList = toPaths(Arrays.copyOfRange(args, 1, args.length));
   
  
  
    
    DupDetector dupDetector = new DupDetector(nSuggestions, filesList);
    System.out.println(dupDetector.toString());

    System.exit(0);
  }

  public static List<File> toPaths(String[] paths) {
    ArrayList<File> files = new ArrayList<File>();
    for (String pathStr : paths) {
      File f = new File(Path.of(pathStr), 0);
      files.add(f);
    }
    return files;
  }

  private final int maxSuggestions;
  private List<String> cppExtensions = new ArrayList<String>(Arrays.asList("cpp", "h"));
  private Optional<Path> propertiesPath = Optional.empty();
  private List<File> filesList = new ArrayList<File>();

  public DupDetector(int nSuggestions, List<Path> paths) {
    maxSuggestions = Math.max(1, nSuggestions);

    try {
      tryParsePropertyFile(paths);
      if (propertiesPath.isPresent()) { // If parsed property file, skip first path in list for processing (it was the properties file)
        findFiles(paths.subList(1, paths.size()));
      } else {
        findFiles(paths);
      }
    } catch (Exception e) {
      System.err.println(e);

      System.exit(-1);
    }
  }

  public int getMaxSuggestions() {
    return maxSuggestions;
  }

  public List<String> getCppExtensions() {
    return cppExtensions;
  }

  public Path getPropertiesPath() {
    return propertiesPath.get();
  }

  public List<File> getFileList() {
    return filesList;
  }

  public void tryParsePropertyFile(List<Path> paths) {
    Path propertiesFilePath = paths.get(0);
    if (propertiesFilePath.toString().endsWith(".ini")) {
      propertiesPath = Optional.of(propertiesFilePath);
      //cppExtensions = ;
    }
  }

  public void searchPaths(List<Path> paths) throws Exception {
    // Loop through each arg and add the file paths from each one
    for (Path path : paths) {
      filesList.addAll(getFilesRecursively(path));
    }
  }

  public List<File> getFilesRecursively(Path filePath) throws Exception {
    if (!filePath.toFile().exists()) {
      throw new Exception("File or directory does not exist: " + filePath.toString());
    }

    List<Path> paths = new ArrayList<Path>();
    try { // Source: https://stackoverflow.com/questions/2056221/recursively-list-files-in-java/69489309#69489309
      try (Stream<Path> stream = Files.walk(filePath)) {
        paths = stream.parallel().filter(Files::isRegularFile)
          .filter(path -> endsWithExtensions(path.getFileName().toString())).collect(Collectors.toList());
      }
    } catch (IOException e) {
      throw new Exception(e.toString());
    }
    List<File> fList = new ArrayList<File>();
    for (File f : fList) {
      fList.add(e)
    }
    return paths;
  }

  public boolean endsWithExtensions(String str) {
    return cppExtensions.stream().anyMatch(e -> str.endsWith(e));
  }

  public void Output() {
    System.out.println("Files scanned:\n");;
    for (Path f : filesList) {
      System.out.println("    " + fPath.toString() + "\n");
    }
  }
}
package edu.odu.cs.cs350.DupDetector;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DupDetector {
  // --------------------- Main --------------------- //
  
  public static void main(String[] args) {
    // Print help message if not enough args
    if (args.length < 2) {
      System.out.println("Usage: java -jar DupDetector.jar nSuggestions [properties filepath] path1 [path2 …]");
      System.exit(-1);
    }

    try {
      // Process input
      int nSuggestions = Integer.parseInt(args[0]);
      List<Path> filePaths = toPaths(Arrays.copyOfRange(args, 1, args.length));
      
      // Run DupDetector
      DupDetector dupDetector = new DupDetector(nSuggestions, filePaths);
      System.out.println(dupDetector.getOutput());
      
      System.exit(0);
    } catch (Exception e) {
      System.err.println(e);

      System.exit(-1);
    }
  }

  public static List<Path> toPaths(String[] argsArray) { // Is there a shorthand for this?
    return Arrays.stream(argsArray).map(arg -> Path.of(arg)).collect(Collectors.toList());
  }

  // --------------------- DupDetector --------------------- //

  private final int maxSuggestions;
  private Optional<Properties> properties = Optional.empty();
  private List<File> files = new ArrayList<File>();

  private final String defaultCppExtensions = "cpp,h";
  private final int defaultMaxSubstitutions = 10;
  private final int defaultMinSequenceLength = 8;

  public DupDetector(int nSuggestions, List<Path> paths) throws FileNotFoundException {
    maxSuggestions = Math.max(1, nSuggestions);

    tryParsePropertyFile(paths);
    if (properties.isPresent()) { // If parsed property file, skip first path in list for processing (it was the properties file)
      searchPaths(paths.subList(1, paths.size()));
    } else {
      searchPaths(paths);
    }
  }

  /**
   * Get maximum number of suggested refactorings to be printed
   * @return {int} max num of suggestions
   */
  public int getMaxSuggestions() {
    return maxSuggestions;
  }

  private String getProperty(Property property) {
    switch (property) {
      case CPP_EXTENSIONS:
        if (properties.isPresent()) {
          return properties.get().getProperty("CppExtensions", defaultCppExtensions);
        } else {
          return defaultCppExtensions;
        }
      case MAX_SUBSTITUTIONS:
        if (properties.isPresent()) {
          return properties.get().getProperty("MaxSubstitutions", Integer.toString(defaultMaxSubstitutions));
        } else {
          return Integer.toString(defaultMaxSubstitutions);
        }
      case MIN_SEQUENCE_LENGTH:
        if (properties.isPresent()) {
          return properties.get().getProperty("MinSequenceLength", Integer.toString(defaultMinSequenceLength));
        } else {
          return Integer.toString(defaultMinSequenceLength);
        }
      default:
        return null;
    }
  }

  /**
  * Returns a list of acceptable file extensions
  * @return {List<String>} list of accepted extensions
  */
  public List<String> getCppExtensions() {
    return Arrays.asList(getProperty(Property.CPP_EXTENSIONS).split(",", 0));
  }

  /**
  * Returns the maximum number of substitutions to be printed
  * @return {int} max num of substitutions
  */
  public int getMaxSubstitutions() {
    return Integer.parseInt(getProperty(Property.MAX_SUBSTITUTIONS));
  }

  /**
  * Returns the minimum allowed length of a token for it to be printed
  * @return {int} min length of token
  */
  public int getMinSequenceLength() {
    return Integer.parseInt(getProperty(Property.MIN_SEQUENCE_LENGTH));
  }

  /**
  * Returns a List of Files (to eventually be read and parsed)
  * @return {List<File>} list of files 
  */
  public List<File> getFiles() {
    return files;
  }

  /**
  * Returns a list of File Paths
  * @return {List<Path>} list of File Paths
  */
  public List<Path> getFilePaths() {
    return files.stream().map(File::getFilePath).collect(Collectors.toList());
  }

  /**
  * Returns a String containing DupDetector's output
  * @return {String} string of output
  */
  public String getOutput() {
    StringBuffer buffer = new StringBuffer("Files scanned:\n");
    for (File file : files) {
      buffer.append("    " + file.toString() + "\n");
    }
    return buffer.toString();
  }

  private void tryParsePropertyFile(List<Path> paths) {
    Path propertiesFilePath = paths.get(0);
    if (propertiesFilePath.toString().endsWith(".ini")) {
      try (InputStream stream = new FileInputStream(propertiesFilePath.toString())) {
        properties = Optional.of(new Properties());
        properties.get().load(stream);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private void searchPaths(List<Path> paths) throws FileNotFoundException {
    // Loop through each arg and add the file paths from each one
    for (Path path : paths) {
      files.addAll(getFilesRecursively(path));
    }
    Collections.sort(files); // Sort alphabetically
  }

  private List<File> getFilesRecursively(Path path) throws FileNotFoundException {
    if (!path.toFile().exists()) {
      throw new FileNotFoundException("File or directory does not exist: " + path);
    }

    List<Path> paths = new ArrayList<Path>();
    try (Stream<Path> stream = Files.walk(path)) { // Source: https://stackoverflow.com/questions/2056221/recursively-list-files-in-java/69489309#69489309
      paths = stream.parallel().filter(Files::isRegularFile)
                               .filter(p -> endsWithExtensions(p.getFileName().toString()))
                               .collect(Collectors.toList());
    } catch (IOException e) {
      e.printStackTrace();
    }

    return paths.stream().map(p -> new File(p, 0)).collect(Collectors.toList());
  }


  private boolean endsWithExtensions(String str) {
    return getCppExtensions().stream().anyMatch(e -> str.endsWith(e));
  }
}
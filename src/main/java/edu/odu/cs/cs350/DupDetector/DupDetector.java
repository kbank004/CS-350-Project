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

/**
 * A program for examining C++ code and making recommendations for near-duplicate code blocks that can be refactored to reduce the overall size and complexity of the C++ program
 * https://www.cs.odu.edu/~zeil/cs350/latest/Protected/refactoring/index.html
 * @author Lugo
 * @author Banks
 */
public class DupDetector {
  // --------------------- Main --------------------- //

  /**
   * Main program takes at least two command-line parameters.
   * 
   * nSuggestions - Maximum number of suggested refactorings to be printed
   * [propertiesFilePath] - Path to a properties file with extension .ini, containing properties of format propertyName = propertyValue
   * path1 - Path to file or directory containing C++ source code
   * [path2...] - Additional paths to files or directories containing C++ source code
   * 
   * @param args nSuggestions [propertiesFilePath] path1 [path2...]
   */
  public static void main(final String[] args) {
    // Print help message if not enough args
    if (args.length < 2) {
      System.err.println("Usage: java -jar DupDetector.jar nSuggestions [properties filepath] path1 [path2 …]");
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

  /**
   * Convert String array of paths into List of Path objects
   * @param argsArray String array of paths
   * @return list of paths
   */
  public static List<Path> toPaths(String[] argsArray) { // Is there a shorthand for this?
    return Arrays.stream(argsArray).map(arg -> Path.of(arg)).collect(Collectors.toList());
  }

  // --------------------- DupDetector --------------------- //

  private final int maxSuggestions;
  private Optional<Properties> properties = Optional.empty();
  private List<File> files = new ArrayList<File>();

  private final String defaultCppExtensions = "cpp,h";
  private final int defaultMinSequenceLength = 8;
  private final int defaultMaxSubstitutions = 10;

  /**
  * Constructor
  * @param nSuggestions Maximum number of suggestions
  * @param paths List of Paths to read (including properties file path, if included) 
  * @throws FileNotFoundException throws exception if one of the given paths is invalid
  */
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
   * @return max num of suggestions
   */
  public final int getMaxSuggestions() {
    return maxSuggestions;
  }

  private final String getProperty(Property property) {
    switch (property) {
      case CPP_EXTENSIONS:
        if (properties.isPresent()) {
          return properties.get().getProperty("CppExtensions", defaultCppExtensions);
        } else {
          return defaultCppExtensions;
        }
      case MIN_SEQUENCE_LENGTH:
        if (properties.isPresent()) {
          return properties.get().getProperty("MinSequenceLength", Integer.toString(defaultMinSequenceLength));
        } else {
          return Integer.toString(defaultMinSequenceLength);
        }
      case MAX_SUBSTITUTIONS:
        if (properties.isPresent()) {
          return properties.get().getProperty("MaxSubstitutions", Integer.toString(defaultMaxSubstitutions));
        } else {
          return Integer.toString(defaultMaxSubstitutions);
        }
      default:
        return null;
    }
  }

  /**
  * Returns a list of acceptable file extensions. Default property value: cpp,h
  * @return list of accepted extensions
  */
  public final List<String> getCppExtensions() {
    return Arrays.asList(getProperty(Property.CPP_EXTENSIONS).split(",", 0));
  }

  /**
  * Returns the minimum allowed length of a token sequence for it to be printed. Default property value: 8
  * @return min length of token sequence
  */
  public final int getMinSequenceLength() {
    return Integer.parseInt(getProperty(Property.MIN_SEQUENCE_LENGTH));
  }

  /**
  * Returns the maximum number of substitutions to be printed. Default property value: 10
  * @return max num of substitutions
  */
  public final int getMaxSubstitutions() {
    return Integer.parseInt(getProperty(Property.MAX_SUBSTITUTIONS));
  }

  /**
  * Returns a List of Files (to eventually be read and parsed)
  * @return list of files 
  */
  public final List<File> getFiles() {
    return files;
  }

  /**
  * Returns a list of File Paths
  * @return list of File Paths
  */
  public final List<Path> getFilePaths() {
    return files.stream().map(File::getFilePath).collect(Collectors.toList());
  }

  /**
   * Returns a String containing DupDetector's output
   * @return string of output
   */
  public final String getOutput() {
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
                               //.peek(p -> System.out.println(p))
                               .collect(Collectors.toList());
    } catch (IOException e) {
      System.err.println(e);
    }

    return paths.stream().map(p -> new File(p)).collect(Collectors.toList());
  }

  private boolean endsWithExtensions(String str) {
    return getCppExtensions().stream().anyMatch(e -> str.endsWith(e));
  }
}
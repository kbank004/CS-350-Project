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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DupDetector {
  // --------------------- Main --------------------- //
  public static void main(String[] args) {
    if (args.length < 2) {
      System.out.println("Usage: java -jar DupDetector.jar nSuggestions [properties filepath] path1 [path2 …]");
      System.exit(-1);
    }

    try {
      // Process input
      int nSuggestions = Integer.parseInt(args[0]);
      List<Path> filePaths = toPaths(Arrays.copyOfRange(args, 1, args.length));
      
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

  // --------------------- DupDetector --------------------- //

  private final int maxSuggestions;
  private Optional<Properties> properties = Optional.empty();
  private List<Path> filePaths = new ArrayList<Path>();

  private final String defaultCppExtensions = "cpp,h";
  private final int defaultMaxSubstitutions = 10;
  private final int defaultMinSequenceLength = 8;

  public DupDetector(int nSuggestions, List<Path> paths) throws FileNotFoundException {
    maxSuggestions = Math.max(1, nSuggestions);

    tryParsePropertyFile(paths);
    if (properties.isPresent()) { // If parsed property file, skip first path in list for processing (it was the properties file)
      findFiles(paths.subList(1, paths.size()));
    } else {
      findFiles(paths);
    }
  }

  public int getMaxSuggestions() {
    return maxSuggestions;
  }

  public String getProperty(Property property) {
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

  public List<String> getCppExtensions() {
    return Arrays.asList(getProperty(Property.CPP_EXTENSIONS).split(",", 0));
  }

  public int getMaxSubstitutions() {
    return Integer.parseInt(getProperty(Property.MAX_SUBSTITUTIONS));
  }

  public int getMinSequenceLength() {
    return Integer.parseInt(getProperty(Property.MIN_SEQUENCE_LENGTH));
  }

  public List<Path> getFilePaths() {
    return filePaths;
  }

  public void tryParsePropertyFile(List<Path> paths) {
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

  public void findFiles(List<Path> paths) throws FileNotFoundException {
    // Loop through each arg and add the file paths from each one
    for (Path path : paths) {
      filePaths.addAll(getPathsRecursively(path));
    }
  }

  public List<Path> getPathsRecursively(Path filePath) throws FileNotFoundException {
    if (!filePath.toFile().exists()) {
      throw new FileNotFoundException("File or directory does not exist: " + filePath);
    }

    List<Path> paths = new ArrayList<Path>();
    try (Stream<Path> stream = Files.walk(filePath)) { // Source: https://stackoverflow.com/questions/2056221/recursively-list-files-in-java/69489309#69489309
      paths = stream.parallel().filter(Files::isRegularFile)
                               .filter(path -> endsWithExtensions(path.getFileName().toString()))
                               .collect(Collectors.toList());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return paths;
  }

  public boolean endsWithExtensions(String str) {
    return getCppExtensions().stream().anyMatch(e -> str.endsWith(e));
  }

  public String toString() {
    return filePaths.toString();
  }
}
package edu.odu.cs.cs350.DupDetector;

import java.io.Reader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
/**
 * Represents a single file with a path and a number of tokens contained inside it
 * @author Banks
 * @author Lugo
 * 
 */
public class File implements Comparable<File> {
  private final Path filePath;
  private TokenStream tokens;

  /**
   * Constructor
   * @param path Path of this file
   */
  public File(final Path path) {
    filePath = path;
    scan();
  }

  /**
   * Constructor
   * @param path Path of this file
   */
  public File(final String path) {
    filePath = Path.of(path);
    scan();
  }

  /**
   * Return the number of tokens in the file
   * @return number of tokens
   */
  public final int getNumTokens() {
    return tokens.toList().size();
  }

  /**
   * Return the tokens in the file
   * @return list of tokens
   */
  public final TokenStream getTokens() {
    return tokens;
  }

  /**
   * Return the Path for the file
   * @return path of file
   */
  public final Path getFilePath() {
    return filePath;
  }

  /**
   * Output the filepath and the number of tokens in the file
   */
  public final String toString() {
    return filePath.toAbsolutePath().toString() + ", " + getNumTokens();
  }

  /**
   *  Lexigraphically compares this file with another one
   * @param f File to compare to
   */
  public final int compareTo(File f) {
    return filePath.compareTo(f.filePath);
  }

  private void scan() {
    try (Reader fileReader = new FileReader(filePath.toString())) {
      tokens = new TokenStream(fileReader);
    } catch (IOException e) {
      // it should never get here because if it got to this part of the program, the file should be valid
      System.err.println(e);
    }
  }
}
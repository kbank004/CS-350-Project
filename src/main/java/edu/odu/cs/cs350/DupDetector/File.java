package edu.odu.cs.cs350.DupDetector;

import java.io.Reader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.ArrayList;
/**
 * Represents a single file with a path and a number of tokens contained inside it
 * @author Banks
 */
public class File implements Comparable<File> {
  private Path filePath;
  private List<Token> tokens = new ArrayList<Token>();

  /**
   * Constructor
   * @param path Path of this tile
   * @param tokenCount Number of tokens in this file
   */
  public File(Path path) {
    filePath = path;
    scan();
  }

  /**
   * Constructor
   * @param path Path of this tile
   * @param tokenCount Number of tokens in this file
   */
  public File(String path) {
    filePath = Path.of(path);
    scan();
  }

  /**
   * Return the number of tokens in the file
   * @return number of tokens
   */
  public int getNumTokens() {
    return tokens.size();
  }

  /**
   * Return the tokens in the file
   * @return list of tokens
   */
  public List<Token> getTokens() {
    return tokens;
  }

  /**
   * Return the Path for the file
   * @return path of file
   */
  public Path getFilePath() {
    return filePath;
  }

  /**
   * Output the filepath and the number of tokens in the file
   */
  public String toString() {
    return filePath.toAbsolutePath().toString() + ", " + Integer.toString(tokens.size());
  }

  /**
   *  Lexigraphically compares this file with another one
   * @param f File to compare to
   */
  public int compareTo(File f) {
    return filePath.compareTo(f.filePath);
  }

  private void scan() {
    try (Reader fileReader = new FileReader(filePath.toString())) {
      TokenStream tokenStream = new TokenStream(fileReader);
      for (Token t : tokenStream) {
        tokens.add(t);
      }
    } catch (IOException e) {
      // it should never get here because if it got to this part of the program, the file should be valid
      System.err.println(e);
    }
  }
}
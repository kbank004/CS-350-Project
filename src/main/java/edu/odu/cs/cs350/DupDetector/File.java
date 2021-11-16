package edu.odu.cs.cs350.DupDetector;

import java.nio.file.Path;
/**
 * Represents a single file with a path and a number of tokens contained inside it
 * @author Banks
 */
public class File implements Comparable<File> {
  private Path filePath;
  private int numTokens;

/**
 * Constructor
 * @param p Path of this tile
 * @param tokenCount Number of tokens in this file
 */
  public File(Path p, int tokenCount) {
    filePath = p;
    numTokens = tokenCount;
  }

  /**
   * Set the number of tokens in this file
   * @param n number of tokens
   */
  public void setNumTokens(int n) {
    numTokens = n;
  }

  /**
   * Return the number of tokens in the file
   * @return number of tokens
   */
  public int getNumTokens() {
    return numTokens;
  }

  /**
   * Return the Path for the file
   * @return path of file
   */
  public Path getFilePath() {
    return filePath;
  }

  /**
   * Output the filepath and the number of tokens int he file
   */
  public String toString() {
    return filePath.toAbsolutePath().toString() + ", " + Integer.toString(numTokens);
  }

  /**
   *  Lexigraphically compares this file with another one
   * @param f File to compare to
   */
  public int compareTo(File f) {
    return filePath.compareTo(f.filePath);
  }
}
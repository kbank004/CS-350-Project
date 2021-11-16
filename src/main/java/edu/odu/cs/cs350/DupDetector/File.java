package edu.odu.cs.cs350.DupDetector;

import java.nio.file.Path;
/**
 * Class used for storing information about a file relevant to DupDetector
 */
public class File implements Comparable<File> {
  private Path filePath;
  private int numTokens;

/**
 * Constructor
 * @param p
 * @param tokenCount
 */
  public File(Path p, int tokenCount) {
    filePath = p;
    numTokens = tokenCount;
  }

  /**
   * Set the number of tokens in this file
   * @param n
   */
  public void setNumTokens(int n) {
    numTokens = n;
  }

  /**
   * Return the number of tokens in the file
   * @return
   */
  public int getNumTokens() {
    return numTokens;
  }

  /**
   * Return the Path for the file
   * @return
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
   * Compares this file with another file
   * @param f File to compare to
   */
  public int compareTo(File f) {
    return filePath.compareTo(f.filePath);
  }
}
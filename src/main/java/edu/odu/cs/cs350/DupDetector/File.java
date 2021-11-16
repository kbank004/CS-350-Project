package edu.odu.cs.cs350.DupDetector;

import java.nio.file.Path;

/**
 * Represents a single file with a path and a number of tokens contained inside it
 * @author banks
 */
public class File implements Comparable<File> {
  private Path filePath;
  private int numTokens;

  public File(Path p, int tokenCount) {
    filePath = p;
    numTokens = tokenCount;
  }

  public void setNumTokens(int n) {
    numTokens = n;
  }

  public int getNumTokens() {
    return numTokens;
  }

  public Path getFilePath() {
    return filePath;
  }

  public String toString() {
    return filePath.toAbsolutePath().toString() + ", " + Integer.toString(numTokens);
  }

  public int compareTo(File f) {
    return filePath.compareTo(f.filePath);
  }
}
package edu.odu.cs.cs350.DupDetector;

import java.nio.file.Path;

public class File {
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
}
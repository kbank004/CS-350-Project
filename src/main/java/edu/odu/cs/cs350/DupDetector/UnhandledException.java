package edu.odu.cs.cs350.DupDetector;

import java.lang.Exception;

// Allows for exiting the program with an error message
public class UnhandledException extends Exception {
  public UnhandledException(String message) {
    super(message);
  }
}
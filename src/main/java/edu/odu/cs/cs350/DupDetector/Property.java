package edu.odu.cs.cs350.DupDetector;

/**
 * Enum representing the different available properties in the properties file
 * @author Lugo
 */
public enum Property {
  /**
   * Comma-separated list of file extensions that will be accepted by the program. Default property value: cpp,h
   */
  CPP_EXTENSIONS,

  /**
   * The minimum allowed length of a token sequence for it to be printed. Default property value: 8
   */
  MIN_SEQUENCE_LENGTH,
  
  /**
   * The maximum number of substitutions to be printed. Default property value: 10
   */
  MAX_SUBSTITUTIONS,
}

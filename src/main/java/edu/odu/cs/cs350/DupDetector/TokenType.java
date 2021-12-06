package edu.odu.cs.cs350.DupDetector;



/**
 * All possible token types.
 * 
 * @author zeil
 *
 */
public enum TokenType {
  IDENTIFIER,
  KEYWORD,
  SYMBOL,
  INTEGER_LIT,
  FLOAT_LIT,
  STRING_LIT,
  CHAR_LIT,
  BOOL_LIT,
  // COMMENT, // ignore
  // PREPROCESSOR, // ignore
  // WHITE_SPACE, // ignore
  EOF
};

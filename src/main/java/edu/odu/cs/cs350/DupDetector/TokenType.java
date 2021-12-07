package edu.odu.cs.cs350.DupDetector;



/**
 * All possible token types.
 * 
 * @author Zeil
 * @author Lugo
 * 
 */
public enum TokenType {
  IDENTIFIER(0),
  KEYWORD(1),
  SYMBOL(2),
  INTEGER_LIT(3),
  FLOAT_LIT(4),
  STRING_LIT(5),
  CHAR_LIT(6),
  BOOL_LIT(7),
  // COMMENT, // ignore
  // PREPROCESSOR, // ignore
  // WHITE_SPACE, // ignore
  EOF(-1);

  public final int value;

  private TokenType(int value_) {
    value = value_;
  }
}

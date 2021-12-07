package edu.odu.cs.cs350.DupDetector;

import java.util.Comparator;

/**
 * Token class, stores the token type, the lexeme (the actual character string)
 * ant the location (column and line numbers).
 * 
 * 
 * @author Zeil
 * @author Lugo
 * 
 */
public class Token implements Comparable<Token> {

  /**
   * What kind of token is this?
   */
  private final TokenType type_;

  /**
   * For variables & literals, the original lexical string.
   */
  private final String lexeme_;
  
  /**
   * Where did we find this within the file?
   */
  private final int lineNumber_;
  
  /**
   * Where did we find this within the file?
   */
  private final int columnNumber_;
  

  /**
   * Create a basic token with no explicit lexeme.
   * @param type the kind of token
   * @param line line number where token was found
   * @param column column number where token begins
   */
  public Token(final TokenType type, final int line, final int column) {
    type_ = type;
    lexeme_ = "";
    lineNumber_ = line;
    columnNumber_ = column;
  }

  /**
   * Create a token.
   * @param type what kind of token
   * @param line line number where token was found
   * @param column column number where token begins
   * @param lexeme the original lexeme
   */
  public Token(final TokenType type, final int line, final int column, final String lexeme) {
    type_ = type;
    lexeme_ = lexeme;
    lineNumber_ = line;
    columnNumber_ = column;
  }

  /**
   * Representation of the token for debugging purposes.
   */
  @Override
  public final String toString() {
    if (getLexeme().length() > 0) {
      return getKind() + ":" + getLexeme() + "(" + getLineNumber() + "," + getColumnNumber() + ")";
    } else {
      return getKind().toString() + "(" + getLineNumber() + getColumnNumber() + ")";
    }
  }

  /**
   * What kind of token is this?
   * @return the kind
   */
  public final TokenType getKind() {
    return type_;
  }


  /**
   * What is the character string (lexeme) associated with this token?
   * @return the lexeme
   */
  public final String getLexeme() {
    return lexeme_;
  }

  /**
   * @return the lineNumber
   */
  public int getLineNumber() {
    return lineNumber_;
  }


  /**
   * @return the columnNumber
   */
  public int getColumnNumber() {
    return columnNumber_;
  }

  public int compareTo(Token t) {
    return Comparator.comparing(Token::getKind)
                     .thenComparing(Token::getLexeme)
                     //.thenComparing(Token::getLineNumber)
                     //.thenComparing(Token::getColumnNumber)
                     .compare(this, t);
  }

  @Override // https://www.artima.com/articles/how-to-write-an-equality-method-in-java
  public boolean equals(Object other) {
    if (other instanceof Token) {
      Token that = (Token) other;
      return 0 == Comparator.comparing(Token::getKind)
                            .thenComparing(Token::getLexeme)
                            //.thenComparing(Token::getLineNumber)
                            //.thenComparing(Token::getColumnNumber)
                            .compare(this, that);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return (41 * (41 + getKind().hashCode()) + getLexeme().hashCode());
  }
}

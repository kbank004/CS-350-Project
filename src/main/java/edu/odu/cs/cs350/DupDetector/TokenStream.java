package edu.odu.cs.cs350.DupDetector;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * @author Zeil
 * @author Lugo
 *
 */
public class TokenStream implements Iterable<Token> {
  /**
   * The list of tokens obtained from the actual scanner.
   */
  private List<Token> tokens;
  private File sourceFile;
  
  /**
   * Create a TokenStream from an input source.
   * @param input input source for this stream
   */
  public TokenStream(final Reader input) {
    tokens = new LinkedList<Token>();
    GeneratedScanner scanner = new GeneratedScanner(input);
    try {
      Token token = scanner.yylex();
      while (token != null && token.getKind() != TokenType.EOF) {
        tokens.add (token);
        token = scanner.yylex();
      }
    } catch (IOException ex) {
      // Not necessarily a problem, depending on the input source
      System.err.println(ex);
    }
  }

  public TokenStream(final List<Token> tokens_, final File sourceFile_) {
    tokens = tokens_;
    sourceFile = sourceFile_;
  }

  /**
   * Return the list of tokens
   * @return tokens
   */
  public final List<Token> toList() {
    return tokens;
  }

  /**
   * Set source file
   */
  public void setSourceFile(final File f) {
    sourceFile = f;
  }

  /**
   * Get source file
   * @return file
   */
  public File getSourceFile() {
    return sourceFile;
  }

  /**
   * Get num tokens
   * @return number of tokens
   */
  public int getNumTokens() {
    return tokens.size();
  }

  /**
   * Return an iterator over the list of tokens.
   * @see java.lang.Iterable#iterator()
   * @return iterator
   */
  @Override
  public final Iterator<Token> iterator() {
    return tokens.iterator();
  }
}

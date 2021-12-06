package edu.odu.cs.cs350.DupDetector;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
//import static org.hamcrest.MatcherAssert.assertThat; 
//import static org.hamcrest.Matchers.*;


/**
 * @author zeil
 *
 */
public class ScannerTest {
  /**
   * @throws java.lang.Exception
   */
  @BeforeAll
  public static void setUp() throws Exception {}

  @Test
  public final void testScanner() {
    String inputString = "123\tident1 \nKEYWORD     42\n";
    Reader input = new StringReader(inputString);
    TokenStream tokenstream = new TokenStream(input);
    ArrayList<Token> tokens = new ArrayList<Token>();
    for (Token tok: tokenstream) {
        tokens.add(tok);
    }
    assertEquals (4, tokens.size());
    
    Token t = tokens.get(0);
    assertEquals (TokenKinds.INTEGER_LITERAL, t.getKind());
    assertEquals ("123", t.getLexeme());
    assertEquals (1, t.getLineNumber());
    assertEquals (1, t.getColumnNumber());
    
    t = tokens.get(1);
    assertEquals (TokenKinds.IDENTIFIER, t.getKind());
    assertEquals ("ident1", t.getLexeme());
    assertEquals (1, t.getLineNumber());
    assertEquals (5, t.getColumnNumber());
    
    t = tokens.get(2);
    assertEquals (TokenKinds.KEYWORD, t.getKind());
    assertEquals (2, t.getLineNumber());
    assertEquals (1, t.getColumnNumber());
    
    t = tokens.get(3);
    assertEquals (TokenKinds.INTEGER_LITERAL, t.getKind());
    assertEquals ("42", t.getLexeme());
    assertEquals (2, t.getLineNumber());
    assertEquals (13, t.getColumnNumber());
  }
}

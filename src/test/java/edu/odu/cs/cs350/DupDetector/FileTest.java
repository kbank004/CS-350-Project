package edu.odu.cs.cs350.DupDetector;

import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class FileTest {
  private static final Path dataPath = Path.of("src/test/data");
  private static final List<Token> expectedTokens = new ArrayList<Token>(Arrays.asList(
    new Token(TokenType.KEYWORD,     3, 1, "int"),
    new Token(TokenType.IDENTIFIER,  3, 5, "main"),
    new Token(TokenType.SYMBOL,      3, 9, "("),
    new Token(TokenType.SYMBOL,      3, 10, ")"),
    new Token(TokenType.SYMBOL,      3, 12, "{"),
    new Token(TokenType.IDENTIFIER,  4, 3, "std"),
    new Token(TokenType.SYMBOL,      4, 6, "::"),
    new Token(TokenType.IDENTIFIER,  4, 8, "cout"),
    new Token(TokenType.SYMBOL,      4, 13, "<<"),
    new Token(TokenType.STRING_LIT,  4, 16, "\"hello, world!\\n\""),
    new Token(TokenType.SYMBOL,      4, 33, ";"),
    new Token(TokenType.KEYWORD,     5, 3, "return"),
    new Token(TokenType.INTEGER_LIT, 5, 10, "0"),
    new Token(TokenType.SYMBOL,      5, 11, ";"),
    new Token(TokenType.SYMBOL,      6, 1, "}")
  ));

  private final Path getDataPath(String path) {
    return dataPath.resolve(path);
  }

  @Test
  public final void testGetNumTokens() {
    File f = new File(getDataPath("a.cpp"));
    assertThat(f.getNumTokens(), is(expectedTokens.size()));
  }

  @Test
  public final void testGetTokens() {
    File f = new File(getDataPath("a.cpp"));
    assertIterableEquals(f.getTokens(), expectedTokens);
  }

  @Test
  public final void testGetFilePath() {
    Path dogPath = Path.of("/home/bob/Pictures/dog.jpg");
    File f = new File(dogPath);
    assertThat(f.getFilePath(), is(dogPath));
  }

  @Test
  public final void testToString() {
    Path p = Path.of("src/test/data/a.cpp");

    File f = new File(p);
    assertThat(f.toString(), is(p.toAbsolutePath() + ", " + expectedTokens.size()));
  }

  @Test
  public final void testCompareTo() {
    File p1 = new File("/home/abby/billiards");
    File p2 = new File("/home/abby/coconut");
    File p3 = new File("/home/chris/alligator");
    File p4 = new File("/home/chris/bounce");
    File p5 = new File("/var/www/html");
    assertTrue(p1.compareTo(p2) < 0);
    assertTrue(p1.compareTo(p5) < 0);
    assertTrue(p2.compareTo(p3) < 0);
    assertTrue(p3.compareTo(p2) > 0);
    assertTrue(p4.compareTo(p2) > 0);
    assertTrue(p3.compareTo(p3) == 0);
  }
}

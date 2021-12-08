package edu.odu.cs.cs350.DupDetector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;

import edu.odu.cs.cs350.sharedphrases.*;

/**
 * @author Banks
 * @author Lugo
 */

 // WIP - Incomplete
public class Opportunity {
  // tStream is a list of integers where each int represents a kind
  
  public List<TokenStream> tokens = new ArrayList<>();

  public Opportunity() {}

  public static String encode(List<? extends Token> tokens) throws IllegalTokenKindException {
    StringBuffer buffer = new StringBuffer();
    for (Token token: tokens) {
        int tkind = token.getTokenKind();
        if (tkind < 0 || tkind >= UnicodeConstants.PRIVATE_USE_AREA) {
            throw new IllegalTokenKindException ("Cannot encode a token kind = " + tkind);
        }
        buffer.append(token.getTokenKind());
    }
    return buffer.toString();
  }


  public static List<Opportunity> findOpportunities(List<File> files, int minSequenceLength) {
    List<String> fileStrings = new ArrayList<>();
    for (File f : files) {
      try {
        var encodedString = encode(f.getTokens().toList());

        System.out.println(encodedString);
        fileStrings.add(encodedString);
      } catch (IllegalTokenKindException e) {
        e.printStackTrace();
      }
    }

    SharedPhrases phrases = new SharedPhrases();
    if (fileStrings.size() == files.size()) {
      for (int i = 0; i < fileStrings.size(); ++i) {
        phrases.addSentence(fileStrings.get(i), files.get(i).getFilePath().toString());
      }
    } else {
      System.err.println("On no! failed to encode tokens in file somehow!");
      System.exit(-1);
    }

    List<String> dupePatterns = new ArrayList<>();
    for (CharSequence phrase : phrases.allPhrases()) {
      var dupePhrases = phrases.phrasesContaining(phrase.toString());
      for (CharSequence c : dupePhrases) {
        if (c.length() >= minSequenceLength) {
          dupePatterns.add(c.toString());
        }
      }
    }
    
    // Uses tokenPatterns to reconstruct the original token sequences
    // of the duplicated phrases lmfao. ew
    List<List<Token>> tokenPatterns = new ArrayList<>();
    for (int i = 0; i < dupePatterns.size(); ++i) {
      List<Token> tokenPattern = new ArrayList<>();
      for (Character c : dupePatterns.get(i).toCharArray()) {
        tokenPattern.add(new Token(TokenType.values()[Integer.parseInt(c.toString())], 0, 0));
      }
      tokenPatterns.add(tokenPattern);
    }

    List<Opportunity> oppies = new ArrayList<>();
    for (List<Token> tokenSequence : tokenPatterns) {
      var opportunity = new Opportunity();
      for (File f : files) {
        for (int i = 0; i < f.getNumTokens() - tokenSequence.size(); ++i) {
          boolean matched = true;
          for (Token t : tokenSequence) {
            if (f.getTokens().toList().get(i).getKind() != t.getKind()) {
              matched = false;
            }
          }
          if (matched) {
            List<Token> sequenceFound = new ArrayList<>();
            for (int j = i - tokenSequence.size(); j < tokenSequence.size(); ++j) {
              sequenceFound.add(f.getTokens().toList().get(i));
            }
            
            opportunity.tokens.add(new TokenStream(sequenceFound, f));
          }
        }
      }
      oppies.add(opportunity);
    }
    
    return oppies;
  }
  
  public int potentialSavings() {
    return (tokens.size() - 1) * tokens.get(0).getNumTokens();
  }
  
  public int getNumOccurrances() {
    return tokens.size();
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    
    sb.append("Opportunity ");
    sb.append(potentialSavings() + ", ");
    sb.append(tokens.get(0).getNumTokens() + "tokens \n");
    
    for (TokenStream stream : tokens) {
      sb.append(stream.getSourceFile().toString() + ":" + stream.toList().get(0).getLineNumber() + "\n");
      for (Token t : stream) {
        sb.append(t + " ");
      }
      sb.append("\n");
    }

    return sb.toString();
  }
}
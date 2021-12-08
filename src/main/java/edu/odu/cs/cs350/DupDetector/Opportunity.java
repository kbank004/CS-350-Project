package edu.odu.cs.cs350.DupDetector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;

import edu.odu.cs.cs350.sharedphrases.*;

/**
 * @author Banks
 */

 // WIP - Incomplete
public class Opportunity {
  List<Integer> tStream = new ArrayList<>();
  SharedPhrases phrases = new SharedPhrases();

  public Opportunity(File f, int minSequenceLength) {

    // tStream is a list of integers where each int represents a kind
    var ts = f.getTokens();
    for (Token t : ts) {
      tStream.add(
          t.getKind().value);
    }

    // adapted from: https://stackoverflow.com/a/41898691
    // this splits the list into separate sublists (partitions) of length
    // minSequenceLength
    int partitionSize = minSequenceLength;
    List<List<Integer>> partitions = new ArrayList<>();
    for (int i = 0; i < tStream.size(); i += partitionSize) {
      partitions.add(tStream.subList(i, Math.min(i + partitionSize, tStream.size())));
    }

    int i = 0;
    // Iterate through each "partition" and add it to phrases
    for (List<Integer> partition : partitions) {
      phrases.addSentence(listToString(partition), Integer.toString(i));
      i++;
    }
  }

  // Takes a list of integers and returns a string of these integers concatenated
  public String listToString(List<Integer> ls) {
    StringBuilder sb = new StringBuilder();
    for (Integer s : ls) {
      sb.append(s);
    }
    return sb.toString();
  }

  public void printPhrases() {
    for (CharSequence p : phrases.phrasesContaining("120202")) {
      System.out.println(p);
    }
  }
}
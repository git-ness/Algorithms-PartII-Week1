import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;

import java.util.ArrayList;

public class WordNet {


    private ArrayList<Integer[]> hyponymIntList = new ArrayList<>();

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {

        In inSynsets = new In(synsets);

        In inHypernyms = new In(hypernyms);
        processHypernymsToArrayAndArrayList(inHypernyms);

        //TODO: Use Hyponyms file to generate Digraph(In in).
    }

    private void hypernymToString() {

        // Take the syset id, lookup the id and grab the word from syset.txt.
        // Stick the word in a list that replaces the hyperyms text with words.
        // So I guess just find a way to look up the id's in syset.txt and translate
        // them to the word, then stick them in another list.

        // Although we need to grab the hypernyms list first.
        // 1. Input the id you want.
        // 2. It will find the id and grab the whole line in hypernyms txt
        // 3. Look up the id's one by one and put them in an arraylist.
        // 4. Print the arraylist to get the translation.

        // Although first I need to process the lines from synset and hypernyms files.
        //

    }

    private void processHypernymsToArrayAndArrayList(In inHypernyms) {
        String hypernymReadLineString = "";

        while (inHypernyms.hasNextLine()) {

            hypernymReadLineString = inHypernyms.readLine();
            String[] lineString;

            // Splits on new line and inserts it to String[] lineString array.
            lineString = hypernymReadLineString.split(System.getProperty("line.separator"));

            for (String lineStringStringVar : lineString) {
                String[] integerLineValues = lineStringStringVar.split(",");
                Integer[] hypernymArray = new Integer[integerLineValues.length];
                for (int i = 0; i < integerLineValues.length; i++) {

                    String integerLineValuesVar = integerLineValues[i];
                    Integer valueOfLineValuesVar = Integer.valueOf(integerLineValuesVar);
                    hypernymArray[i] = valueOfLineValuesVar;

                }
                hyponymIntList.add(hypernymArray);
            }
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        // Process out all the nouns from the given files. Aren't they already all nouns though? From
        // the exercise page, "The file synsets.txt lists all the (noun) synsets in WordNet"
        // Guess it just wants a list of them that's iterable.
        return null;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        // Check if the word is a nouns. the exercise page still mentions
        // "The file synsets.txt lists all the (noun) synsets in WordNet"
        // Only think I can think of is phrases where those are not nouns.
        // If that's the case, I don't think there's a way to
        return false;
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        // Incorporate the graph to determine distance? Maybe it's more simple than that.
        return 0;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        // Return a synset with a common ancestor.
        return "";
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordNet = new WordNet("/Users/elsa/learning/Algorithms-Part2/wordnettesting/synsets6.txt", "/Users/elsa/learning/Algorithms-Part2/wordnettesting/hypernyms6TwoAncestors.txt");

    }
}

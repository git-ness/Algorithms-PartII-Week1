import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.*;

public class WordNet {

    private ArrayList<String> synsetNounWordList = new ArrayList<>();
    private ArrayList<String> synsetSynonyms = new ArrayList<>();
    private ArrayList<String> synsetGloss = new ArrayList<>();
    private ArrayList<Integer[]> hypernymIntList = new ArrayList<>();
    private Digraph digraph;
    private BreadthFirstDirectedPaths breadthFirstDirectedPaths;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {

        processSynsets(new In(synsets));
        processHypernyms(new In(hypernyms));

        BreadthFirstDirectedPaths breadthFirstDirectedPaths = new BreadthFirstDirectedPaths(digraph, 5);
//        System.out.println(breadthFirstDirectedPaths.hasPathTo(10));
        Iterable<Integer> iterator1 = breadthFirstDirectedPaths.pathTo(10);
//        System.out.println(iterator1.iterator());


//        Iterable<Integer> iterator2 = breadthFirstDirectedPaths.pathTo(10);
//        iterator2.iterator();

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

        // Although first I need to process the lines from synset and hypernyms files. (done!)
        //
    }


    private void processSynsets(In inSynsets) {

        String synsetReadLine;

        while (inSynsets.hasNextLine()) {
            synsetReadLine = inSynsets.readLine();


            String[] lineString = synsetReadLine.split(System.getProperty("line.separator"));

            for (String lineSynsetStringVar : lineString) {
                String[] synsetLineValue = lineSynsetStringVar.split(",");

                // Synset and synonyms are not delimited by a , but by a space. This further
                // extracts the synset word and synonyms further.
                String synsetWordAndSynonyms = synsetLineValue[1];
                String[] synsetAndSynonymSplit = synsetWordAndSynonyms.split(" ", 2);
                synsetNounWordList.add(synsetAndSynonymSplit[0]);

                if (synsetAndSynonymSplit.length > 1) {
                    synsetSynonyms.add(synsetAndSynonymSplit[1]);
                } else {
                    synsetSynonyms.add("");
                }

                synsetGloss.add(synsetLineValue[2]);
            }
        }
    }

    private void processHypernyms(In inHypernyms) {
        String hypernymReadLineString = "";
        int verticesCount = 0;

        while (inHypernyms.hasNextLine()) {
            hypernymReadLineString = inHypernyms.readLine();
            String[] lineString;

            // Splits on new line and inserts it to String[] lineString array.
            lineString = hypernymReadLineString.split(System.getProperty("line.separator"));

            for (String lineStringStringVar : lineString) {
                String[] integerLineValues = lineStringStringVar.split(",");
                Integer[] hypernymArray = new Integer[integerLineValues.length];
                for (int i = 0; i < integerLineValues.length; i++) {
                    verticesCount++;
                    String integerLineValuesVar = integerLineValues[i];
                    Integer valueOfLineValuesVar = Integer.valueOf(integerLineValuesVar);
                    hypernymArray[i] = valueOfLineValuesVar;
                }
                hypernymIntList.add(hypernymArray);
            }
        }
        digraph = new Digraph(verticesCount);

        for (Integer[] intList : hypernymIntList) {
            for (int i = 1; i < intList.length; i++) {
//              System.out.println("i: " + i + " intList[intList.length - i]: " + intList[intList.length - i] + " intList[intList.length - i - 1]: " + intList[intList.length - i - 1]);
                digraph.addEdge(intList[intList.length - i], intList[intList.length - i - 1]);

            }
        }
    }


    // returns all WordNet nouns
    public Iterable<String> nouns() {
        // Process out all the nouns from the given files. Aren't they already all nouns though? From
        // the exercise page, "The file synsets.txt lists all the (noun) synsets in WordNet"
        // Guess it just wants a list of them that's iterable.
        return synsetNounWordList;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) { // Should be O(logarithmic); ArrayList.contains(word) is O(n)
        // Check if the word is a nouns. the exercise page still mentions
        // "The file synsets.txt lists all the (noun) synsets in WordNet"
        return synsetNounWordList.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        // distance(A, B) = distance is the minimum length of any ancestral path between any synset v of A and any synset w of B.
//        this.breadthFirstDirectedPaths = new BreadthFirstDirectedPaths(digraph, 5);
        int size = 1;
        this.breadthFirstDirectedPaths = new BreadthFirstDirectedPaths(digraph, synsetNounWordList.indexOf(nounA));
        if (breadthFirstDirectedPaths.hasPathTo(synsetNounWordList.indexOf(nounB))) {
//        if (breadthFirstDirectedPaths.hasPathTo(0)) {
            Iterable<Integer> iterator = breadthFirstDirectedPaths.pathTo(0);
            size++;
            if (iterator.iterator().hasNext()) {
            }
        } else {
            return 0;
        }
        return size;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        // Return a synset word between nounA and nounB with a common ancestor.
        return "";
    }

    // do unit testing of this class
    public static void main(String[] args) {
//        WordNet wordNet = new WordNet("wordnettesting/synsets6.txt", "wordnettesting/hypernyms6TwoAncestors.txt");
//        WordNet wordNet = new WordNet("wordnettesting/synsetsSubSet.txt","wordnettesting/hypernymSubSet.txt");
        WordNet wordNet = new WordNet("wordnettesting/synsets.txt", "wordnettesting/hypernymsManyPathsOneAncestor.txt");
        System.out.println("Should be 2: " + wordNet.distance("noun1", "noun2"));

    }
}

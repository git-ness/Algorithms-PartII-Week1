// Implementing that stores both the id and the set of synonyms. What data structure what would that be.

// Use all nouns as a key and have the value be the integer.
// If there is more than one noun, store multiple keys as an array.
// noun -> key1, key2, key3 (noun, idKey if more than one) --> if there is a <K, V> at all...return true

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;


public class WordNet {

    private ArrayList<String> synsetNounWordArrayList = new ArrayList<>();
    private HashMap<String, ArrayList<Integer>> synsetHashMap = new HashMap<>();
    private ArrayList<String> synsetSynonyms = new ArrayList<>();
    private ArrayList<String> synsetGloss = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> hypernymIntList = new ArrayList<>();
    private Digraph digraph;
    private BreadthFirstDirectedPaths breadthFirstDirectedPathsAll;
    private BreadthFirstDirectedPaths breadthFirstDirectedPathsDistance;
    private SAP sap;
    private int verticesCount;


    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {

        processSynsets(new In(synsets));
        processHypernyms(new In(hypernyms));
    }

    private void processSynsets(In inSynsets) {

        String synsetReadLine;
        verticesCount = 0;

        while (inSynsets.hasNextLine()) {
            synsetReadLine = inSynsets.readLine();
            String[] lineString = synsetReadLine.split(System.getProperty("line.separator"));
            verticesCount++;

            for (String lineSynsetStringVar : lineString) {
                String[] synsetLineValue = lineSynsetStringVar.split(",");

                // Synset and synonyms are not delimited by a , but by a space. This further
                // extracts the synset word and synonyms further.
                String synsetWordAndSynonyms = synsetLineValue[1];
                String[] synsetAndSynonymSplit = synsetWordAndSynonyms.split(" ", 2);
                synsetNounWordArrayList.add(synsetLineValue[1]);

                for (String noun : synsetAndSynonymSplit) {
                    if (synsetHashMap.containsKey(noun)) {
                        ArrayList<Integer> synsetHashValue = synsetHashMap.get(noun);
                        synsetHashValue.add(Integer.parseInt(synsetLineValue[0]));
                    }
                    else {
                        ArrayList<Integer> newSynsetHashValue = new ArrayList<>(1);
                        newSynsetHashValue.add(Integer.parseInt(synsetLineValue[0]));
                        synsetHashMap.put(noun, newSynsetHashValue);
                    }
                }
            }
        }
    }

    private void processHypernyms(In inHypernyms) {
        String hypernymReadLineString = "";

        while (inHypernyms.hasNextLine()) {
            hypernymReadLineString = inHypernyms.readLine();
            String[] lineString;

            // Splits on new line and inserts it to String[] lineString array.
            lineString = hypernymReadLineString.split(System.getProperty("line.separator"));
            for (String lineStringStringVar : lineString) {
                String[] integerStringLineValues = lineStringStringVar.split(",");
                ArrayList<Integer> hypernymArray = new ArrayList<>();

                for (int i = 0; i < integerStringLineValues.length; i++) {
                    // Take the string extracted from integerStringLineValues, grab the integer and
                    // input that into the ArrayList holding each synset integers.
                    hypernymArray.add(Integer.parseInt(integerStringLineValues[i]));

                }

                hypernymIntList.add(hypernymArray);
            }
        }
        digraph = new Digraph(verticesCount);

        for (ArrayList<Integer> intList : hypernymIntList) {
            for (int i = 1; i < intList.size(); i++) {
                digraph.addEdge(intList.get(intList.size() - i), intList.get(intList.size() - i - 1));
            }
        }
        this.sap = new SAP(digraph);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return synsetNounWordArrayList;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return synsetHashMap.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        return sap.length(synsetHashMap.get(nounA), synsetHashMap.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        int ancestor = sap.ancestor(synsetHashMap.get(nounA), synsetHashMap.get(nounB));

        if (ancestor == -1) {
            return "No ancestor found?"; //TODO: Check if this is valid.
        } else {
            return synsetNounWordArrayList.get(ancestor);
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordNet = new WordNet("wordnettesting/synsets6.txt", "wordnettesting/hypernyms6InvalidCycle+Path.txt");

//        WordNet wordNet = new WordNet("wordnettesting/synsets6.txt", "wordnettesting/hypernyms6TwoAncestors.txt");
//        String ancestorString = wordNet.sap("f", "b");
//        System.out.println("f " + ancestorString);  // How is this b? Shouldn't it be f?
//        /Users/elsa/Pictures/Monosnap/hypernyms5TwoAncestorsVisual.png  --> Perhaps due to b being a smaller vertex value makes it b.

//        String result = wordNet.sap("b", "c");
//        System.out.println(result);
//        Was invalid before but was fixed by adding the counter to match each line in the sysnsetprocessor method. 

//        String ancestorString = wordNet.sap("c", "b");
//        System.out.println(ancestorString);
//        passes!

//        String ancestorString = wordNet.sap("e", "b");
//        String ancestorString = wordNet.sap("e", "b");
//        System.out.println(ancestorString);
//        passes!

        String ancestorStr = wordNet.sap("h", "h");
        System.out.println(ancestorStr);
//        /Users/elsa/Pictures/Monosnap/hypernyms8ManyAncestors.jpg

//        String[] myArray = {"a", "b", "c"};
//        for (int i = 0; i < myArray.length; i++ ) {
//            for (int j = 0; j < myArray.length; j++) {
//                String ancestorString = wordNet.sap(myArray[i], myArray[j]);
//                System.out.println("v: " + myArray[i] + " w: " + myArray[j]);
//                System.out.println(ancestorString);
//                System.out.println("--------------------");
//            }
//        }

//        WordNet wordNet = new WordNet("wordnettesting/synsets.txt", "wordnettesting/hypernyms11AmbiguousAncestor.txt");
        // /Users/elsa/Pictures/Monosnap/hypernyms11AmbiguousAncestorPic.png


    }
}

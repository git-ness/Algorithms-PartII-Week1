// Implementing that stores both the id and the set of synonyms. What data structure what would that be.

// Use all nouns as a key and have the value be the integer.
// If there is more than one noun, store multiple keys as an array.
// noun -> key1, key2, key3 (noun, idKey if more than one) --> if there is a <K, V> at all...return true

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;


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


    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {

        //TODO: Check isNoun is O(log n) and test distance

        processSynsets(new In(synsets));
        processHypernyms(new In(hypernyms));
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
                synsetNounWordArrayList.add(synsetAndSynonymSplit[0]);

                if (synsetHashMap.containsKey(synsetAndSynonymSplit[0])) { // synsetHashMap ("e", [4]) and is true
                    // After extracting current id's, collect the id of the value and aggregate existing id's, add the new id into an array.

                    ArrayList<Integer> synsetHashValue = synsetHashMap.get(synsetAndSynonymSplit[0]);
                    int placeholder = 0;
//                    Integer[] newInt = new Integer[synsetHashValue.length + 1];
//                    for (int i = 0; i < synsetHashValue.length; i++) {
//                        newInt[i] = synsetHashValue[i];
//                    }

//                    newInt[newInt.length - 1] = Integer.valueOf(synsetLineValue[0]);
//                    synsetHashMap.put(synsetAndSynonymSplit[0], newInt);
                } else {
                    String s = synsetLineValue[0];
                    Integer[] matchingNounIndexArray = new Integer[1];
                    matchingNounIndexArray[0] = Integer.parseInt(s);
//                    synsetHashMap.put(synsetAndSynonymSplit[0], matchingNounIndexArray);
                }

                if (synsetAndSynonymSplit.length > 1) {
                    synsetSynonyms.add(synsetAndSynonymSplit[1]);
//                    synsetHashMap.put(synsetLineValue[0], synsetAndSynonymSplit[1]);
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
                String[] integerStringLineValues = lineStringStringVar.split(",");
                verticesCount++;
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
                int placeholder = 0;
//              System.out.println("i: " + i + " intList[intList.length - i]: " + intList[intList.length - i] + " intList[intList.length - i - 1]: " + intList[intList.length - i - 1]);
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
        // distance(A, B) = distance is the minimum length of any ancestral path between any synset v of A and any synset w of B.
//        this.breadthFirstDirectedPathsDistance = new BreadthFirstDirectedPaths(digraph, 5);

        return sap.length(synsetHashMap.get(nounA), synsetHashMap.get(nounB));
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
//        WordNet wordNet = new WordNet("wordnettesting/synsets15.txt", "wordnettesting/hypernyms15Path.txt");
        WordNet wordNet = new WordNet("wordnettesting/synsets.txt", "wordnettesting/hypernyms.txt");
        System.out.println("Is n present? Yes: " + wordNet.isNoun("claw"));

    }
}



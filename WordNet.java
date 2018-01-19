//TODO: Think about how Wordnet and length from SAP are related.
    //TODO: THey are related via the call to SAP from the distance method
//TODO: Smaller subsets i.e. onesynset etc can't reproduce this issue.

// Implementing that stores both the id and the set of synonyms. What data structure what would that be.

// Use all nouns as a key and have the value be the integer.
// If there is more than one noun, store multiple keys as an array.
// noun -> key1, key2, key3 (noun, idKey if more than one) --> if there is a <K, V> at all...return true

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;


public class WordNet {

    private final ArrayList<String> synsetNounWordArrayList = new ArrayList<>();
    private final HashMap<String, ArrayList<Integer>> synsetHashMap = new HashMap<>();
    private final ArrayList<ArrayList<Integer>> hypernymIntList = new ArrayList<>();
    private Digraph digraph;
    private SAP sap;
    private int verticesCount;
    private int root;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {

        processSynsets(new In(synsets));
        processHypernyms(new In(hypernyms));
        System.out.println("------- 0 =? " + digraph.outdegree(38003));

        //TODO: Set a conditional for when the root is ^ and see what lengths it is creating. Does it have outdegree 0?

        for (int anc = 0; anc < digraph.V(); anc++) {

            if (digraph.outdegree(anc) == 0) {
//                System.out.println("I found the root, it is: " + anc);
            }
        }
    }

    private int root() {
        for (int anc = 0; anc < digraph.V(); anc++) {
            int value = anc;
            if (digraph.outdegree(anc) == 0) {
                return anc;
            }
        }
        return 0;
    }

    private void processSynsets(In inSynsets) {

        String entireLineInFile;
        String[] convertToStringArray;
        String[] splitOnCommaToArray;
        String[] synsetAndSynonymSplit; // Is split on space due to convention of synset.txt.
        String synsetWord;
        verticesCount = 0;

        while (inSynsets.hasNextLine()) {
            entireLineInFile = inSynsets.readLine();
            convertToStringArray = entireLineInFile.split(System.getProperty("line.separator"));
            verticesCount++;

            for (String extractStringFromArray : convertToStringArray) {
                splitOnCommaToArray = extractStringFromArray.split(",");

                // Synset and synonyms are not delimited by a , but by a space. This further
                // extracts the synset word and synonyms further.
                String synsetAndSynonymString = splitOnCommaToArray[1];
                synsetAndSynonymSplit = synsetAndSynonymString.split(" ", 2);
                synsetWord = synsetAndSynonymSplit[0];
                synsetNounWordArrayList.add(synsetWord);

                for (String noun : synsetAndSynonymSplit) {
                    if (synsetHashMap.containsKey(noun)) {
                        ArrayList<Integer> synsetHashValue = synsetHashMap.get(noun);
                        synsetHashValue.add(Integer.parseInt(splitOnCommaToArray[0]));
                    } else {
                        ArrayList<Integer> newSynsetHashValue = new ArrayList<>(1);
                        newSynsetHashValue.add(Integer.parseInt(splitOnCommaToArray[0]));
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
            throw new IllegalArgumentException();
        } else {
            return synsetNounWordArrayList.get(ancestor);
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {

//        WordNet wordNet = new WordNet("wordnettesting/synsets15.txt", "wordnettesting/hypernyms15Tree.txt");
        WordNet wordNet = new WordNet("wordnettesting/synsets.txt", "wordnettesting/hypernyms.txt");


        int root = wordNet.root();
        System.out.println("Proposed root is: " + root);

    }


}

// Write code to check to see if a dag is rooted or not.
// Count the number of nodes pointing out. If it's 0, it's a root.

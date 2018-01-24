
// Implementing that stores both the id and the set of synonyms. What data structure what would that be.

// Use all nouns as a key and have the value be the integer.
// If there is more than one noun, store multiple keys as an array.
// noun -> key1, key2, key3 (noun, idKey if more than one) --> if there is a <K, V> at all...return true

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class WordNet {

    private final ArrayList<ArrayList<Integer>> hypernymIntList = new ArrayList<>();
    HashMap<String, HashSet<Integer>> nounToSynsetIdMap = new HashMap<>();
    HashMap<Integer, HashSet<String>> synsetIdToNounsMap = new HashMap<>();
    private Digraph digraph;
    private SAP sap;
    private int verticesCount;
    private int root;


    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {

        In synsetsInput = new In(synsets);
        processSynsets(synsetsInput);
        processHypernyms(new In(hypernyms));

        for (int anc = 0; anc < digraph.V(); anc++) {

            if (digraph.outdegree(anc) == 0) {
//                System.out.println("I found the root, it is: " + anc);
            }
        }
    }

    private int root() {
        for (int anc = 0; anc < digraph.V(); anc++) {
            if (digraph.outdegree(anc) == 0) {
                return anc;
            }
        }
        return -1;
    }

    private void processSynsets(In inSynsets) {

        String entireLineInFile;
        String[] convertToStringArray;
        String[] stringsBetweenCommas;
        String[] stringsBetweenSpaces; // Is split on space due to convention of synset.txt.
        String synsetWord;

        while (inSynsets.hasNextLine()) {
            entireLineInFile = inSynsets.readLine();
            convertToStringArray = entireLineInFile.split(System.getProperty("line.separator"));
            verticesCount++;
            ArrayList<String> wordsInSynset;

            for (String extractStringFromArray : convertToStringArray) {
                // Synset and synonyms are not delimited by a , but by a space. This further
                // extracts the synset word and synonyms further.

                stringsBetweenCommas = extractStringFromArray.split(",");
                Integer synsetId = Integer.parseInt(stringsBetweenCommas[0]);

                // Synset and synonyms are not delimited by a , but by a space. This further
                // extracts the synset word and synonyms further.
                String synsetsWords = stringsBetweenCommas[1];
                stringsBetweenSpaces = synsetsWords.split(" ");

                // Parses out the String[] from the synsetAndSynonymSplit and imports them into an ArrayList<String>
                wordsInSynset = new ArrayList<>(Arrays.asList(stringsBetweenSpaces));

                /*
                    Goal to accomplish next (What's this things job?):
                        [x] Add synset words to nounToSynsetIdMap.  "zero",1
                        [x] Add synsetId to which words are on the "line" in the csv file.    1, {one, uno, appearsTwice)

                    Process - pseudo code (What we'll need):
                        [x] HashSet<Integer> nounIdSet = nounToSynsetIdMap.get(noun);
                        [x] nounIdSet.add(synsetId);

                        HashSet<Integer> nounsInSynset = synsetIdToNounMap.get(noun);
                        nounsInSynset.add(noun);

                    Test Result Validation:
                        [x] nounToSynsetIdMap.get("appearsTwice") returns 1,3
                        [x] nounIdSet.get("one") returns 1,2
                 */

                HashSet<String> nounsWithinSynset = new HashSet<>();

                for (String noun : wordsInSynset) {

                    synsetIdToNounsMap.computeIfAbsent(synsetId, k -> new HashSet<>());
                    HashSet<String> nounsFromSynset = synsetIdToNounsMap.get(synsetId);
                    nounsFromSynset.add(noun);

                    nounToSynsetIdMap.computeIfAbsent(noun, k -> new HashSet<>());
                    HashSet<Integer> synsetIds = nounToSynsetIdMap.get(noun);
                    synsetIds.add(synsetId);
                }
            }
        }

        int placeholder = 0;
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
        return (Iterable<String>) nounToSynsetIdMap;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return nounToSynsetIdMap.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        return sap.length(nounToSynsetIdMap.get(nounA), nounToSynsetIdMap.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        int ancestor = sap.ancestor(nounToSynsetIdMap.get(nounA), nounToSynsetIdMap.get(nounB)); // 0

        if (ancestor == -1) {
            throw new IllegalArgumentException();
        } else {
            HashSet<String> nounsSet = synsetIdToNounsMap.get(ancestor); // zero, nilch, nada
            for (String nounCanidate : nounsSet) {

                if (nounsSet.contains(nounCanidate)) {
                    return nounCanidate;
                }
            }
        }
            /*---------- Memory Map ---------------
                    Goal to accomplish next (What's this things job?):
                          Find the common ancestor from the integer. The integer is the id of the synset that maps to a noun.

                    Process - pseudo code (What we'll need):
                          nounToSynsetIdMap
                          synsetIdToNounsMap

                          Iterate through the nounToSynsetIdMap, and synsetIdToNounsMap.
                          Locate the common noun in both.

                          1 and 4 share 0 which is the word "zero"

                          for(Integer synsetInteger : nounToSynsetIdMap) {
                            for (String noun : synsetIdToNounsMap) {
                                if ( noun.equals(synsetInteger) ) {
                                  return noun;
                                }
                             }
                          }


                    Test Result Validation:
                          sap("one", "three") = zero
            */
        // If nothing matches, throw new IllegalArg
        throw new IllegalArgumentException();
    }

    // do unit testing of this class
    public static void main(String[] args) {

        WordNet wordNet = new WordNet("wordnettesting/processingBug.txt", "wordnettesting/processingBugHypernyms.txt");

        int root = wordNet.root();
        System.out.println("Proposed root is: " + root);
        testMethod(wordNet);


        int isItFive = wordNet.distance("worm", "bird");
        System.out.println("distance is five?  " + isItFive);



//        testMethod(wordNet);

    }

    private static void testMethod(WordNet wordNet) {
        // Path between animate_being and animal.. what is the length?

        try {
            int bird = wordNet.distance("worm","bird");
            int animal = wordNet.distance("animate_being", "animal");

            System.out.println("distance for word: " + bird);
            System.out.println("distance for animal: " + animal);
        } catch (Exception E) {
            int bird = wordNet.distance("worm","bird");
            int animal = wordNet.distance("animate_being", "animal");

            System.out.println(E);

        }
    }

//}

    }


// Write code to check to see if a dag is rooted or not.
// Count the number of nodes pointing out. If it's 0, it's a root.

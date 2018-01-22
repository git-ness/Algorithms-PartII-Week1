
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

    private final ArrayList<String> synsetNounWordArrayList = new ArrayList<>();
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
//        System.out.println("------- 0 =? " + digraph.outdegree(38003));

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

//                synsetNounWordArrayList.add(synsetWord);
                HashSet<String> synsetNouns = new HashSet<>();
                synsetIdToNounsMap.put(synsetId, synsetNouns);


                for (String noun : wordsInSynset) {


                    synsetNouns.add(noun);

                    if (!nounToSynsetIdMap.containsKey(noun)) {
                        HashSet<Integer> synsetIdSet = new HashSet<>();
                        synsetIdSet.add(synsetId);
                        nounToSynsetIdMap.put(noun, synsetIdSet);
                    } else {
                        HashSet<Integer> synsetIdSet = nounToSynsetIdMap.get(noun);
                        synsetIdSet.add(synsetId);
                    }

                    // when parsing line 1:
                    /*
                            create a new HashMap above.
                            We grab the noun, and add the synsetId of that noun to the HashMap
                            Insert

                            new hashMap
                            hashMap.add(synsetId) of the noun
                            nounToSynsetIdMap.put("one", hashMap)


                            one, 1
                            uno, 1
                            two_plus_one, 1

                            one, 2
                            en, 2

                            When parsing line 2, hashMap.get("one")
                            HashMap<Integer> theMap = hashMap.get("one)
                            hashMap.add(synsetId) for the next appearance of the noun


                     */

                    // when parsing line 2:

                    // nounToSynsetId : k -. "one", v->?1,2


                    // Creates a one to many map of synsetNoun to multiple synsetIds if the noun is in more than one synset.

                }
//                    noun : moreThanOneNounIds -> use ids to parse each id where needed
//                    noun : ArrayList of synsetIds the noun belogs to.
//                    noun : synsetIds -> isnoun(word) -> isNoun(noun) -> pass noun as iterable where needed  (cannot have a dup key, will not work)
//
//                    can have more than one nounId
//                    can have more than one noun in the synset

            }
        }
        int placeHolder = 0;
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

    // is the word a WordNet noun? //TODO: Does the return value for nouns() look ok?
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
        int ancestor = sap.ancestor(nounToSynsetIdMap.get(nounA), nounToSynsetIdMap.get(nounB));

        if (ancestor == -1) {
            throw new IllegalArgumentException();
        } else {
            //TODO: Find how to return the ancestor given this situation.

            return synsetNounWordArrayList.get(ancestor);
            //TODO-------------------------------------------------------

        }
    }

    // do unit testing of this class
    public static void main(String[] args) {

        WordNet wordNet = new WordNet("wordnettesting/processingBug.txt", "wordnettesting/processingBugHypernyms.txt");

//        int root = wordNet.root();
//        System.out.println("Proposed root is: " + root);
//        testMethod(wordNet);

        System.out.println("True " + wordNet.isNoun("one"));
        System.out.println("True: " + wordNet.isNoun("two_plus_one"));
        System.out.println("1,3: " + wordNet.nounToSynsetIdMap.get("two_plus_one"));

// Check # of eddges and vertices in graph. Check testMethod. Run that testmethod. Double check # vertices and edges graph.

//        int distance = wordNet.distance("zero", "two");
//        System.out.println("Does this work at all? " + distance);
//        int one = wordNet.distance("one", "zero");
//        int nilch = wordNet.distance("nilch", "zero");

    }

    private static void testMethod(WordNet wordNet) {
        // Path between animate_being and animal.. what is the length?



//        try {
//            int bird = wordNet.distance("worm","bird");
//            int animal = wordNet.distance("animate_being", "animal");
//
//            System.out.println("distance for word: " + bird);
//            System.out.println("distance for animal: " + animal);
//        } catch (Exception E) {
//            int bird = wordNet.distance("worm","bird");
//            int animal = wordNet.distance("animate_being", "animal");
//
//            System.out.println(E);
//
//        }
//    }

//}

    }
}

// Write code to check to see if a dag is rooted or not.
// Count the number of nodes pointing out. If it's 0, it's a root.

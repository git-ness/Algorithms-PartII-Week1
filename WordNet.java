
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Topological;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class WordNet {

    private final ArrayList<ArrayList<Integer>> hypernymIntList = new ArrayList<>();
    private HashMap<String, HashSet<Integer>> nounToSynsetIdMap = new HashMap<>();
    private HashMap<Integer, HashSet<String>> synsetIdToNounsMap = new HashMap<>();
    private Digraph digraph;
    private SAP sap;
    private int verticesCount;
    private int root;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {

        In synsetsInput = new In(synsets);
        processSynsets(synsetsInput);
        processHypernyms(new In(hypernyms));
        int rootCounter = 0;

        for (int anc = 0; anc < digraph.V(); anc++) {

            if (digraph.outdegree(anc) == 0) {
//                System.out.println("I found the root, it is: " + anc);
                rootCounter++;
            }
        }
        if (rootCounter != 1) {
            throw new IllegalArgumentException("");
        }

        if (!(new Topological(digraph).hasOrder()));
        { throw new IllegalArgumentException(); }
    }

    private int root() {
        for (int anc = 0; anc < digraph.V(); anc++) {
//            System.out.println("anc " + digraph.outdegree(anc));
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

                HashSet<String> nounsWithinSynset = new HashSet<>();
                synsetIdToNounsMap.put(synsetId, nounsWithinSynset);

                for (String noun : wordsInSynset) {

                    nounsWithinSynset.add(noun);

                    HashSet<Integer> synsetIds = nounToSynsetIdMap.computeIfAbsent(noun, k -> new HashSet<>());
                    synsetIds.add(synsetId);
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

                int v = intList.get(0);
                int w = intList.get(i);

                // Debug
                if (v == 70904 || w == 70904  || v == 4497 || w == 4497) {
//                    System.out.println(v + " -> " + w);
                }
                digraph.addEdge(v, w);
            }
        }
        this.sap = new SAP(digraph);
//        System.out.println("38003 out: " + digraph.outdegree(38003));
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounToSynsetIdMap.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
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
            HashSet<String> nounsSet = synsetIdToNounsMap.get(ancestor);

            String nounAggregate = "";
            for ( String stringVar : nounsSet ) {
                nounAggregate += stringVar + " ";
            }
            return nounAggregate;
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {

//        WordNet wordNet = new WordNet("wordnettesting/processingBug.txt", "wordnettesting/processingBugHypernyms.txt");
        WordNet wordNet = new WordNet("wordnettesting/synsets.txt", "wordnettesting/hypernyms.txt");
        String synsetReturn = wordNet.sap("deoxyribose","scale_wax");
        System.out.println("sap return is: " + synsetReturn);



        int root = wordNet.root();
        System.out.println("Proposed root is: " + root);
//        testMethod(wordNet);


//        int isOne= wordNet.distance("word", "bird");
//        String isZero = wordNet.sap("one", "three");
//        System.out.println("distance is isOne?  " + isOne);

    }

//    private static void testMethod(WordNet wordNet) {
//        // Path between animate_being and animal.. what is the length?
//
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


// Write code to check to see if a dag is rooted or not.
// Count the number of nodes pointing out. If it's 0, it's a root.

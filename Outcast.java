import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    private WordNet wordNetCopy;  //TODO: Check to see if defensive copy is advised here. Don't see how to do it otherwise.
    private WordNet wordNet1;

    public Outcast(WordNet wordnet) {       // constructor takes a WordNet object
        this.wordNet1 = wordnet;



       /*---------- Memory Map ---------------
                    Goal to accomplish next (What's this things job?):

                          Go through each vertex in the node and determine which noun is "least" or farthest away in distance it is vs the other work.

                          The problem suggests the formula di   =   dist(Ai, A1)   +   dist(Ai, A2)   +   ...   +   dist(Ai, An)
                          although I'm not sure how adding them all up to create di does unless we have a local var that will keep track of the highest betwee
                          the sums.

                    Process - pseudo code (What we'll need):

                          String noun = hashMap.getSynetId conversion

                          int highestSoFar = 0;
                          for (int i = 0; i < hashMap.size; i++) {
                             // keep first noun as static in dist(Ai, A1)...dist(Ai, A2) etc. second noun will use i to iterate

                             int newPossibleHighest = dist(ai, AX)
                             if (highestSoFar < newPossibleHighest) {
                                highestSoFar = newPossibleHighest;
                             }
                          }

                           di   =   dist(Ai, A1)   +   dist(Ai, A2)   +   ...   +   dist(Ai, An)
	                       d_word1  =   dist(word1, word2)   +   dist(word1, word3)   +   ...   +   dist(word1, An)
	                       d_word2 = dist(word2, word2)   +   dist(word2, word3)   +   ...   +   dist(word2, An)

                             --------------
                           1. Take the words from the String array.
                           2.  iterate through the words
                           3. Have a candidate word that is the highest, with an if statement to replace the highest if it exceeds it.




                    Test Result Validation:
                           wordnettesting/outcast3.txt

                            horse
                            zebra
                            cat
                            bear
                            table

                            Answer is table as table doesn't not have a SAP of animal.
                            horse and zebra = "4"
                            horse and cat = "3"
                            ...etc

                            table and horse = "34"
                            -------

                            d_horse (in reff to the other words  = dist("horse", "zebra") + dist("horse", "cat") + dist("horse", "bear") + dist("horse", "bear") + dist("horse", "table")
                            d_zebra (in reff to the other words  = dist("zebra", "zebra") + dist("zebra", "cat") + dist("zebra", "bear") + dist("zebra", "bear") + dist("zebra", "table")
                            d_cat = dist("cat", "horse") + dist("cat", "zebra") + dist("cat", "bear")

                          HashMap<String, Integer> storedWords;
                          String noun = hashMap.getSynetId conversion

                          int distanceWordSum;
                          String word;
                          for (int i = 0; i < stringArray.length; i++) {
                            for(int j = 0; i < stringArray.length; i++) {
                             // keep first noun as static in dist(Ai, A1)...dist(Ai, A2) etc. second noun will use i to iterate

                              distanceWordSum += wordNet.distance(stringArray[i], stringArray[j]
                              hashMap.put (distanceWordSum, stringArray[i]);
                              }
                          }

                          for loop using hashmap key integer, determine the highest, and then return the string.


*/

    }

    public String outcast(String[] nouns) {  // given an array of WordNet nouns, return an outcast

        int distanceOfWordi = 0;
        int highestDistance = 0;
        int distanceOfWordiPlusOne = 0;
        String outcastStringCandidate = "";

        for (int i = 0; i < nouns.length; i++) {
            for (int j = 0; j < nouns.length; j++) {


                distanceOfWordi = wordNet1.distance(nouns[i], nouns[j]);
                distanceOfWordiPlusOne = wordNet1.distance(nouns[i + 1], nouns[j]);

                if (distanceOfWordi > distanceOfWordiPlusOne) {
                    highestDistance = distanceOfWordiPlusOne;
                    outcastStringCandidate = nouns[i+1];

                }

            }
        outcastStringCandidate = nouns[i];
        }


        return outcastStringCandidate;
    }


    public static void main(String[] args) { // see test client below

        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));

        }
    }
}

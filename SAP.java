import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class SAP {
    Digraph digraph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        this.digraph = G;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        return ancestorLength(v, w).getLength();
    }

    private LengthAndAncestor ancestorLength(Iterable<Integer> v, Iterable<Integer> w) {
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(digraph, w);

        int shortestDistanceCandidate = Integer.MAX_VALUE;
        int bestAncestor = 0;
        for (int anc = 0; anc < digraph.V(); anc++) {
            if (bfsV.hasPathTo(anc) && bfsW.hasPathTo(anc)) {

                int checkForNewCandidate = bfsV.distTo(anc) + bfsW.distTo(anc);
                if (checkForNewCandidate < shortestDistanceCandidate) {
                    shortestDistanceCandidate = checkForNewCandidate;
                    bestAncestor = anc;
                }
            }
        }
        if (shortestDistanceCandidate == Integer.MAX_VALUE) {
            return new LengthAndAncestor(-1, -1);
        }

        return new LengthAndAncestor(bestAncestor, shortestDistanceCandidate);
    }

    private class LengthAndAncestor {

        private int ancestorVertex;
        private int length;

        private LengthAndAncestor(int ancestorVertex, int length) {
            this.ancestorVertex = ancestorVertex;
            this.length = length;
        }

        private int getAncestorVertex() {
            return ancestorVertex;
        }

        private int getLength() {
            return length;
        }
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    private LengthAndAncestor ancestorLength(int v, int w) { // bfs.distTo = length

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(digraph, w);

        int shortestDistanceCandidate = Integer.MAX_VALUE;
        int bestAncestor = 0;
        for (int anc = 0; anc < digraph.V(); anc++) {
            if (bfsV.hasPathTo(anc) && bfsW.hasPathTo(anc)) {

                int checkForNewCandidate = bfsV.distTo(anc) + bfsW.distTo(anc);
                if (checkForNewCandidate < shortestDistanceCandidate) {
                    shortestDistanceCandidate = checkForNewCandidate;
                    bestAncestor = anc;
                }
            }
        }
        if (shortestDistanceCandidate == Integer.MAX_VALUE) {
            return new LengthAndAncestor(-1, -1);
        }

        return new LengthAndAncestor(bestAncestor, shortestDistanceCandidate);
    }

    public int ancestor(int v, int w) {
        return ancestorLength(v, w).getAncestorVertex();

    } // Returns a node id.

    // Private class called ancestor path that stores them. And you have to say NodeId and Length ...
    // Use a builder pattern. Then have getter setter methods.

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return 0;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {

        return ancestorLength(v, w).ancestorVertex;
    }

    private void returnTwoIterables() {

    }

    // do unit testing of this class
    public static void main(String[] args) {



    }
}